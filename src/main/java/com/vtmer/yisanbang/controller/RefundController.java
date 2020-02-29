package com.vtmer.yisanbang.controller;

import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayRefundQueryRequest;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.result.WxPayRefundQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.common.annotation.RequestLog;
import com.vtmer.yisanbang.common.exception.api.ApiException;
import com.vtmer.yisanbang.common.exception.api.order.ApiOrderAndUserNotMatchException;
import com.vtmer.yisanbang.common.exception.api.order.ApiOrderNotFoundException;
import com.vtmer.yisanbang.common.exception.api.order.ApiOrderStatusNotFitException;
import com.vtmer.yisanbang.common.exception.api.refund.ApiDuplicateApplyRefundException;
import com.vtmer.yisanbang.common.exception.api.refund.ApiRefundNotFoundException;
import com.vtmer.yisanbang.common.exception.api.refund.ApiRefundNotMatchUserException;
import com.vtmer.yisanbang.common.exception.api.refund.ApiRefundStatusNotFitException;
import com.vtmer.yisanbang.common.exception.service.order.OrderAndUserNotMatchException;
import com.vtmer.yisanbang.common.exception.service.order.OrderNotFoundException;
import com.vtmer.yisanbang.common.exception.service.refund.*;
import com.vtmer.yisanbang.domain.Refund;
import com.vtmer.yisanbang.domain.RefundExpress;
import com.vtmer.yisanbang.dto.AgreeRefundDTO;
import com.vtmer.yisanbang.dto.RefundDTO;
import com.vtmer.yisanbang.service.OrderService;
import com.vtmer.yisanbang.service.RefundService;
import com.vtmer.yisanbang.vo.RefundVo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;

