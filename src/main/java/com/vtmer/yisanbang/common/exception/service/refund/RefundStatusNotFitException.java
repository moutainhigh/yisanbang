package com.vtmer.yisanbang.common.exception.service.refund;

public class RefundStatusNotFitException extends RuntimeException{
    public RefundStatusNotFitException() {
        super("该退款单状态不适合该操作");
    }

    public RefundStatusNotFitException(String message) {
        super(message);
    }
}
