package com.vtmer.yisanbang.common.exception.service.cart;

public class OrderGoodsCartGoodsNotMatchException extends RuntimeException {
    public OrderGoodsCartGoodsNotMatchException() {
        super("下购物车订单的商品列表和购物车勾选商品列表不一致");
    }

    public OrderGoodsCartGoodsNotMatchException(String message) {
        super(message);
    }
}
