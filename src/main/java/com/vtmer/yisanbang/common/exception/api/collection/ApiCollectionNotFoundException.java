package com.vtmer.yisanbang.common.exception.api.collection;

import com.vtmer.yisanbang.common.exception.api.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = {"classpath:errorCode.yml"})
public class ApiCollectionNotFoundException extends ApiException {

    @Value("${collection.collectionNotFoundErrorCode}")
    private static Long CollectionNotFoundErrorCode;

    public ApiCollectionNotFoundException(String message) {
        super(CollectionNotFoundErrorCode,message,null);
    }
}
