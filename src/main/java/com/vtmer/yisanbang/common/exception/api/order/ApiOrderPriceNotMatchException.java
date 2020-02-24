package com.vtmer.yisanbang.common.exception.api.order;

import com.vtmer.yisanbang.common.exception.api.ApiException;
import org.springframework.beans.factory.annotation.Value;

public class ApiOrderPriceNotMatchException extends ApiException {
    @Value("${order.orderPriceNotMatchErrorCode}")
    private static Long OrderPriceNotMatchErrorCode;

    public ApiOrderPriceNotMatchException(String message) {
        super(OrderPriceNotMatchErrorCode,message,null);
    }
}
