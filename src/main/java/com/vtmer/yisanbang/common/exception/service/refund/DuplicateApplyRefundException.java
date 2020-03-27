package com.vtmer.yisanbang.common.exception.service.refund;

public class DuplicateApplyRefundException extends RuntimeException {
    public DuplicateApplyRefundException() {
        super("重复申请退款");
    }

    public DuplicateApplyRefundException(String message) {
        super(message);
    }
}
