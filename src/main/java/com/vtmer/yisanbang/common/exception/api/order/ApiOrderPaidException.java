package com.vtmer.yisanbang.common.exception.api.order;

import com.vtmer.yisanbang.common.exception.api.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = {"classpath:errorCode.yml"})
public class ApiOrderPaidException extends ApiException {

    @Value("${order.orderPaidErrorCode}")
    private static Long OrderPaidErrorCode;

    public ApiOrderPaidException(String message) {
        super(OrderPaidErrorCode,message,null);
    }
}
