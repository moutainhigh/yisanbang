package com.vtmer.yisanbang.common.exception.api.order;

import com.vtmer.yisanbang.common.exception.api.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = {"classpath:errorCode.yml"})
public class ApiOrderGoodsNotExistException extends ApiException {

    @Value("${order.orderGoodsNotExistErrorCode}")
    private static Long OrderGoodsNotExistErrorCode;

    public ApiOrderGoodsNotExistException(String message) {
        super(OrderGoodsNotExistErrorCode,message,null);
    }
}
