package com.vtmer.yisanbang.common.exception.api.order;

import com.vtmer.yisanbang.common.exception.api.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = {"classpath:errorCode.yml"})
public class ApiOrderAlreadyRemindException extends ApiException {
    @Value("${order.orderAlreadyRemindErrorCode}")
    private static Long OrderAlreadyRemindErrorCode;

    public ApiOrderAlreadyRemindException(String message) {
        super(OrderAlreadyRemindErrorCode,message,null);
    }
}
