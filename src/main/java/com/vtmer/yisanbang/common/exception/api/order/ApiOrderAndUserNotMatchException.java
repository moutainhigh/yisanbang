package com.vtmer.yisanbang.common.exception.api.order;

import com.vtmer.yisanbang.common.exception.api.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = {"classpath:errorCode.yml"})
public class ApiOrderAndUserNotMatchException extends ApiException {

    @Value("${order.orderAndUserNotMatchErrorCode}")
    private static Long OrderAndUserNotMatchErrorCode;

    public ApiOrderAndUserNotMatchException(String message) {
        super(OrderAndUserNotMatchErrorCode,message,null);
    }
}
