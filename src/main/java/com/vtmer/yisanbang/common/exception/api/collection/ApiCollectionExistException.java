package com.vtmer.yisanbang.common.exception.api.collection;

import com.vtmer.yisanbang.common.exception.api.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = {"classpath:errorCode.yml"})
public class ApiCollectionExistException extends ApiException {

    @Value("${collection.collectionExistErrorCode}")
    private static Long CollectionExistErrorCode;

    public ApiCollectionExistException(String message) {
        super(CollectionExistErrorCode,message,null);
    }
}
