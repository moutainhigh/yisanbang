package com.vtmer.yisanbang.common.exception.api.third;

import com.vtmer.yisanbang.common.exception.api.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = {"classpath:errorCode.yml"})
public class ApiCode2SessionException extends ApiException {

    @Value("${third.code2SessionExceptionErrorCode}")
    private static Long Code2SessionExceptionErrorCode;

    public ApiCode2SessionException(String message) {
        super(Code2SessionExceptionErrorCode, message, null);
    }
}
