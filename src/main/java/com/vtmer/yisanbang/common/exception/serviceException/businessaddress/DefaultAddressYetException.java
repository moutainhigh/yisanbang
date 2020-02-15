package com.vtmer.yisanbang.common.exception.serviceException.businessaddress;

public class DefaultAddressYetException extends RuntimeException {

    public DefaultAddressYetException() {
        super("该收货地址已是默认收货地址");
    }

    public DefaultAddressYetException(String message) {
        super(message);
    }
}
