package com.vtmer.yisanbang.common.exception.service.collection;

public class CommodityNotExistException extends RuntimeException {

    public CommodityNotExistException() {
        super("收藏的商品不存在");
    }
    public CommodityNotExistException(String message) {
        super(message);
    }
}
