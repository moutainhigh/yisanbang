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


    // 点击去结算，显示确认订单页面
    @GetMapping("confirmOrder/{userId}")
    public ResponseMessage confirmOrder(@PathVariable("userId") Integer userId) {
        OrderVo orderVo = orderService.confirmCartOrder(userId);
        return ResponseMessage.newSuccessInstance(orderVo,"获取确认订单相关信息成功");
    }


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

    @GetMapping("/get")
    public ResponseMessage listOrder(@RequestBody Map<String,Integer> orderMap) {
        System.out.println(orderMap);
        if (orderMap.get("status")>6 || orderMap.get("status")<0) {
            return ResponseMessage.newErrorInstance("订单状态参数有误");
        } else {
            List<OrderVo> orderList = orderService.listOrder(orderMap);
            if (orderList!=null && orderList.size()!=0) {
                return ResponseMessage.newSuccessInstance(orderList,"获取用户订单列表成功");
            } else {
                return ResponseMessage.newSuccessInstance("无该类型订单");
            }
        }
    }
}