@Api(tags = "退款接口", value = "用户部分&后台管理部分")
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
     * 根据订单id获取退款详情接口
     *
     * @param orderId: 订单id
     * @return
     */
    @RequestLog(module = "退款", operationDesc = "获取退款详情")
    @ApiOperation(value = "获取退款详情", notes = "根据订单id获取退款详情接口")
    @GetMapping("/getByOrderId/{orderId}")
    public ResponseMessage<RefundVo> getByOrderId(@ApiParam(value = "订单id", example = "1", required = true, name = "orderId")
                                                  @NotNull(message = "订单id为空") @PathVariable Integer orderId) {
        RefundVo refundVo;
        try {
            refundVo = refundService.getRefundVoByOrderId(orderId);
        } catch (RefundNotFoundException e) {
            throw new ApiRefundNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new ApiException(e);
        }
        return ResponseMessage.newSuccessInstance(refundVo, "获取退款详情信息成功");
    }

    /**
     * 商家同意退款则调用该接口
     * status 2-->3 0-->3使用
     * 两种情况可调用该接口：1.未发货的情况下，用户申请退款； 2. 已发货的情况下，商家收到退货商品.(商家待收货)
     * 申请退款成功会更新订单表、退款表的状态，失败则返回相关信息
     * 订单状态定义：status 订单状态 0--待付款 1--待发货 2--待收货 3--已完成 4--交易关闭 5--所有订单
     * 退款状态定义：status 退款状态 0--等待商家处理  1--退款中（待买家发货） 2--退款中（待商家收货） 3--退款成功 4--退款失败
     *
     * @param
     * @return
     */
    @RequestLog(module = "商家同意退款", operationDesc = "商家同意退款")
    @ApiOperation(value = "退款接口", notes = "商家同意退款则调用该接口，" +
            "只有在【待商家处理】或【待商家收货】的退款状态下可调用，调用成功后退款金额将原路退回给用户\n" +
            "退款状态定义：status 退款状态 0--等待商家处理  1--退款中（待买家发货） 2--退款中（待商家收货） 3--退款成功 4--退款失败")
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/agree")
    public ResponseMessage agreeRefund(@ApiParam(value = "退款编号", example = "12345678998765432110")
                                       @NotBlank(message = "refundNumber is null")
                                       @RequestParam String refundNumber,
                                       @ApiParam(value = "退款原因(非必需),若商户传入，会在下发给用户的退款消息中体现退款原因", example = "西装无货")
                                       @NotBlank(message = "refundDesc is null")
                                       @RequestParam(required = false) String refundDesc) {
        AgreeRefundDTO agreeRefundDTO = new AgreeRefundDTO();
        agreeRefundDTO.setRefundNumber(refundNumber);
        agreeRefundDTO.setRefundDesc(refundDesc);
        try {
            refundService.setAgreeRefundDTOByRefundNumber(agreeRefundDTO);
        } catch (RefundNotFoundException e) {
            throw new ApiRefundNotFoundException(e.getMessage());
        } catch (RefundStatusNotFitException e) {
            throw new ApiRefundStatusNotFitException(e.getMessage());
        } catch (Exception e) {
            throw new ApiException(e);
        }
        WxPayRefundRequest request = new WxPayRefundRequest();
        // 商户订单号
        request.setOutTradeNo(agreeRefundDTO.getOrderNumber());
        // 退款编号
        request.setOutRefundNo(refundNumber);
        // 订单金额
        request.setTotalFee(BaseWxPayRequest.yuanToFen(agreeRefundDTO.getTotalFee()));
        // 退款金额
        request.setRefundFee(BaseWxPayRequest.yuanToFen(agreeRefundDTO.getRefundFee()));
        if (refundDesc != null) { // 如果退款原因不为null
            request.setRefundDesc(refundDesc);
        }
        // 退款退款结果通知url，可不设置
        // request.setNotifyUrl(refundNotifyURL);
        try {
            WxPayRefundResult refundResult = this.wxPayService.refund(request);
            if ("SUCCESS".equals(refundResult.getReturnCode())) {
                // 调用申请退款接口成功
                if ("SUCCESS".equals(refundResult.getResultCode())) {
                    // 申请退款成功
                    Refund refund = refundService.selectByRefundNumber(refundNumber);
                    // 更新退款表状态为 退款成功
                    HashMap<String, Integer> refundMap = new HashMap<>();
                    refundMap.put("orderId", refund.getOrderId());
                    refundMap.put("status", 3);
                    refundService.updateRefundStatus(refundMap);
                    logger.info("退款单[{}]状态更新为[退款成功]", refundNumber);
                    // 更新订单表状态为 已完成
                    HashMap<String, Integer> orderMap = new HashMap<>();
                    orderMap.put("orderId", refund.getOrderId());
                    orderMap.put("status", 3);
                    logger.info("订单id[{}]状态更新为[已完成]", refund.getOrderId());
                    orderService.setOrderStatus(orderMap);
                    // 库存归位
                    String orderNumber = refundResult.getOutRefundNo();
                    // 1 代表增加库存
                    orderService.updateInventory(orderNumber, 1);
                    return ResponseMessage.newSuccessInstance("微信退款成功");
                } else {
                    // 退款失败
                    logger.error("微信退款失败,错误码{},错误原因{}", refundResult.getErrCode(), refundResult.getErrCodeDes());
                    return ResponseMessage.newErrorInstance("退款失败，请稍后重试！");
                }
            } else {
                // 调用申请退款接口失败
                logger.error("调用申请退款接口失败,错误原因{}", refundResult.getReturnMsg());
                return ResponseMessage.newErrorInstance("退款失败，请稍后重试！");
            }
        } catch (WxPayException e) {
            logger.error("微信退款结果异常,异常原因{}", e.getMessage());
            throw new ApiException(e);
        }
    }

    /**
     * 更新退款退货订单状态接口，status0-->1
     * 使用，待商家处理-->待用户发货 0 --> 1
     * status全改变： 0 --> 1 or 3 or 4  and  1 --> 2   and  2 --> 3  and 0 1 2-->删除
     * 订单状态定义：status 订单状态 0--待付款 1--待发货 2--待收货 3--已完成 4--交易关闭 5--所有订单
     * 退款状态定义：status 退款状态 0--等待商家处理  1--退款中（待买家发货） 2--退款中（待商家收货） 3--退款成功 4--退款失败
     *
     * @param refundNumber:退款编号
     * @return
     */
    @RequestLog(module = "商家同意退款退货申请", operationDesc = "【待商家处理】-->【待用户发货】")
    @ApiOperation(value = "同意退款之【待商家处理】-->【待用户发货】",
            notes = "更新退款订单状态，【待商家处理】-->【待用户发货】")
    @PutMapping("/update/{refundNumber}")
    public ResponseMessage updateRefundStatus(@ApiParam(name = "refundNumber", value = "退款编号", example = "12345678998765432110", required = true)
                                              @NotBlank(message = "退款编号为空") @PathVariable String refundNumber) {
        try {
            refundService.agreeRefundApply(refundNumber);
        } catch (RefundNotFoundException e) {
            throw new ApiRefundNotFoundException(e.getMessage());
        } catch (RefundStatusNotFitException e) {
            throw new ApiRefundStatusNotFitException(e.getMessage());
        } catch (Exception e) {
            throw new ApiException(e);
        }
        return ResponseMessage.newSuccessInstance("更新退款状态成功");
    }

    /**
     * 商家查询成功申请了退款(退款成功)的订单的退款状态
     *
     * @param refundNumber：退款编号
     * @return
     */
    @RequestLog(module = "退款", operationDesc = "查询成功申请了退款(退款成功)的订单的退款状态")
    @ApiOperation(value = "商家查询成功申请了退款(退款成功)的订单的退款状态",
            notes = "调用该接口的前提是商家已同意退款，查询微信端退款情况（受理中、退款成功、退款失败）")
    @GetMapping("/refundQuery/{refundNumber}")
    public ResponseMessage refundQuery(@ApiParam(name = "refundNumber", value = "退款编号", example = "12345678998765432110", required = true)
                                       @NotBlank(message = "退款编号为空") @PathVariable String refundNumber) {
        Refund refund = refundService.selectByRefundNumber(refundNumber);
        if (refund == null) {
            throw new ApiRefundNotFoundException("找不到该退款单");
        } else if (refund.getStatus() != 3) { // 如果退款状态不为 退款成功(3)
            throw new ApiOrderStatusNotFitException("该退款编号还未成功申请退款");
        }
        WxPayRefundQueryRequest request = new WxPayRefundQueryRequest();
        request.setOutRefundNo(refundNumber);
        try {
            WxPayRefundQueryResult result = this.wxPayService.refundQuery(request);
            // 如果请求成功
            if ("SUCCESS".equals(result.getReturnCode())) {
                List<WxPayRefundQueryResult.RefundRecord> refundRecords = result.getRefundRecords();
                if (refundRecords != null && refundRecords.size() != 0) {
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
                            default: return ResponseMessage.newErrorInstance("查询退款单状态失败，请稍后重试");
                        } // end switch
                    } // end for
                } // end for
                return ResponseMessage.newSuccessInstance("该退款编号未申请微信退款");
            } else {
                return ResponseMessage.newErrorInstance("请求微信接口失败，请稍后重试");
            }
        } catch (WxPayException e) {
            logger.error("查询退款状态失败,异常原因{}", e.getMessage());
            throw new ApiException(e);
        }
    }


    /**
     * 商家获取相应状态的退款订单
     * 退款状态定义：status 退款状态 0--等待商家处理  1--退款中（待买家发货） 2--退款中（待商家收货） 3--退款成功 4--退款失败
     * 5 -- 获取所有退款订单
     *
     * @param status：退款状态
     * @return List<RefundVo>
     */
    @RequestLog(module = "退款", operationDesc = "获取相应状态的退款订单")
    @ApiOperation(value = "商家获取相应状态的退款订单详情", notes = "该接口可在后台管理系统中调用，方便商家查看\n" +
            "退款状态定义：status 退款状态 0--等待商家处理  1--退款中（待买家发货） 2--退款中（待商家收货） 3--退款成功 4--退款失败")
    @GetMapping("/getByStatus/{status}")
    public ResponseMessage<List<RefundVo>> getByStatus(@ApiParam(value = "退款状态", example = "3", required = true)
                                                       @PathVariable Integer status) {

        List<RefundVo> refundVoList;
        try {
            refundVoList = refundService.getRefundVoListByStatus(status);
        } catch (RefundStatusNotFitException e) {
            throw new ApiRefundStatusNotFitException(e.getMessage());
        } catch (Exception e) {
            throw new ApiException(e);
        }
        if (refundVoList != null && refundVoList.size() != 0) {
            return ResponseMessage.newSuccessInstance(refundVoList, "获取相应状态的退款订单成功");
        } else {
            return ResponseMessage.newSuccessInstance("暂无该类型的退款订单");
        }
    }

    /**
     * 用户撤销退款，根据退款编号删除退款订单接口
     * 该接口应该由用户调用，商家不应该动客户的退款信息
     *
     * @param refundNumber:退款编号
     * @return
     */
    @RequestLog(module = "退款", operationDesc = "撤销退款申请")
    @ApiOperation(value = "用户撤销退款申请", notes = "根据退款编号删除退款订单接口,用户撤销退款使用")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "校验token", name = "Authorization", paramType = "header", required = true)
    })
    @DeleteMapping("/delete")
    public ResponseMessage delete(@ApiParam(name = "refundNumber", value = "退款编号", example = "12345678998765432110", required = true)
                                  @NotBlank(message = "退款编号为空") @PathVariable String refundNumber) {
        try {
            refundService.deleteByRefundNumber(refundNumber);
        } catch (RefundNotFoundException e) {
            throw new ApiRefundNotFoundException(e.getMessage());
        } catch (RefundStatusNotFitException e) {
            throw new ApiRefundStatusNotFitException(e.getMessage());
        } catch (RefundNotMatchUserException e) {
            throw new ApiRefundNotMatchUserException(e.getMessage());
        } catch (Exception e) {
            throw new ApiException(e);
        }
        return ResponseMessage.newSuccessInstance("删除退款订单成功");
    }

    /**
     * 商家拒绝退款申请 status 0-->4
     *
     * @param refundNumber:退款编号
     * @return
     */
    @RequestLog(module = "退款", operationDesc = "商家拒绝退款申请")
    @ApiOperation(value = "商家拒绝退款申请", notes = "商家拒绝退款申请调用，退款状态：【待商家处理】-->【退款失败】")
    @PutMapping("/refuseApplication/{refundNumber}")
    public ResponseMessage refuseApplication(@ApiParam(name = "refundNumber", value = "退款编号", example = "12345678998765432110", required = true)
                                             @NotBlank(message = "退款编号为空") @PathVariable String refundNumber) {
        try {
            refundService.refuseRefundApply(refundNumber);
        } catch (RefundNotFoundException e) {
            throw new ApiRefundNotFoundException(e.getMessage());
        } catch (RefundStatusNotFitException e) {
            throw new ApiRefundStatusNotFitException(e.getMessage());
        } catch (Exception e) {
            throw new ApiException(e);
        }
        return ResponseMessage.newSuccessInstance("拒绝退款申请成功");
    }

    /**
     * 用户填写退款发货单
     * status 1-->2
     *
     * @param refundExpress:refundId、expressCompany(选填)、courierNumber(快递单号)
     * @return
     */
    @RequestLog(module = "退款", operationDesc = "用户填写退款发货单")
    @ApiOperation(value = "用户填写退款发货单",
            notes = "用户填写退款发货单,退款状态【待用户发货】-->【待商家收货】")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "校验token", name = "Authorization", paramType = "header", required = true)
    })
    @PostMapping("/express")
    public ResponseMessage insertExpress(@RequestBody @Validated RefundExpress refundExpress) {
        try {
            refundService.insertRefundExpress(refundExpress);
        } catch (RefundNotFoundException e) {
            throw new ApiRefundNotFoundException(e.getMessage());
        } catch (RefundStatusNotFitException e) {
            throw new ApiRefundStatusNotFitException(e.getMessage());
        } catch (RefundNotMatchUserException e) {
            throw new ApiRefundNotMatchUserException(e.getMessage());
        } catch (Exception e) {
            throw new ApiException(e);
        }
        return ResponseMessage.newSuccessInstance("填写退款发货单成功");
    }

    /**
     * 用户申请退款接口：
     * 退款商品传null代表全退，此时可不传退款金额
     * 部分退需要传退款金额，退款商品信息
     * @param refundDTO：refund，refundGoodsList 退款原因，退款金额，订单id，退款商品:sizeId、isGoods
     * @return
     */
    @RequestLog(module = "退款", operationDesc = "申请退款")
    @ApiOperation(value = "用户申请退款", notes = "用户申请退款接口;退款商品传null代表全退，退款原因非必需")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "校验token", name = "Authorization", paramType = "header", required = true)
    })
    @PostMapping("/apply")
    public ResponseMessage apply(@RequestBody @Validated RefundDTO refundDTO) {
        try {
            refundService.applyForRefund(refundDTO);
        } catch (OrderNotFoundException e) {
            throw new ApiOrderNotFoundException(e.getMessage());
        } catch (DuplicateApplyRefundException e) {
            throw new ApiDuplicateApplyRefundException(e.getMessage());
        } catch (OrderAndUserNotMatchException e) {
            throw new ApiOrderAndUserNotMatchException(e.getMessage());
        } catch (RefundGoodsNotFoundException e) {
            throw new ApiRefundNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new ApiException(e);
        }
        return ResponseMessage.newSuccessInstance("申请退款成功");
    }
}
