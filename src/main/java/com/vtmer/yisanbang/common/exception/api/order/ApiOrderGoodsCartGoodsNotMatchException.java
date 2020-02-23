package com.vtmer.yisanbang.common.exception.api.order;

import com.vtmer.yisanbang.common.exception.api.ApiException;
import org.springframework.beans.factory.annotation.Value;

public class ApiOrderGoodsCartGoodsNotMatchException extends ApiException {

    @Value("${order.orderGoodsCartGoodsNotMatchErrorCode}")
    private static Long OrderGoodsCartGoodsNotMatchErrorCode;

    public ApiOrderGoodsCartGoodsNotMatchException(String message) {
        super(OrderGoodsCartGoodsNotMatchErrorCode, message, null);
    }
}
