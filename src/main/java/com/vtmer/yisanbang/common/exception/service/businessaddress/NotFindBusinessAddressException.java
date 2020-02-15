package com.vtmer.yisanbang.common.exception.service.businessaddress;

public class NotFindBusinessAddressException extends RuntimeException {

    public NotFindBusinessAddressException() {
        super("找不到此商家收货地址");
    }

    public NotFindBusinessAddressException(String message) {
        super(message);
    }
}
