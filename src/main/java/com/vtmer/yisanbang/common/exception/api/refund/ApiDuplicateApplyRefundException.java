package com.vtmer.yisanbang.common.exception.api.refund;

import com.vtmer.yisanbang.common.exception.api.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = {"classpath:errorCode.yml"})
public class ApiDuplicateApplyRefundException extends ApiException {
    @Value("${refund.duplicateApplyRefundErrorCode}")
    private static Long DuplicateApplyRefundErrorCode;

    public ApiDuplicateApplyRefundException(String message) {
        super(DuplicateApplyRefundErrorCode,message,null);
    }
}
