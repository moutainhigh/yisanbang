package com.vtmer.yisanbang.controller;

import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.service.OrderService;
import com.vtmer.yisanbang.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;


    /**
     * 点击去结算，显示确认订单页面
     * @param userId
     * @return
     */
    @GetMapping("confirmOrder/{userId}")
    public ResponseMessage confirmOrder(@PathVariable("userId") Integer userId) {
        OrderVo orderVo = orderService.confirmCartOrder(userId);
        return ResponseMessage.newSuccessInstance(orderVo,"获取确认订单相关信息成功");
    }


    /**
     * 创建订单
     * @param orderVo
     * @return
     */
    @PostMapping("/insert")
    public ResponseMessage insert(@RequestBody OrderVo orderVo) {
        if (orderVo.getUserAddress() == null) {
            return ResponseMessage.newErrorInstance("用户收货地址传入失败");
        } else if (orderVo.getOrderGoodsList().getCartGoodsList() == null && orderVo.getOrderGoodsList().getCartGoodsList().size()==0) {
            return ResponseMessage.newErrorInstance("用户订单商品信息传入失败");
        } else {
            String orderNumber = orderService.createCartOrder(orderVo);
            HashMap<String, String> map = new HashMap<>();
            map.put("orderNumber",orderNumber);
            return ResponseMessage.newSuccessInstance(map,"创建订单成功，返回订单编号");
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
        } else if (res == 0) {
            return ResponseMessage.newErrorInstance("该订单状态不适合该接口");
        } else if (res == 1) {
            return ResponseMessage.newSuccessInstance("更新订单状态成功");
        }
        return ResponseMessage.newErrorInstance("异常错误");
    }

    /**
     * status 订单状态 0--待付款 1--待发货 2--待收货 3--已完成 4--申请退款 5--交易关闭 6--所有订单
     * 非 0--待付款 1--待发货 2--待收货 状态订单 设置订单状态
     * @param orderMap
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
     * @param orderIdMap
     * @return
     */
    @DeleteMapping("/delete")
    public ResponseMessage delete(@RequestBody Map<String,Integer> orderIdMap) {
        Integer orderId = orderIdMap.get("orderId");
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
