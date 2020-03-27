package com.vtmer.yisanbang.common.exception.api.refund;

import com.vtmer.yisanbang.common.exception.api.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = {"classpath:errorCode.yml"})
public class ApiRefundNotFoundException extends ApiException {

    @Value("${refund.refundNotFoundErrorCode}")
    private static Long RefundNotFoundErrorCode;

    public ApiRefundNotFoundException(String message) {
        super(RefundNotFoundErrorCode,message,null);
    }
}
