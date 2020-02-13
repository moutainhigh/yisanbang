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
import com.vtmer.yisanbang.common.validGroup.Insert;
import com.vtmer.yisanbang.domain.Order;
import com.vtmer.yisanbang.service.OrderService;
import com.vtmer.yisanbang.service.UserService;
import com.vtmer.yisanbang.vo.OrderVo;
import com.vtmer.yisanbang.vo.UpdateUserAddressVo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "订单接口")
@RestController
@RequestMapping("/order")
public class OrderController {

    private final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private WxPayService wxPayService;

    @Autowired
    private UserService userService;

    @Value("${wx.pay.spbillCreateIp}")
    private String spbillCreateIp;

    /**
     * 点击去结算，显示确认订单页面
     * @return
     */
    @RequestLog(module = "订单",operationDesc = "确认订单")
    @ApiOperation(value = "确认订单",notes = "点击去结算，显示确认订单页面")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "校验token",name = "accessToken",paramType = "header",required = true)
    })
    @GetMapping("confirmOrder")
    public ResponseMessage<OrderVo> confirmOrder() {
        OrderVo orderVo = orderService.confirmCartOrder();
        if (orderVo == null) {
            return ResponseMessage.newErrorInstance("购物车勾选商品为空");
        }
        return ResponseMessage.newSuccessInstance(orderVo,"获取确认订单相关信息成功");
    }


    /**
     * 创建订单
     * @param orderVo：订单详情信息
     * @return
     */
    @RequestLog(module = "订单",operationDesc = "创建订单")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "校验token",name = "accessToken",paramType = "header",required = true)
    })
    @ApiOperation(value = "创建订单",notes = "用户从购物车页面确认订单，点击提交订单后调用,若是订单中的某件商品数量超过库存，会返回【库存不足】的提示")
    @PostMapping("/insert")
    public ResponseMessage insert(@RequestBody @Validated({Insert.class}) OrderVo orderVo) {
        Map<String, String> orderMap = orderService.createCartOrder(orderVo);
        return ResponseMessage.newSuccessInstance(orderMap,"创建订单成功，返回订单编号和openId");

    }

    /**
     * 通过订单号进行微信支付
     * @param orderNumber：orderNumber订单号
     * @return 返回前端调起微信支付所需的支付参数（5个参数和sign）
     */
    @RequestLog(module = "订单",operationDesc = "微信支付")
    @ApiOperation(value = "微信支付",notes = "通过订单号进行微信支付，返回前端调起微信支付所需的支付参数（5个参数和sign）")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "校验token",name = "accessToken",paramType = "header",required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200,message = "ResponseMessage => \n 'data:" +
                    "{\n'appId':'wxd678efh567hg6787'" +
                    "\n'timeStamp':'1490840662'" +
                    "\n'nonceStr':'5K8264ILTKCH16CQ2502SI8ZNMTM67VS'" +
                    "\n'packageValue':'prepay_id=wx2017033010242291fcfe0db70013231072'" +
                    "\n'signType':'MD5'" +
                    "\n'paySign':' MD5(appId=wxd678efh567hg6787&nonceStr=5K8264ILTKCH16CQ2502SI8ZNMTM67VS&package=prepay_id=wx2017033010242291fcfe0db70013231072&signType=MD5&timeStamp=1490840662&key=qazwsxedcrfvtgbyhnujmikolp111111) = 22D9B4E54AB1950F51E0649E8810ACD6'" +
                    "\n}")
    })
    @GetMapping("/wxpay/{orderNumber}")
    public ResponseMessage wxpay(@ApiParam(name = "orderNumber",value = "订单编号",required = true)
                                     @PathVariable String orderNumber) {
        if (orderNumber == null) {
            return ResponseMessage.newErrorInstance("订单号传入错误");
        } else { // 订单编号不为null
            OrderVo orderVo = orderService.selectOrderVoByOrderNumber(orderNumber);
            if (orderVo == null) {
                return ResponseMessage.newErrorInstance("订单号有误，无此订单号相关信息");
            } else {
                Integer userId = TokenInterceptor.getLoginUser().getId();
                String openId = userService.getOpenIdByUserId(userId);
                try {
                    WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
                    orderRequest.setBody("衣仨邦-服饰");
                    orderRequest.setOutTradeNo(orderNumber);
                    double totalPrice = orderVo.getOrderGoodsList().getTotalPrice();
                    orderRequest.setTotalFee(BaseWxPayRequest.yuanToFen(String.valueOf(totalPrice)));
                    orderRequest.setOpenid(openId);
                    orderRequest.setSpbillCreateIp(spbillCreateIp);
                    orderRequest.setTimeStart("yyyyMMddHHmmss");
                    orderRequest.setTimeExpire("yyyyMMddHHmmss");
                    Object orderResponse = wxPayService.createOrder(orderRequest);
                    logger.info("调用微信支付接口，返回参数[{}]",orderResponse);
                    return ResponseMessage.newSuccessInstance(orderResponse,"返回支付参数");
                } catch (WxPayException e) {
                    logger.error("微信支付失败！订单号：{},原因:{}", orderNumber, e.getMessage());
                    e.printStackTrace();
                    return ResponseMessage.newErrorInstance("支付失败，请稍后重试！");
                }
            }
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
    @RequestLog(module = "订单",operationDesc = "支付回调通知处理")
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
            if(!checkSignRes) { // 如果签名验证失败
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
                    logger.info("更新订单[{}]状态：[未付款]-->[待发货]",orderNumber);
                    if (res == 0) { // 如果更新订单状态失败
                        logger.warn("更新订单[{}]状态：[未付款]-->[待发货]出现异常",orderNumber);
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
     * @param flag、status
     *                 flag传0可以列出商城相关状态的所有订单
     * @return
     */
    @RequestLog(module = "订单",operationDesc = "获取用户相关状态的所有订单")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "校验token",name = "accessToken",paramType = "header",required = true)
    })
    @ApiOperation(value = "获取用户相关状态的所有订单",
            notes = "订单状态定义：status 0--待付款 1--待发货 2--待收货 3--已完成 4--交易关闭 5--所有订单;\n" +
                    "退款状态定义：status 0--等待商家处理  1--退款中（待买家发货） 2--退款中（待商家收货） 3--退款成功 4--退款失败\n" +
            "flag传0获取商城相关状态的所有订单,flag传1获取用户相关状态的所有订单")
    @GetMapping("/get/status/{status}/flag/{flag}")
    public ResponseMessage<List<OrderVo>> listOrder(@ApiParam(name = "status",value = "订单状态标识",required = true) @PathVariable Integer status,
                                     @ApiParam(name = "flag",value = "查询标识",required = true) @PathVariable Integer flag) {
        Map<String,Integer> orderMap = new HashMap<>();
        orderMap.put("status",status);
        orderMap.put("flag",flag);
        if (!(status<=5 && status>=0)) {
            return ResponseMessage.newErrorInstance("订单状态参数超出定义范围");
        } else {
            List<OrderVo> orderList = orderService.listOrder(orderMap);
            if (orderList!=null && orderList.size()!=0) {
                return ResponseMessage.newSuccessInstance(orderList,"获取订单列表成功");
            } else {
                return ResponseMessage.newSuccessInstance("无该类型订单");
            }
        }
    }

    /**
     * 订单状态自增修改，适用于待付款、待发货、待收货类订单
     * 订单状态定义：status 0--待付款 1--待发货 2--待收货 3--已完成 4--交易关闭 5--所有订单
     * 0--待付款 1--待发货 2--待收货 状态订单 更新订单状态
     * @param orderNumber:订单编号
     * @return
     */
    @RequestLog(module = "订单",operationDesc = "确认收货")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "校验token",name = "accessToken",paramType = "header",required = true)
    })
    @ApiOperation(value = "确认收货",notes = "订单状态定义：status 0--待付款 1--待发货 2--待收货 3--已完成 4--交易关闭 5--所有订单\n"
            +"修改订单状态为下一状态，适用于'确认收货'按钮")

    @PutMapping("updateOrderStatus/{orderNumber}")
    public ResponseMessage updateOrderStatus(@ApiParam(name = "orderNumber",value = "订单编号",required = true)
                                                 @PathVariable String orderNumber) {
        int res = orderService.updateOrderStatus(orderNumber);
        if (res == -1) {
            return ResponseMessage.newErrorInstance("订单id有误");
        } else if (res == -2) {
            return ResponseMessage.newErrorInstance("该订单状态不适合该接口");
        } else if (res == 0) {
            return ResponseMessage.newErrorInstance("更新订单状态失败");
        } else if (res == 1) {
            return ResponseMessage.newSuccessInstance("更新订单状态成功");
        }
        return ResponseMessage.newErrorInstance("异常错误");
    }

    /**
     * 设置订单状态 —— 适用于已完成、交易关闭订单（该接口貌似没用emm）
     * 订单状态定义：status 订单状态 0--待付款 1--待发货 2--待收货 3--已完成 4--交易关闭 5--所有订单
     * 非 0--待付款 1--待发货 2--待收货 状态订单 设置订单状态
     * @param orderMap —— orderId、status
     * @return
     */
    @PutMapping("/setOrderStatus")
    public ResponseMessage setOrderStatus(@RequestBody Map<String,Integer> orderMap) {
        Integer status = orderMap.get("status");
        if (status<3 || status>5) {  // 如果订单状态超出范围
            return ResponseMessage.newErrorInstance("订单状态status值超出范围");
        } else {
            int res = orderService.setOrderStatus(orderMap);
            if (res == 1) {
                return ResponseMessage.newSuccessInstance("更新订单状态成功");
            } else if (res == -2) {
                return ResponseMessage.newErrorInstance("订单id有误");
            } else if (res == -1) {
                return ResponseMessage.newErrorInstance("该订单目前已经是该状态");
            } else {
                return ResponseMessage.newErrorInstance("更新订单状态出错");
            }
        }
    }

    /**
     * 删除订单，暂时无用，用户没有删除订单按钮emm
     * @param orderId：订单id
     * @return
     */
    @RequestLog(module = "订单",operationDesc = "删除订单")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "校验token",name = "accessToken",paramType = "header",required = true)
    })
    @ApiOperation(value = "删除订单",notes = "暂时无用，用户没有删除订单按钮emm")
    @DeleteMapping("/delete/{orderId}")
    public ResponseMessage delete(@ApiParam(name = "orderId",value = "订单id",required = true)
                                      @PathVariable Integer orderId) {
        int res = orderService.deleteOrder(orderId);
        if (res == 1) {
            return ResponseMessage.newSuccessInstance("删除订单成功");
        } else if (res == -1) {
            return ResponseMessage.newErrorInstance("订单id有误");
        } else {
            return ResponseMessage.newErrorInstance("删除订单失败");
        }
    }

    /**
     * 根据订单id或订单编号设置快递编号（订单编号优先）
     * @param order:orderId or orderNumber and courierNumber
     * @return
     */
    @RequestLog(module = "订单",operationDesc = "商家发货")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "校验token",name = "accessToken",paramType = "header",required = true)
    })
    @ApiOperation(value = "商家发货",notes = "商家发货设置快递编号，需要订单id或订单编号")
    @PutMapping("/setCourierNumber")
    public ResponseMessage setCourierNumber(@RequestBody @NotNull(message = "传入参数为空") Order order) {
        if (order.getId()==null && order.getOrderNumber() == null) {
            return ResponseMessage.newErrorInstance("orderId和orderNumber都为空");
        } else if (order.getCourierNumber() == null) {
            return ResponseMessage.newErrorInstance("快递编号courierNumber为空");
        }
        int res = orderService.setCourierNumber(order);
        if (res == 1) {
            return ResponseMessage.newSuccessInstance("设置快递单号成功");
        } else {
            return ResponseMessage.newErrorInstance("设置快递单号失败");
        }
    }

    /**
     * 在未发货之前用户修改地址接口
     * @param updateUserAddressVo:用户地址UserAddress、订单编号orderNumber
     * @return
     */
    @RequestLog(module = "订单",operationDesc = "修改收货地址")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "校验token",name = "accessToken",paramType = "header",required = true)
    })
    @ApiOperation(value = "修改收货地址",notes = "待付款、待发货状态适用")
    @PutMapping("/updateAddress")
    public ResponseMessage updateAddress(@RequestBody @Validated UpdateUserAddressVo updateUserAddressVo) {
        OrderVo orderVo = new OrderVo();
        orderVo.setUserAddress(updateUserAddressVo.getUserAddress());
        orderVo.setOrderNumber(updateUserAddressVo.getOrderNumber());
        int res = orderService.updateAddress(orderVo);
        if (res == -1) {
            return ResponseMessage.newErrorInstance("订单编号不存在");
        } else if (res == -2) {
            return ResponseMessage.newErrorInstance("该订单状态不能修改收货地址");
        } else if (res == 1) {
            return ResponseMessage.newSuccessInstance("修改收货地址成功");
        } else {
            return ResponseMessage.newErrorInstance("修改收货地址失败");
        }
    }

    /**
     * 取消订单接口
     * @param orderId：订单id
     * @return
     */
    @RequestLog(module = "订单",operationDesc = "取消订单")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "校验token",name = "accessToken",paramType = "header",required = true)
    })
    @ApiOperation(value = "取消订单",notes = "在未支付的情况下用户可以取消订单")
    @PutMapping("/cancelOrder/{orderId}")
    public ResponseMessage cancelOrder(@ApiParam(name = "orderId",value = "订单id",required = true)
                                           @PathVariable Integer orderId) {
        int res = orderService.cancelOrder(orderId);
        if (res == -1) {
            return ResponseMessage.newErrorInstance("订单id不存在");
        } else if (res == -2) {
            return ResponseMessage.newErrorInstance("该订单状态不可取消订单");
        } else if (res == 1) {
            return ResponseMessage.newSuccessInstance("取消订单成功");
        } else {
            return ResponseMessage.newErrorInstance("取消订单失败");
        }
    }
}
