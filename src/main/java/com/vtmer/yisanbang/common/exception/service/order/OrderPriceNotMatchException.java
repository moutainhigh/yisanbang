package com.vtmer.yisanbang.common.exception.service.order;

public class OrderPriceNotMatchException extends RuntimeException {
    public OrderPriceNotMatchException() {
        super("前后端的订单总价不一致");
    }
    public OrderPriceNotMatchException(String message) {
        super(message);
    }
}
