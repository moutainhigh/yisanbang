package com.vtmer.yisanbang.controller;

import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.domain.Refund;
import com.vtmer.yisanbang.dto.AgreeRefundDto;
import com.vtmer.yisanbang.service.RefundService;
import com.vtmer.yisanbang.vo.RefundVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;


@RestController
@RequestMapping("/refund")
public class RefundController {

    private static final Logger logger = LoggerFactory.getLogger(RefundController.class);

    @Autowired
    private RefundService refundService;

    @Autowired
    private WxPayService wxPayService;

    @Value("${wx.pay.refundNotifyURL}")
    private String refundNotifyURL;

    /**
     * 用户申请退款接口
     * @param refund：userId、orderId、reason
     * @return
     */
    @PostMapping("/apply")
    public ResponseMessage refund(@RequestBody Refund refund) {
        if (refund == null) {
            return ResponseMessage.newErrorInstance("传入参数有误");
        } else {
            int res = refundService.applyForRefund(refund);
            if (res == -1) {
                return ResponseMessage.newErrorInstance("订单id有误");
            } else if (res == 1) {
                return ResponseMessage.newErrorInstance("申请退款成功");
            }
            return ResponseMessage.newErrorInstance("申请退款失败，请稍后重试");
        }
    }

    /**
     * 根据订单id获取退款详情接口
     * @param orderId
     * @return
     */
    @GetMapping("/getByOrderId")
    public ResponseMessage getByOrderId(@RequestBody Integer orderId) {
        if (orderId == null) {
            return ResponseMessage.newErrorInstance("传入参数有误");
        }
        RefundVo refundVo = refundService.getRefundVoByOrderId(orderId);
        if (refundVo!=null) {
            return ResponseMessage.newSuccessInstance(refundVo,"获取退款详情信息成功");
        } else {
            return ResponseMessage.newErrorInstance("该订单id无退款信息,请检查传入的订单id");
        }
    }

    /**
     * 退款接口 —— 商家同意退款则调用该接口
     * 申请退款成功会更新订单表、退款表的状态，失败则返回相关信息
     * 使用该接口需要退款单的状态为退款中（待商家收货）（1）才能发起退款申请
     * @param agreeRefundDto:退款编号（必填）、退款原因（商家选填，如无货）
     * @return
     */
    @PostMapping("/agree")
    public ResponseMessage agreeRefund(@RequestBody AgreeRefundDto agreeRefundDto) {
        if (agreeRefundDto == null) {
            return ResponseMessage.newErrorInstance("传入参数有误");
        }
        String refundNumber = agreeRefundDto.getRefundNumber();
        Refund refund = refundService.selectByRefundNumber(refundNumber);
        if (refund == null) {
            return ResponseMessage.newErrorInstance("退款编号有误");
        } else if (refund.getStatus()!=1) {
            return ResponseMessage.newErrorInstance("该退款单的状态不适合同意退款");
        }
        AgreeRefundDto agreeRefundDto1 = refundService.getAgreeRefundDtoByRefundNumber(refundNumber);

        WxPayRefundRequest request = new WxPayRefundRequest();
        // 商户订单号
        request.setOutTradeNo(agreeRefundDto1.getOrderNumber());
        // 退款编号
        request.setOutRefundNo(refundNumber);
        // 订单金额
        request.setTotalFee(BaseWxPayRequest.yuanToFen(agreeRefundDto1.getTotalFee()));
        // 退款金额
        request.setRefundFee(BaseWxPayRequest.yuanToFen(agreeRefundDto1.getRefundFee()));
        // 退款原因
        String refundDesc = agreeRefundDto.getRefundDesc();
        if (refundDesc !=null) { // 如果退款原因不为null
            request.setRefundDesc(refundDesc);
        }
        // 退款退款结果通知url，可不设置
        // request.setNotifyUrl(refundNotifyURL);
        try {
            WxPayRefundResult refundResult = this.wxPayService.refund(request);
            if (refundResult.getReturnCode().equals("SUCCESS")) { // 调用申请退款接口成功
                if (refundResult.getResultCode().equals("SUCCESS")) {  // 退款成功
                    HashMap<String, Integer> refundMap = new HashMap<>();
                    refundMap.put("orderId",refund.getOrderId());
                    refundMap.put("status",2);
                    refundService.updateRefundStatus(refundMap);
                    return ResponseMessage.newSuccessInstance("微信退款成功");
                } else { // 退款失败
                    logger.error("微信退款失败,错误码{},错误原因{}",refundResult.getErrCode(),refundResult.getErrCodeDes());
                    return ResponseMessage.newErrorInstance("退款失败，请稍后重试！");
                }
            } else { // 调用申请退款接口失败
                logger.error("调用申请退款接口失败,错误原因{}",refundResult.getReturnMsg());
                return ResponseMessage.newErrorInstance("退款失败，请稍后重试！");
            }
        } catch (WxPayException e) {
            logger.error("微信退款结果异常,异常原因{}", e.getMessage());
            return ResponseMessage.newErrorInstance("退款失败，请稍后重试！");
        }
    }

