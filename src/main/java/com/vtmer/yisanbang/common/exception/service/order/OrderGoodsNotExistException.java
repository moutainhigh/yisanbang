package com.vtmer.yisanbang.common.exception.service.order;

public class OrderGoodsNotExistException extends RuntimeException {
    public OrderGoodsNotExistException() {
        super("订单中的商品不存在");
    }

    public OrderGoodsNotExistException(String message) {
        super(message);
    }
}
