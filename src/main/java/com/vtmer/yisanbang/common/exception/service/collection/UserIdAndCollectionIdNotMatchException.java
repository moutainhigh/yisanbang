package com.vtmer.yisanbang.common.exception.service.collection;

public class UserIdAndCollectionIdNotMatchException extends RuntimeException {

    public UserIdAndCollectionIdNotMatchException() {
        super("用户id和收藏夹id不匹配");
    }

    public UserIdAndCollectionIdNotMatchException(String message) {
        super(message);
    }
}
