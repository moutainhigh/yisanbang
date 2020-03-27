package com.vtmer.yisanbang.common.exception.service.order;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException() {
        super("找不到该订单");
    }
    public OrderNotFoundException(String message) {
        super(message);
    }
}
