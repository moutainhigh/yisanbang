package com.vtmer.yisanbang.common.exception.service.cart;

public class CartGoodsNotExistException extends RuntimeException{

    public CartGoodsNotExistException() {
        super("购物车商品不存在");
    }

    public CartGoodsNotExistException(String message) {
        super(message);
    }
}
