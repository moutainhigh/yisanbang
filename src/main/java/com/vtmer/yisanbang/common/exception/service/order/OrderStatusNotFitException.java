package com.vtmer.yisanbang.common.exception.service.order;

public class OrderStatusNotFitException extends RuntimeException {
    public OrderStatusNotFitException() {
        super("该订单状态不适合该接口");
    }

    public OrderStatusNotFitException(String message) {
        super(message);
    }
}
