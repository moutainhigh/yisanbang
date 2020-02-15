package com.vtmer.yisanbang.common.exception.api.collection;

import com.vtmer.yisanbang.common.exception.api.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = {"classpath:errorCode.yml"})
public class ApiCommodityNotExistException extends ApiException {

    @Value("${collection.commodityNotExistErrorCode}")
    private static Long CommodityNotExistErrorCode;

    public ApiCommodityNotExistException(String message) {
        super(CommodityNotExistErrorCode,message,null);
    }
}
