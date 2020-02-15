package com.vtmer.yisanbang.common.exception.service.discount;

public class DiscountNotFoundException extends RuntimeException {
    public DiscountNotFoundException() {
        super("还未设置优惠信息，无法执行更新或删除操作");
    }

    public DiscountNotFoundException(String message) {
        super(message);
    }
}
