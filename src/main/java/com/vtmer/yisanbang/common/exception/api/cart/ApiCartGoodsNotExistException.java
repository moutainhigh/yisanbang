package com.vtmer.yisanbang.common.exception.api.cart;

import com.vtmer.yisanbang.common.exception.api.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = {"classpath:errorCode.yml"})
public class ApiCartGoodsNotExistException extends ApiException {

    @Value("${cart.cartGoodsNotExistErrorCode}")
    private static Long CartGoodsNotExistErrorCode;

    public ApiCartGoodsNotExistException(String message) {
        super(CartGoodsNotExistErrorCode,message,null);
    }
}
