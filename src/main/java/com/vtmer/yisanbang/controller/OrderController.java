package com.vtmer.yisanbang.controller;

import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.BaseWxPayResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.util.SignUtils;
import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.common.TokenInterceptor;
import com.vtmer.yisanbang.common.annotation.RequestLog;
import com.vtmer.yisanbang.common.exception.api.ApiException;
import com.vtmer.yisanbang.common.exception.api.order.*;
import com.vtmer.yisanbang.common.exception.service.order.*;
import com.vtmer.yisanbang.common.valid.group.Insert;
import com.vtmer.yisanbang.domain.Order;
import com.vtmer.yisanbang.domain.User;
import com.vtmer.yisanbang.dto.CartOrderDTO;
import com.vtmer.yisanbang.dto.DeliverGoodsDTO;
import com.vtmer.yisanbang.dto.OrderDTO;
import com.vtmer.yisanbang.service.OrderService;
import com.vtmer.yisanbang.vo.UpdateUserAddressVo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Api(tags = "订单接口", value = "用户部分")
@RestController
@RequestMapping("/order")
public class OrderController {

    private final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private WxPayService wxPayService;

    @Value("${wx.pay.spbillCreateIp}")
    private String spbillCreateIp;

    /**
     * 点击去结算，显示确认订单页面
     *
     * @return
     */
    @RequestLog(module = "订单", operationDesc = "确认订单")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "校验token", name = "accessToken", paramType = "header", required = true)
    })
    @ApiOperation(value = "确认订单", notes = "点击去结算，显示确认订单页面")
    @GetMapping("confirmOrder")
    public ResponseMessage<OrderDTO> confirmOrder() {
        OrderDTO orderDTO;
        try {
            orderDTO = orderService.confirmCartOrder();
        } catch (CartEmptyException e) {
            throw new ApiCartEmptyException(e.getMessage());
        }
        return ResponseMessage.newSuccessInstance(orderDTO, "获取确认订单相关信息成功");
    }


    /**
     * 创建订单
     *
     * @param cartOrderDTO：留言，用户收货地址，邮费
     * @return
     */
    @RequestLog(module = "订单", operationDesc = "创建购物车类订单")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "校验token", name = "accessToken", paramType = "header", required = true)
    })
    @ApiOperation(value = "创建购物车订单", notes = "用户从购物车页面确认订单，点击提交订单后调用,若是订单中的某件商品数量超过库存，会返回【库存不足】的提示，返回订单编号和用户openid")
    @PostMapping("/insert")
    public ResponseMessage insert(@RequestBody @Validated({Insert.class}) CartOrderDTO cartOrderDTO) {
        Map<String, String> orderMap;
        try {
            orderMap = orderService.createCartOrder(cartOrderDTO);
        } catch (InventoryNotEnoughException e) {
            throw new ApiInventoryNotEnoughException(e.getMessage());
        } catch (Exception e) {
            throw new ApiException(e);
        }
        return ResponseMessage.newSuccessInstance(orderMap, "创建订单成功，返回订单编号和openId");
    }

    /**
     * 通过订单号进行微信支付
     * @param orderNumber：orderNumber订单号
     * @return 返回前端调起微信支付所需的支付参数（5个参数和sign）
     */
    @RequestLog(module = "订单", operationDesc = "微信支付")
    @ApiOperation(value = "微信支付", notes = "通过订单号进行微信支付，返回前端调起微信支付所需的支付参数（5个参数和sign）")
    @ApiResponses({
            @ApiResponse(code = 200, message = "ResponseMessage => \n 'data:" +
                    "{\n'appId':'wxd678efh567hg6787'" +
                    "\n'timeStamp':'1490840662'" +
                    "\n'nonceStr':'5K8264ILTKCH16CQ2502SI8ZNMTM67VS'" +
                    "\n'packageValue':'prepay_id=wx2017033010242291fcfe0db70013231072'" +
                    "\n'signType':'MD5'" +
                    "\n'paySign':' MD5(appId=wxd678efh567hg6787&nonceStr=5K8264ILTKCH16CQ2502SI8ZNMTM67VS&package=prepay_id=wx2017033010242291fcfe0db70013231072&signType=MD5&timeStamp=1490840662&key=qazwsxedcrfvtgbyhnujmikolp111111) = 22D9B4E54AB1950F51E0649E8810ACD6'" +
                    "\n}")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(value = "校验token", name = "accessToken", paramType = "header", required = true)
    })
    @GetMapping("/wxpay/{orderNumber}")
    public ResponseMessage wxpay(@ApiParam(name = "orderNumber", value = "订单编号", required = true)
                                 @NotBlank(message = "订单号传入为空") @PathVariable String orderNumber) {
        OrderDTO orderDTO = orderService.selectOrderDTOByOrderNumber(orderNumber);
        User user = TokenInterceptor.getLoginUser();
        if (orderDTO == null) {
            throw new ApiOrderNotFoundException("找不到订单[" + orderNumber + "]");
        } else {
            Integer userId = user.getId();
            if (!orderDTO.getUserAddress().getUserId().equals(userId)) {
                // 如果订单的userId和线程中的userId不匹配
                throw new ApiOrderAndUserNotMatchException("订单和用户不匹配，即该订单不属于用户");
            }
        }
        // 验证结束，开始微信支付逻辑
        String openId = user.getOpenId();
        try {
            // 微信支付信息封装
            WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
            orderRequest.setBody("衣仨邦-服饰");
            orderRequest.setOutTradeNo(orderNumber);
            double totalPrice = orderDTO.getTotalPrice();
            orderRequest.setTotalFee(BaseWxPayRequest.yuanToFen(String.valueOf(totalPrice)));
            orderRequest.setOpenid(openId);
            orderRequest.setSpbillCreateIp(spbillCreateIp);
            orderRequest.setTimeStart("yyyyMMddHHmmss");
            orderRequest.setTimeExpire("yyyyMMddHHmmss");
            logger.info("调用微信支付接口，调用参数[{}]", orderRequest);
            Object orderResponse = wxPayService.createOrder(orderRequest);
            logger.info("调用微信支付接口，返回参数[{}]", orderResponse);
            return ResponseMessage.newSuccessInstance(orderResponse, "返回支付参数");
        } catch (WxPayException e) {
            logger.error("微信支付失败！订单号：{},原因:{}", orderNumber, e.getMessage());
            throw new ApiException(e);
        }

    }


    /**
     * 支付回调通知处理
     * @param xmlData 相关支付结果及用户信息(微信端提供)
     * @return 向微信服务端返回应答
     * status 订单状态 0--待付款 1--待发货 2--待收货 3--已完成 4--申请退款(待商家处理) 5--退款中(待商家收货) 6--退款成功 7--退款失败 8--交易关闭 9--所有订单
     * 该链接是通过【统一下单API】中提交的参数notify_url设置，如果链接无法访问，商户将无法接收到微信通知。
     * 通知url必须为直接可访问的url，不能携带参数。示例：notify_url：“https://pay.weixin.qq.com/wxpay/pay.action”
     * <p>
     * 支付完成后，微信会把相关支付结果和用户信息发送给商户，商户需要接收处理，并返回应答。
     * 对后台通知交互时，如果微信收到商户的应答不是成功或超时，微信认为通知失败，微信会通过一定的策略定期重新发起通知，尽可能提高通知的成功率，但微信不保证通知最终能成功。
     * （通知频率为15/15/30/180/1800/1800/1800/1800/3600，单位：秒）
     * 注意：同样的通知可能会多次发送给商户系统。商户系统必须能够正确处理重复的通知。
     * 推荐的做法是，当收到通知进行处理时，首先检查对应业务数据的状态，判断该通知是否已经处理过，如果没有处理过再进行处理，如果处理过直接返回结果成功。在对业务数据进行状态检查和处理之前，要采用数据锁进行并发控制，以避免函数重入造成的数据混乱。
     * 特别提醒：商户系统对于支付结果通知的内容一定要做签名验证,并校验返回的订单金额是否与商户侧的订单金额一致，防止数据泄漏导致出现“假通知”，造成资金损失。
     */
    @RequestLog(module = "订单", operationDesc = "支付回调通知处理")
    @PostMapping("/wxNotify")
    public String parseOrderNotifyResult(@RequestBody String xmlData) {

        try {
            final WxPayOrderNotifyResult notifyResult = this.wxPayService.parseOrderNotifyResult(xmlData);
            // 用户订单编号
            String orderNumber = notifyResult.getOutTradeNo();
            // 根据订单号获取订单基础信息
            Order order = orderService.selectOrderByOrderNumber(orderNumber);

            // 微信回调返回的订单金额
            String totalFee = BaseWxPayResult.fenToYuan(notifyResult.getTotalFee());
            Double totalPrice = Double.parseDouble(totalFee);

            // 验证签名
            String sign = notifyResult.getSign();
            boolean checkSignRes = SignUtils.checkSign(xmlData, "MD5", sign);
            if (!checkSignRes) { // 如果签名验证失败
                logger.warn("微信支付回调——签名验证有误");
                return WxPayNotifyResponse.fail("签名验证有误");
            }

            // 校验返回的订单金额是否与商户侧的订单金额一致
            if (!order.getTotalPrice().equals(totalPrice)) { // 返回的订单金额与商户侧的订单金额不一致
                logger.warn("微信支付回调——返回的订单金额与商户侧的订单金额不一致");
                return WxPayNotifyResponse.fail("返回的订单金额与商户侧的订单金额不一致");
            }
            // 如果交易成功(支付成功)
            if (notifyResult.getResultCode().equals("SUCCESS")) {
                if (order.getStatus() == 0) { // 判断订单状态，避免微信重复通知调用该接口，如果该订单的状态是未付款
                    int res = orderService.updateOrderStatus(order.getOrderNumber());// 更新该订单为待发货
                    logger.info("更新订单[{}]状态：[未付款]-->[待发货]", orderNumber);
                    if (res == 0) { // 如果更新订单状态失败
                        logger.warn("更新订单[{}]状态：[未付款]-->[待发货]出现异常", orderNumber);
                        // 更新操作出现异常，向微信返回错误的xml代码，用微信的重试机制来二次回调
                        return WxPayNotifyResponse.fail("更新订单状态失败");
                    }
                }
            }
            // 无论交易成功与否都返回处理成功
            return WxPayNotifyResponse.success("处理成功！");
        } catch (WxPayException e) {
            logger.error("微信回调结果异常,异常原因{}", e.getMessage());
            return WxPayNotifyResponse.fail(e.getMessage());
        }
    }

    /**
     * 列出用户相关状态所有订单
     * status 订单状态 0--待付款 1--待发货 2--待收货 3--已完成 4--交易关闭 5--所有订单
     *
     * @param status：订单状态标识
     * @return
     */
    @RequestLog(module = "订单", operationDesc = "获取用户相关状态的所有订单")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "校验token", name = "accessToken", paramType = "header", required = true)
    })
    @ApiOperation(value = "获取用户相关状态的所有订单",
            notes = "订单状态定义：status 0--待付款 1--待发货 2--待收货 3--已完成 4--交易关闭 5--所有订单;\n" +
                    "退款状态定义：status 0--等待商家处理  1--退款中（待买家发货） 2--退款中（待商家收货） 3--退款成功 4--退款失败")
    @GetMapping("/getUserOrderList/status/{status}")
    public ResponseMessage<List<OrderDTO>> getUserOrderList(@Max(value = 5, message = "订单标识最大值为5")
                                                            @Min(value = 0, message = "订单标识最小值为0")
                                                            @ApiParam(name = "status", value = "订单状态标识", required = true) @PathVariable Integer status) {
        List<OrderDTO> orderDTOList = orderService.getUserOrderList(status);
        if (orderDTOList != null && orderDTOList.size() != 0) {
            return ResponseMessage.newSuccessInstance(orderDTOList, "获取订单列表成功");
        } else {
            return ResponseMessage.newSuccessInstance("暂无该类型订单");
        }
    }

    /**
     * @param status
     */
    @RequestLog(module = "订单", operationDesc = "获取商城中相关状态的所有订单")
    @ApiOperation(value = "获取商城中相关状态的所有订单",
            notes = "订单状态定义：status 0--待付款 1--待发货 2--待收货 3--已完成 4--交易关闭 5--所有订单;\n" +
                    "退款状态定义：status 0--等待商家处理  1--退款中（待买家发货） 2--退款中（待商家收货） 3--退款成功 4--退款失败")
    @GetMapping("/getOrderList/status/{status}")
    public ResponseMessage<List<OrderDTO>> getOrderList(@Max(value = 5, message = "订单标识最大值为5")
                                                        @Min(value = 0, message = "订单标识最小值为0")
                                                        @ApiParam(name = "status", value = "订单状态标识", required = true) @PathVariable Integer status) {
        List<OrderDTO> orderDTOList = orderService.getOrderList(status);
        if (orderDTOList != null && orderDTOList.size() != 0) {
            return ResponseMessage.newSuccessInstance(orderDTOList, "获取订单列表成功");
        } else {
            return ResponseMessage.newSuccessInstance("暂无该类型订单");
        }
    }

    /**
     * 订单状态自增修改，适用于待付款、待发货、待收货类订单
     * 订单状态定义：status 0--待付款 1--待发货 2--待收货 3--已完成 4--交易关闭 5--所有订单
     * 0--待付款 1--待发货 2--待收货 状态订单 更新订单状态
     *
     * @param orderNumber:订单编号
     * @return
     */
    @RequestLog(module = "订单", operationDesc = "确认收货")
    @ApiOperation(value = "确认收货", notes = "订单状态定义：status 0--待付款 1--待发货 2--待收货 3--已完成 4--交易关闭 5--所有订单\n"
            + "修改订单状态为下一状态，适用于'确认收货'按钮")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "校验token", name = "accessToken", paramType = "header", required = true)
    })
    @PutMapping("updateOrderStatus/{orderNumber}")
    public ResponseMessage updateOrderStatus(@ApiParam(name = "orderNumber", value = "订单编号", required = true)
                                             @NotBlank(message = "订单编号为空") @PathVariable String orderNumber) {
        try {
            orderService.updateOrderStatus(orderNumber);
        } catch (OrderNotFoundException e) {
            throw new ApiOrderNotFoundException(e.getMessage() + "订单编号：" + orderNumber);
        } catch (OrderStatusNotFitException e) {
            throw new ApiOrderStatusNotFitException(e.getMessage());
        } catch (OrderAndUserNotMatchException e) {
            throw new ApiOrderAndUserNotMatchException(e.getMessage());
        } catch (Exception e) {
            throw new ApiException(e);
        }
        return ResponseMessage.newSuccessInstance("更新订单状态成功");
    }

    /**
     * 设置订单状态 —— 适用于已完成、交易关闭订单（该接口貌似没用emm）
     * 订单状态定义：status 订单状态 0--待付款 1--待发货 2--待收货 3--已完成 4--交易关闭 5--所有订单
     * 非 0--待付款 1--待发货 2--待收货 状态订单 设置订单状态
     *
     * @param orderMap —— orderId、status
     * @return
     */
    @PutMapping("/setOrderStatus")
    public ResponseMessage setOrderStatus(@RequestBody Map<String, Integer> orderMap) {
        Integer status = orderMap.get("status");
        if (status < 3 || status > 5) {  // 如果订单状态超出范围
            throw new ApiOrderStatusNotFitException("订单状态超出该接口的适配范围");
        }
        try {
            orderService.setOrderStatus(orderMap);
        } catch (OrderNotFoundException e) {
            throw new ApiOrderNotFoundException(e.getMessage());
        } catch (OrderStatusNotFitException e) {
            throw new ApiOrderStatusNotFitException(e.getMessage());
        } catch (OrderAndUserNotMatchException e) {
            throw new ApiOrderAndUserNotMatchException(e.getMessage());
        } catch (Exception e) {
            throw new ApiException(e);
        }
        return ResponseMessage.newSuccessInstance("更新订单状态成功");
    }

    /**
     * 删除订单，暂时无用，用户没有删除订单按钮emm
     * @param orderId：订单id
     * @return
     */
    @RequestLog(module = "订单", operationDesc = "删除订单")
    @ApiOperation(value = "删除订单", notes = "暂时无用，用户没有删除订单按钮emm")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "校验token", name = "accessToken", paramType = "header", required = true)
    })
    @DeleteMapping("/delete/{orderId}")
    public ResponseMessage delete(@ApiParam(name = "orderId", value = "订单id", required = true)
                                  @NotNull(message = "订单id为空") @PathVariable Integer orderId) {
        try {
            orderService.deleteOrder(orderId);
        } catch (OrderNotFoundException e) {
            throw new ApiOrderNotFoundException(e.getMessage());
        } catch (OrderAndUserNotMatchException e) {
            throw new ApiOrderAndUserNotMatchException(e.getMessage());
        } catch (OrderStatusNotFitException e) {
            throw new ApiOrderStatusNotFitException(e.getMessage());
        } catch (Exception e) {
            throw new ApiException(e);
        }
        return ResponseMessage.newSuccessInstance("删除订单成功");
    }

    /**
     * 根据订单编号设置快递编号
     * @param deliverGoodsDTO:orderNumber and courierNumber
     * @return
     */
    @RequestLog(module = "订单", operationDesc = "商家发货")
    @ApiOperation(value = "商家发货", notes = "商家发货设置快递编号，需要订单编号")
    @PutMapping("/setCourierNumber")
    public ResponseMessage setCourierNumber(@RequestBody @Validated DeliverGoodsDTO deliverGoodsDTO) {
        Order order = new Order();
        BeanUtils.copyProperties(deliverGoodsDTO, order);
        try {
            orderService.setCourierNumber(order);
        } catch (OrderNotFoundException e) {
            throw new ApiOrderNotFoundException(e.getMessage());
        } catch (OrderStatusNotFitException e) {
            throw new ApiOrderStatusNotFitException(e.getMessage());
        } catch (Exception e) {
            throw new ApiException(e);
        }
        return ResponseMessage.newSuccessInstance("设置快递单号成功");
    }

    /**
     * 在未发货之前用户修改地址接口
     *
     * @param updateUserAddressVo:用户地址UserAddress、订单编号orderNumber
     * @return
     */
    @RequestLog(module = "订单", operationDesc = "修改收货地址")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "校验token", name = "accessToken", paramType = "header", required = true)
    })
    @ApiOperation(value = "修改收货地址", notes = "待付款、待发货状态适用")
    @PutMapping("/updateAddress")
    public ResponseMessage updateAddress(@RequestBody @Validated UpdateUserAddressVo updateUserAddressVo) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserAddress(updateUserAddressVo.getUserAddress());
        orderDTO.setOrderNumber(updateUserAddressVo.getOrderNumber());
        try {
            orderService.updateAddress(orderDTO);
        } catch (OrderNotFoundException e) {
            throw new ApiOrderNotFoundException(e.getMessage());
        } catch (OrderStatusNotFitException e) {
            throw new ApiOrderStatusNotFitException(e.getMessage());
        } catch (OrderAndUserNotMatchException e) {
            throw new ApiOrderAndUserNotMatchException(e.getMessage());
        } catch (Exception e) {
            throw new ApiException(e);
        }
        return ResponseMessage.newSuccessInstance("修改订单收货地址成功");
    }

    /**
     * 取消订单接口
     * @param orderNumber：订单编号
     * @return
     */
    @RequestLog(module = "订单", operationDesc = "取消订单")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "校验token", name = "accessToken", paramType = "header", required = true)
    })
    @ApiOperation(value = "取消订单", notes = "在未支付的情况下用户可以取消订单")
    @PutMapping("/cancelOrder/{orderId}")
    public ResponseMessage cancelOrder(@ApiParam(name = "orderNumber", value = "订单编号", required = true)
                                       @NotBlank(message = "订单编号为空") @PathVariable String orderNumber) {
        try {
            orderService.cancelOrder(orderNumber);
        } catch (OrderNotFoundException e) {
            throw new ApiOrderNotFoundException(e.getMessage());
        } catch (OrderStatusNotFitException e) {
            throw new ApiOrderStatusNotFitException(e.getMessage());
        } catch (OrderAndUserNotMatchException e) {
            throw new ApiOrderAndUserNotMatchException(e.getMessage());
        } catch (Exception e) {
            throw new ApiException(e);
        }
        return ResponseMessage.newSuccessInstance("取消订单成功");
    }
}
