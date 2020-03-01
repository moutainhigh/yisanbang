package com.vtmer.yisanbang.common.exception.service.order;

public class OrderAlreadyRemindException extends RuntimeException {
    public OrderAlreadyRemindException() {
        super("该订单已提交发货");
    }

    public OrderAlreadyRemindException(String message) {
        super(message);
    }
}
