package com.vtmer.yisanbang.common.exception.api.shiro;

import com.vtmer.yisanbang.common.exception.api.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = {"classpath:errorCode.yml"})
public class ApiUnknownAccountException extends ApiException {
    @Value("${shiro.unknownAccountExceptionErrorCode}")
    private static Long UnknownAccountExceptionErrorCode;

    public ApiUnknownAccountException(String message) {
        super(UnknownAccountExceptionErrorCode,message,null);
    }
}
