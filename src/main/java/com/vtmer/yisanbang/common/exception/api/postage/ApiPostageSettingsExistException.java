package com.vtmer.yisanbang.common.exception.api.postage;

import com.vtmer.yisanbang.common.exception.api.ApiException;
import org.springframework.beans.factory.annotation.Value;

public class ApiPostageSettingsExistException extends ApiException {
    @Value("${postage.postageSettingsExistErrorCode}")
    private static Long PostageSettingsExistErrorCode;

    public ApiPostageSettingsExistException(String message) {
        super(PostageSettingsExistErrorCode,message,null);
    }
}
