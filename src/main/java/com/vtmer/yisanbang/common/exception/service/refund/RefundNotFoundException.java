package com.vtmer.yisanbang.common.exception.service.refund;

public class RefundNotFoundException extends RuntimeException {
    public RefundNotFoundException() {
        super("找不到该退款单");
    }
    public RefundNotFoundException(String message) {
        super(message);
    }
}
