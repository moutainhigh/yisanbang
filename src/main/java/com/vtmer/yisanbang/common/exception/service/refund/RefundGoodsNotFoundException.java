package com.vtmer.yisanbang.common.exception.service.refund;

public class RefundGoodsNotFoundException extends RuntimeException {

    public RefundGoodsNotFoundException() {
        super("找不到退款商品");
    }
    public RefundGoodsNotFoundException(String message) {
        super(message);
    }

}
