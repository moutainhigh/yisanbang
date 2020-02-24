package com.vtmer.yisanbang.common.exception.service.third;

public class Code2SessionException extends RuntimeException{
    public Code2SessionException() {
        super("调用微信code2session接口失败");
    }

    public Code2SessionException(String message) {
        super(message);
    }
}
