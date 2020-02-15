package com.vtmer.yisanbang.common.exception.api.discount;

import com.vtmer.yisanbang.common.exception.api.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = {"classpath:errorCode.yml"})
public class ApiDiscountExistException extends ApiException {

    @Value("${discount.discountExistErrorCode}")
    private static Long DiscountExistErrorCode;

    public ApiDiscountExistException(String message) {
        super(DiscountExistErrorCode,message,null);
    }
}
