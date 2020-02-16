package com.vtmer.yisanbang.common.exception.api.refund;

import com.vtmer.yisanbang.common.exception.api.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = {"classpath:errorCode.yml"})
public class ApiRefundStatusNotFitException extends ApiException {
    @Value("${refund.refundStatusNotFitErrorCode}")
    private static Long RefundStatusNotFitErrorCode;

    public ApiRefundStatusNotFitException(String message) {
        super(RefundStatusNotFitErrorCode,message,null);
    }
}
