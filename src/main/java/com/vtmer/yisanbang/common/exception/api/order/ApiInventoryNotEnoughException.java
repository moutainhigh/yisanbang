package com.vtmer.yisanbang.common.exception.api.order;

import com.vtmer.yisanbang.common.exception.api.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = {"classpath:errorCode.yml"})
public class ApiInventoryNotEnoughException extends ApiException {

    @Value("${order.inventoryNotEnoughErrorCode}")
    private static Long InventoryNotEnoughErrorCode;

    public ApiInventoryNotEnoughException(String message) {
        super(InventoryNotEnoughErrorCode,message,null);
    }
}
