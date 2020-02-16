package com.vtmer.yisanbang.common.exception.api.refund;

import com.vtmer.yisanbang.common.exception.api.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = {"classpath:errorCode.yml"})
public class ApiRefundNotMatchUserException extends ApiException {
    @Value("${refund.refundNotMatchUserErrorCode}")
    private static Long RefundNotMatchUserErrorCode;

    public ApiRefundNotMatchUserException(String message) {
        super(RefundNotMatchUserErrorCode,message,null);
    }
}
