package com.vtmer.yisanbang.common.exception.service.order;

public class CartEmptyException extends RuntimeException {
    public CartEmptyException() {
        super("读取用户购物车勾选商品为空");
    }
    public CartEmptyException(String message) {
        super(message);
    }
}
