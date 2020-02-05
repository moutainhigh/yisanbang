package com.vtmer.yisanbang.controller;

import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayRefundQueryRequest;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.result.WxPayRefundQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.domain.Refund;
import com.vtmer.yisanbang.dto.AgreeRefundDto;
import com.vtmer.yisanbang.service.OrderService;
import com.vtmer.yisanbang.service.RefundService;
import com.vtmer.yisanbang.vo.RefundVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    @Autowired
    private OrderService orderService;

    /**
     * 退款逻辑梳理
     * 一个订单，发起申请退款后不能再次发起(申请了不能再申请)，申请退款后可以撤销申请，撤销退款申请后即可再次发起申请
     * 一个退款订单在退款中也无法再次发起申请，只有退款失败才可重新发起退款；
     * 退款失败的原因有 1. 用户主动撤销  2. 商家拒绝退款  退款失败订单的状态都应该退回之前的状态
     * 能发起退款的订单有 1. 从未发起过退款申请的订单 2. 退款失败的订单（包括用户主动撤销和商家拒绝退款）
     * 用户主动撤销：订单归为之前状态，删除退款表数据库记录;
     * 商家拒绝退款：订单归为之前状态，退款状态改为退款失败;
     * 退款失败显示定义：商家拒绝退款
     * 按此设计 一个orderId只能有一个退款编号：
     * 当用户撤销退款申请就从数据库中删除该条退款记录，用户无法看到自己的撤销申请记录
     * 当商家拒绝退款更改退款表状态为退款失败，下次再次发起退款申请时生成新的退款编号并更新退款信息
     * 问题是用户撤销退款申请、商家拒绝退款申请之后如何让订单退回之前的状态——解决：退款的时候不更新订单表状态，通过获取orderId查询退款表获取退款状态
     *
     * 订单状态定义：status 订单状态 0--待付款 1--待发货 2--待收货 3--已完成 4--交易关闭 5--所有订单
     * 退款状态定义：status 退款状态 0--等待商家处理  1--退款中（待买家发货） 2--退款中（待商家收货） 3--退款成功 4--退款失败
     */


    /**
     * 用户申请退款接口：未收到货（仅退款、全退）
     * @param refund：orderId、reason
     * @return
     */
    @PostMapping("/applyNotReceived")
    public ResponseMessage refund(@RequestBody Refund refund) {
        if (refund == null) {
            return ResponseMessage.newErrorInstance("传入参数有误");
        } else {
            int res = refundService.applyForRefund(refund);
            if (res == -1) {
                return ResponseMessage.newErrorInstance("订单id有误");
            } else if (res == -2) {
                return ResponseMessage.newErrorInstance("重复申请退款");
            } else if (res == 1) {
                return ResponseMessage.newSuccessInstance("申请退款成功");
            }
            return ResponseMessage.newErrorInstance("申请退款失败，请稍后重试");
        }
    }

    /**
     * 根据订单id获取退款详情接口
     * @param orderId: 订单id
     * @return
     */
    @GetMapping("/getByOrderId/{id}")
    public ResponseMessage getByOrderId(@PathVariable("id") Integer orderId) {
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
     * status 2-->3 0-->3使用
     * 两种情况可调用该接口：1.未发货的情况下，用户申请退款； 2. 已发货的情况下，商家收到退货商品.(商家待收货)
     * 申请退款成功会更新订单表、退款表的状态，失败则返回相关信息
     * 订单状态定义：status 订单状态 0--待付款 1--待发货 2--待收货 3--已完成 4--交易关闭 5--所有订单
     * 退款状态定义：status 退款状态 0--等待商家处理  1--退款中（待买家发货） 2--退款中（待商家收货） 3--退款成功 4--退款失败
     * @param agreeRefundDto:退款编号（必填）、退款原因（商家选填，如无货）
     * @return
     */
    @Transactional
    @PostMapping("/agree")
    public ResponseMessage agreeRefund(@RequestBody AgreeRefundDto agreeRefundDto) {
        if (agreeRefundDto == null) {
            return ResponseMessage.newErrorInstance("传入参数有误");
        }
        String refundNumber = agreeRefundDto.getRefundNumber();
        Refund refund = refundService.selectByRefundNumber(refundNumber);
        if (refund == null) {
            return ResponseMessage.newErrorInstance("退款编号有误");
        } else if (refund.getStatus()!=2 && refund.getIsReceived() == 1) {
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
                if (refundResult.getResultCode().equals("SUCCESS")) {  // 申请退款成功
                    // 更新退款表状态为 退款成功
                    HashMap<String, Integer> refundMap = new HashMap<>();
                    refundMap.put("orderId",refund.getOrderId());
                    refundMap.put("status",3);
                    refundService.updateRefundStatus(refundMap);
                    // 更新订单表状态为 已完成
                    HashMap<String, Integer> orderMap = new HashMap<>();
                    orderMap.put("orderId",refund.getOrderId());
                    orderMap.put("status",3);
                    orderService.setOrderStatus(orderMap);
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
     * 更新退款订单状态接口，status0-->1
     * 使用，待商家处理-->待用户发货 0 --> 1
     * status全改变： 0 --> 1 or 3 or 4  and  1 --> 2   and  2 --> 3  and 0 1 2-->删除
     * 订单状态定义：status 订单状态 0--待付款 1--待发货 2--待收货 3--已完成 4--交易关闭 5--所有订单
     * 退款状态定义：status 退款状态 0--等待商家处理  1--退款中（待买家发货） 2--退款中（待商家收货） 3--退款成功 4--退款失败
     * @param refundNumberMap:refundNumber退款编号
     * @return
     */
    @PutMapping("/update")
    public ResponseMessage updateRefundStatus(@RequestBody Map<String,String> refundNumberMap) {
        String refundNumber = refundNumberMap.get("refundNumber");
        Refund refund = refundService.selectByRefundNumber(refundNumber);

        if (refund == null) {
            return ResponseMessage.newErrorInstance("退款编号有误");
        } else if (refund.getStatus()!=0) {
            return ResponseMessage.newErrorInstance("该退款单的状态不为待商家处理，不能调用该接口");
        }

        HashMap<String, Integer> refundMap = new HashMap<>();
        refundMap.put("orderId",refund.getOrderId());
        refundMap.put("status",1);
        int res = refundService.updateRefundStatus(refundMap);
        if (res == 1) {
            return ResponseMessage.newSuccessInstance("更新退款状态成功");
        } else {
            return ResponseMessage.newErrorInstance("更新退款状态失败");
        }
    }

    /**
     * 查询成功申请了退款(退款成功)的订单的退款状态
     * @param refundNumberMap:refundNumber：退款编号
     * @return
     */
    @PostMapping("/refundQuery")
    public ResponseMessage refundQuery(@RequestBody Map<String,String> refundNumberMap) {
        String refundNumber = refundNumberMap.get("refundNumber");
        if (refundNumber == null) {
            return ResponseMessage.newErrorInstance("传入参数有误");
        }
        Refund refund = refundService.selectByRefundNumber(refundNumber);
        if (refund.getStatus()!=3) { // 如果退款状态不为 退款成功(3)
            return ResponseMessage.newErrorInstance("该退款编号还未成功申请退款");
        }
        WxPayRefundQueryRequest request = new WxPayRefundQueryRequest();
        request.setOutRefundNo(refundNumber);
        try {
            WxPayRefundQueryResult result = this.wxPayService.refundQuery(request);
            // 如果请求成功
            if (result.getReturnCode().equals("SUCCESS")) {
                List<WxPayRefundQueryResult.RefundRecord> refundRecords = result.getRefundRecords();
                // 因为只有一笔退款，退款金额即为订单金额，所以refundRecords.size为1 在循环中返回结果
                for (WxPayRefundQueryResult.RefundRecord refundRecord : refundRecords) {
                    // 订单退款状态
                    String refundStatus = refundRecord.getRefundStatus();
                    switch (refundStatus) {
                        case "SUCCESS":   // // 订单退款成功
                            return ResponseMessage.newSuccessInstance("退款成功，款项已经原路返回");
                        case "PROCESSING":   // 退款处理中
                            return ResponseMessage.newSuccessInstance("退款处理中");
                        case "CHANGE":  // 退款异常
                            logger.info("退款异常，退款到银行发现用户的卡作废或者冻结了，导致原路退款银行卡失败.退款编号{}", refundNumber);
                            return ResponseMessage.newSuccessInstance("退款异常，退款到银行发现用户的卡作废或者冻结了，" +
                                    "导致原路退款银行卡失败, 可前往商户平台（pay.weixin.qq.com）-交易中心，手动处理此笔退款。");
                        case "REFUNDCLOSE": // 退款关闭
                            return ResponseMessage.newSuccessInstance("退款关闭");
                    }
                } // end for
                return ResponseMessage.newSuccessInstance("该退款编号未申请微信退款");
            } else {
                return ResponseMessage.newErrorInstance("请求微信接口失败，请稍后重试");
            }
        } catch (WxPayException e) {
            logger.error("查询退款状态失败,异常原因{}",e.getMessage());
            e.printStackTrace();
            return ResponseMessage.newErrorInstance("查询退款状态失败，请稍后重试");
        }
    }


    /**
     * 获取相应状态的退款订单
     * 退款状态定义：status 退款状态 0--等待商家处理  1--退款中（待买家发货） 2--退款中（待商家收货） 3--退款成功 4--退款失败
     * 5 -- 获取所有退款订单
     * @param status：退款状态
     * @return List<RefundVo>
     */
    @GetMapping("/getByStatus/{status}")
    public ResponseMessage getByStatus(@PathVariable(value = "status") Integer status) {
        if (status == null) {
            return ResponseMessage.newErrorInstance("传入参数有误");
        } else if (!(status>=0 && status<=5)) {
            return ResponseMessage.newErrorInstance("传入参数超过范围");
        }
        List<RefundVo> refundVoList = refundService.getRefundVoListByStatus(status);
        if (refundVoList!=null && refundVoList.size()!=0) {
            return ResponseMessage.newSuccessInstance(refundVoList,"获取相应状态的退款订单成功");
        } else {
            return ResponseMessage.newSuccessInstance("暂无该类型的退款订单");
        }
    }

    /**
     * 根据退款编号删除退款订单接口,用户撤销退款使用
     * 该接口应该由用户调用，商家不应该动客户的退款信息
     * @param refundNumberMap:refundNumber退款编号
     * @return
     */
    @DeleteMapping("/delete")
    public ResponseMessage delete(@RequestBody Map<String,String> refundNumberMap) {
        String refundNumber = refundNumberMap.get("refundNumber");
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


    /**
     * TODO 商家拒绝申请
     * status 0-->4
     */

    /**
     * TODO 用户填写退款发货单
     * status 1-->2
     */
}
