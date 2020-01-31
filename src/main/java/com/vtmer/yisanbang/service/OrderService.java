package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.vo.OrderVo;

public interface OrderService {

    OrderVo confirmOrder(Integer userId);
}