    /**
     * 更新退款订单状态接口
     * 待商家处理-->待商家收货or退款失败   and   待商家收货（商家已同意退款）-->退款成功or退款失败（商家未收到货）
     * status改变： 0 --> 1 or 3   and   1 --> 2 or 3
     * @param refund：orderId,status
     * @return
     */
    @PutMapping("/update")
    public ResponseMessage updateRefundStatus(@RequestBody Refund refund) {
        Integer status = refund.getStatus();
        if (!(status>=1 && status<=3)) {
            return ResponseMessage.newErrorInstance("欲修改的退款状态超出范围");
        }
        Integer orderId = refund.getOrderId();
        RefundVo refundVo = refundService.getRefundVoByOrderId(orderId);
        if (refundVo == null) {
            return ResponseMessage.newErrorInstance("该订单id不存在退款信息");
        }
        HashMap<String, Integer> refundMap = new HashMap<>();
        refundMap.put("orderId",orderId);
        refundMap.put("status",status);
        int res = refundService.updateRefundStatus(refundMap);
        if (res == -1) {
            return ResponseMessage.newErrorInstance("欲更新的状态与订单原状态相同");
        } else if (res == -2) {
            return ResponseMessage.newErrorInstance("不能由待商家处理状态更新至退款成功状态");
        } else if (res == 1) {
            return ResponseMessage.newSuccessInstance("更新退款状态成功");
        } else {
            return ResponseMessage.newErrorInstance("更新退款状态失败");
        }
    }

    /**
     * 获取相应状态的退款订单
     * 退款状态status 0--等待商家处理  1--退款中（待商家收货） 2--退款成功 3--退款失败
     * 4 -- 获取所有退款订单
     * @param status
     * @return List<RefundVo>
     */
    @GetMapping("/getByStatus")
    public ResponseMessage getByStatus(@RequestBody Integer status) {
        if (status == null) {
            return ResponseMessage.newErrorInstance("传入参数有误");
        } else if (!(status>=0 && status<=4)) {
            return ResponseMessage.newErrorInstance("传入参数超过范围");
        }
        List<RefundVo> refundVoList = refundService.getRefundVoListByStatus(status);
        if (refundVoList!=null) {
            return ResponseMessage.newSuccessInstance(refundVoList,"获取相应状态的退款订单成功");
        } else {
            return ResponseMessage.newSuccessInstance("暂无该类型的退款订单");
        }
    }

    /**
     * 根据退款编号删除退款订单接口
     * @param refundNumber:退款编号
     * @return
     */
    @DeleteMapping("/delete")
    public ResponseMessage delete(@RequestBody String refundNumber) {
        if (refundNumber == null) {
            return ResponseMessage.newErrorInstance("传入参数有误");
        }
        int res = refundService.deleteByRefundNumber(refundNumber);
        if (res == -1) {
            return ResponseMessage.newErrorInstance("传入的退款编号有误");
        } else if (res == 1) {
            return ResponseMessage.newSuccessInstance("删除退款订单成功");
        } else {
            return ResponseMessage.newErrorInstance("删除退款订单失败");
        }
    }
}
