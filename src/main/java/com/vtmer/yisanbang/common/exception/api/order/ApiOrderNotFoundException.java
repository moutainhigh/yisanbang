package com.vtmer.yisanbang.common.exception.api.order;

import com.vtmer.yisanbang.common.exception.api.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = {"classpath:errorCode.yml"})
public class ApiOrderNotFoundException extends ApiException {

    @Value("${order.orderNotFoundErrorCode}")
    private static Long OrderNotFoundErrorCode;

    public ApiOrderNotFoundException(String message) {
        super(OrderNotFoundErrorCode,message,null);
    }
}
