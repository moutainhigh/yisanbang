package com.vtmer.yisanbang.common.exception.api.businessaddress;

import com.vtmer.yisanbang.common.exception.api.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = {"classpath:errorCode.yml"})
public class ApiDefaultAddressYetException extends ApiException {

    @Value("${businessAddress.defaultAddressYetErrorCode}")
    private static Long DefaultAddressYetErrorCode;

    public ApiDefaultAddressYetException(String message) {
        super(DefaultAddressYetErrorCode,message,null);
    }
}
