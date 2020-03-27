package com.vtmer.yisanbang.common.exception.api.order;

import com.vtmer.yisanbang.common.exception.api.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = {"classpath:errorCode.yml"})
public class ApiCartEmptyException extends ApiException {

    @Value("${order.cartEmptyErrorCode}")
    private static Long CartEmptyErrorCode;

    public ApiCartEmptyException(String message) {
        super(CartEmptyErrorCode,message,null);
    }
}
