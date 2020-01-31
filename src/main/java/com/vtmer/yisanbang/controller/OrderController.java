package com.vtmer.yisanbang.controller;

import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.service.CartService;
import com.vtmer.yisanbang.service.OrderService;
import com.vtmer.yisanbang.vo.ConfirmOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;


    // 点击去结算，显示确认订单页面
    @GetMapping("confirmOrder/{userId}")
    public ResponseMessage confirmOrder(@PathVariable("userId") Integer userId) {
        ConfirmOrderVo confirmOrderVo = orderService.confirmOrder(userId);
        return ResponseMessage.newSuccessInstance(confirmOrderVo,"获取确认订单相关信息成功");
    }
}
