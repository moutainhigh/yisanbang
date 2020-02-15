package com.vtmer.yisanbang.common.exception.apiException.businessaddress;

import com.vtmer.yisanbang.common.exception.apiException.ApiException;
import org.springframework.beans.factory.annotation.Value;

public class ApiDefaultAddressYetException extends ApiException {

    @Value("${businessAddress.DefaultAddressYetErrorCode}")
    private static Long NotFindBusinessAddressErrorCode;

    public ApiDefaultAddressYetException(String message) {
        super(NotFindBusinessAddressErrorCode,message,null);
    }
}
