package com.vtmer.yisanbang.common.exception.service.collection;

public class CollectionExistException extends RuntimeException {
    public CollectionExistException() {
        super("该商品已在收藏夹");
    }

    public CollectionExistException(String message) {
        super(message);
    }
}
