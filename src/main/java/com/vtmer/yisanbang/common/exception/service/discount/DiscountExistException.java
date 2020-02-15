package com.vtmer.yisanbang.common.exception.service.discount;

public class DiscountExistException extends RuntimeException {

    public DiscountExistException() {
        super("优惠规则已设置");
    }

    public DiscountExistException(String message) {
        super("message");
    }
}
