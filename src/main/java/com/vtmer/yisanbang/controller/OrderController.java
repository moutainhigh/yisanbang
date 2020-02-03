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
import com.vtmer.yisanbang.domain.Order;
import com.vtmer.yisanbang.service.OrderService;
import com.vtmer.yisanbang.service.UserService;
import com.vtmer.yisanbang.vo.OrderVo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private WxPayService wxPayService;

    @Autowired
    private UserService userService;

    /**
     * 点击去结算，显示确认订单页面
     * @param userId：用户id
     * @return
     */
    @GetMapping("confirmOrder/{userId}")
    public ResponseMessage confirmOrder(@PathVariable("userId") Integer userId) {
        OrderVo orderVo = orderService.confirmCartOrder(userId);
        return ResponseMessage.newSuccessInstance(orderVo,"获取确认订单相关信息成功");
    }


    /**
     * 创建订单
     * @param orderVo：订单详情信息
     * @return
     */
    @PostMapping("/insert")
    public ResponseMessage insert(@RequestBody OrderVo orderVo) {
        if (orderVo.getUserAddress() == null) {
            return ResponseMessage.newErrorInstance("用户收货地址传入失败");
        } else if (orderVo.getOrderGoodsList().getCartGoodsList() == null && orderVo.getOrderGoodsList().getCartGoodsList().size()==0) {
            return ResponseMessage.newErrorInstance("用户订单商品信息传入失败");
        } else {
            Map<String, String> orderMap = orderService.createCartOrder(orderVo);
            if (orderMap != null) {
                return ResponseMessage.newSuccessInstance(orderMap,"创建订单成功，返回订单编号和openId");
            } else {
                return ResponseMessage.newErrorInstance("该用户还未通过微信登录，无openId");
            }
        }
    }

    /**
     * 通过订单号进行微信支付
     * @param orderNumber：订单号
     * @return 返回前端调起微信支付所需的支付参数（5个参数和sign）
     */
    @PostMapping("/wxpay")
    public ResponseMessage pay(@RequestBody String orderNumber) {
        if (orderNumber == null) {
            return ResponseMessage.newErrorInstance("订单号传入错误");
        } else { // 订单编号不为null
            OrderVo orderVo = orderService.selectOrderVoByOrderNumber(orderNumber);
            if (orderVo == null) {
                return ResponseMessage.newErrorInstance("订单号有误，无此订单号相关信息");
            } else {
                Integer userId = orderVo.getUserAddress().getUserId();
                String openId = userService.getOpenIdByUserId(userId);
                try {
                    WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
                    orderRequest.setBody("衣仨邦-服饰");
                    orderRequest.setOutTradeNo(orderNumber);
                    double totalPrice = orderVo.getOrderGoodsList().getTotalPrice();
                    orderRequest.setTotalFee(BaseWxPayRequest.yuanToFen(String.valueOf(totalPrice)));
                    orderRequest.setOpenid(openId);
                    orderRequest.setSpbillCreateIp("127.0.0.1");
                    orderRequest.setTimeStart("yyyyMMddHHmmss");
                    orderRequest.setTimeExpire("yyyyMMddHHmmss");

                    return ResponseMessage.newSuccessInstance(wxPayService.createOrder(orderRequest),"返回支付参数");
                } catch (WxPayException e) {
                    logger.error("微信支付失败！订单号：{},原因:{}", orderNumber, e.getMessage());
                    e.printStackTrace();
                    return ResponseMessage.newErrorInstance("支付失败，请稍后重试！");
                }
            }
        }
    }


    /**
     *
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
    @PostMapping("/wxNotify")
    public String parseOrderNotifyResult(@RequestBody String xmlData) {

        try {
            final WxPayOrderNotifyResult notifyResult = this.wxPayService.parseOrderNotifyResult(xmlData);
            // 用户订单编号
            String orderNumber = notifyResult.getOutTradeNo();
            // 根据订单号获取订单基础信息
            Order order = orderService.selectOrderByOrderNumber(orderNumber);
            String totalFee = BaseWxPayResult.fenToYuan(notifyResult.getTotalFee());
            Double totalPrice = Double.parseDouble(totalFee);

            // 验证签名
            String sign = notifyResult.getSign();
            boolean checkSignRes = SignUtils.checkSign(xmlData, "MD5", sign);
            if(!checkSignRes) { // 如果签名验证失败
                return WxPayNotifyResponse.fail("签名验证有误");
            }

            // 校验返回的订单金额是否与商户侧的订单金额一致
            if (!order.getTotalPrice().equals(totalPrice)) { // 返回的订单金额与商户侧的订单金额不一致
                return WxPayNotifyResponse.fail("返回的订单金额与商户侧的订单金额不一致");
            }
            // 如果交易成功(支付成功)
            if (notifyResult.getResultCode().equals("SUCCESS")) {
                if (order.getStatus() == 0) { // 判断订单状态，避免微信重复通知调用该接口，如果该订单的状态是未付款
                    int res = orderService.updateOrderStatus(order.getId());// 更新该订单为待发货
                    if (res == 0) { // 如果更新订单状态失败
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
     * status 订单状态 0--待付款 1--待发货 2--待收货 3--已完成 4--申请退款 5--交易关闭 6--所有订单
     * 列出用户相关状态所有订单
     * @param orderMap —— userId、status
     *                 userId传null可以列出商城相关状态的所有订单
     * @return
     */
    @GetMapping("/get")
    public ResponseMessage listOrder(@RequestBody Map<String,Integer> orderMap) {
        System.out.println(orderMap.get("userId"));
        if (orderMap.get("status")>6 || orderMap.get("status")<0) {
            return ResponseMessage.newErrorInstance("订单状态参数有误");
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
     * status 订单状态 0--待付款 1--待发货 2--待收货 3--已完成 4--申请退款 5--交易关闭 6--所有订单
     * 0--待付款 1--待发货 2--待收货 状态订单 更新订单状态
     * @param orderIdMap —— orderId
     * @return
     */
    @PutMapping("updateOrderStatus")
    public ResponseMessage updateOrderStatus(@RequestBody Map<String,Integer> orderIdMap) {
        int res = orderService.updateOrderStatus(orderIdMap.get("orderId"));
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
     * status 订单状态 0--待付款 1--待发货 2--待收货 3--已完成 4--申请退款 5--交易关闭 6--所有订单
     * 非 0--待付款 1--待发货 2--待收货 状态订单 设置订单状态
     * @param orderMap —— orderId、status
     * @return
     */
    @PutMapping("/setOrderStatus")
    public ResponseMessage setOrderStatus(@RequestBody Map<String,Integer> orderMap) {
        Integer status = orderMap.get("status");
        if (status<3 || status>6) {  // 如果订单状态超出范围
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
     * 删除订单
     * @param orderId：订单id
     * @return
     */
    @DeleteMapping("/delete")
    public ResponseMessage delete(@RequestBody Integer orderId) {
        int res = orderService.deleteOrder(orderId);
        if (res == 1) {
            return ResponseMessage.newSuccessInstance("删除订单成功");
        } else if (res == -1) {
            return ResponseMessage.newErrorInstance("订单id有误");
        } else {
            return ResponseMessage.newErrorInstance("删除订单失败");
        }
    }

}
