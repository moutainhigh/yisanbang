package com.vtmer.yisanbang.controller;

import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.service.OrderService;
import com.vtmer.yisanbang.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;


    // 点击去结算，显示确认订单页面
    @GetMapping("confirmOrder/{userId}")
    public ResponseMessage confirmOrder(@PathVariable("userId") Integer userId) {
        OrderVo orderVo = orderService.confirmOrder(userId);
        return ResponseMessage.newSuccessInstance(orderVo,"获取确认订单相关信息成功");
    }


    @PostMapping("/insert")
    public ResponseMessage insert(@RequestBody OrderVo orderVo) {
        return null;
    }



}
