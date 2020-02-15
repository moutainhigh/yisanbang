package com.vtmer.yisanbang.common.exception.apiException.businessaddress;

import com.vtmer.yisanbang.common.exception.apiException.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = {"classpath:errorCode.yml"})
public class ApiNotFindBusinessAddressException extends ApiException {

    @Value("${businessAddress.NotFindBusinessAddressErrorCode}")
    private static Long NotFindBusinessAddressErrorCode;

    public ApiNotFindBusinessAddressException(String message) {
        super(NotFindBusinessAddressErrorCode,message,null);
    }
}
