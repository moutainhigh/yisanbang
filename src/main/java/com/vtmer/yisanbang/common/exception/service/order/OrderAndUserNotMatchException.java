package com.vtmer.yisanbang.common.exception.service.order;

public class OrderAndUserNotMatchException extends RuntimeException {
    public OrderAndUserNotMatchException() {
        super("订单和用户不匹配");
    }

    public OrderAndUserNotMatchException(String message) {
        super(message);
    }
}
