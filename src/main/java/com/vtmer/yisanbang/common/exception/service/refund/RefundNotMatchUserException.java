package com.vtmer.yisanbang.common.exception.service.refund;

public class RefundNotMatchUserException extends RuntimeException {
    public RefundNotMatchUserException() {
        super("该退款单不属于目前的用户");
    }

    public RefundNotMatchUserException(String message) {
        super(message);
    }
}
