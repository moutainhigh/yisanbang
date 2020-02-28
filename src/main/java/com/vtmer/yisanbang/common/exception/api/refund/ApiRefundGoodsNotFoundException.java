package com.vtmer.yisanbang.common.exception.api.refund;

import com.vtmer.yisanbang.common.exception.api.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = {"classpath:errorCode.yml"})
public class ApiRefundGoodsNotFoundException extends ApiException {

    @Value("${refund.refundGoodsNotFoundErrorCode}")
    private static Long RefundGoodsNotFoundErrorCode;

    public ApiRefundGoodsNotFoundException(String message) {
        super(RefundGoodsNotFoundErrorCode,message,null);
    }
}
