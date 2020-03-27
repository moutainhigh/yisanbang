package com.vtmer.yisanbang.common.exception.api.businessaddress;

import com.vtmer.yisanbang.common.exception.api.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = {"classpath:errorCode.yml"})
public class ApiNotFindBusinessAddressException extends ApiException {

    @Value("${businessAddress.notFindBusinessAddressErrorCode}")
    private static Long NotFindBusinessAddressErrorCode;

    public ApiNotFindBusinessAddressException(String message) {
        super(NotFindBusinessAddressErrorCode,message,null);
    }
}
