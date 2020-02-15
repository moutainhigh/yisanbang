package com.vtmer.yisanbang.common.exception.service.collection;

public class CollectionNotFoundException extends RuntimeException {
    public CollectionNotFoundException() {
        super("收藏夹商品不存在");
    }

    public CollectionNotFoundException(String message) {
        super(message);
    }
}
