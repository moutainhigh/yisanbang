package com.vtmer.yisanbang.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class ResponseMessage<T> {
    public static final int STATUS_SUCCESS = 200; // 成功
    public static final int STATUS_ERROR = 400; // 失败

    @ApiModelProperty(value = "状态码")
    private int status;
    @ApiModelProperty(value = "数据对象")
    private T data;
    @ApiModelProperty(value = "描述")
    private String message;

    private Long timestamp;

    public ResponseMessage(int status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    public static ResponseMessage newSuccessInstance(Object data, String message) {
        return new ResponseMessage(ResponseMessage.STATUS_SUCCESS, data, message);
    }

    public static ResponseMessage newSuccessInstance(String message) {
        return new ResponseMessage(ResponseMessage.STATUS_SUCCESS, null, message);
    }

    public static ResponseMessage newSuccessInstance(Object data) {
        return new ResponseMessage(ResponseMessage.STATUS_SUCCESS, data, null);
    }

    public static ResponseMessage newErrorInstance(String message) {
        return new ResponseMessage(ResponseMessage.STATUS_ERROR, null, message);
    }
}
