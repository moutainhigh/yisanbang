package com.vtmer.yisanbang.common.exception.api.collection;

import com.vtmer.yisanbang.common.exception.api.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = {"classpath:errorCode.yml"})
public class ApiUserIdAndCollectionIdNotMatchException extends ApiException {

    @Value("${collection.userIdAndCollectionIdNotMatchErrorCode}")
    private static Long UserIdAndCollectionIdNotMatchErrorCode;

    public ApiUserIdAndCollectionIdNotMatchException(String message) {
        super(UserIdAndCollectionIdNotMatchErrorCode,message,null);
    }
}
