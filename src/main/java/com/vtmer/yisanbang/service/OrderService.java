package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.vo.ConfirmOrderVo;

public interface OrderService {

    ConfirmOrderVo confirmOrder(Integer userId);
}
