package com.vtmer.yisanbang.common.exception.api.discount;

import com.vtmer.yisanbang.common.exception.api.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = {"classpath:errorCode.yml"})
public class ApiDiscountNotFoundException extends ApiException {
    @Value("${discount.discountNotFoundErrorCode}")
    private static Long DiscountNotFoundErrorCode;

    public ApiDiscountNotFoundException(String message) {
        super(DiscountNotFoundErrorCode,message,null);
    }
}
