package com.vtmer.yisanbang.common.exception.api.postage;

import com.vtmer.yisanbang.common.exception.api.ApiException;
import org.springframework.beans.factory.annotation.Value;

public class ApiPostageSettingsNotFoundException extends ApiException {
    @Value("${postage.postageSettingsNotFoundErrorCode}")
    private static Long PostageSettingsNotFoundErrorCode;

    public ApiPostageSettingsNotFoundException(String message) {
        super(PostageSettingsNotFoundErrorCode,message,null);
    }
}
