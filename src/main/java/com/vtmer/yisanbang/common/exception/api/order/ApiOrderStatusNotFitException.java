package com.vtmer.yisanbang.common.exception.api.order;

import com.vtmer.yisanbang.common.exception.api.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = {"classpath:errorCode.yml"})
public class ApiOrderStatusNotFitException extends ApiException {

    @Value("${order.orderStatusNotFitErrorCode}")
    private static Long OrderStatusNotFitErrorCode;

    public ApiOrderStatusNotFitException(String message) {
        super(OrderStatusNotFitErrorCode,message,null);
    }
}
