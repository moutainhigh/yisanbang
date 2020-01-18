package com.vtmer.yisanbang.common;

public class ResponseMessage {
    public static final int STATUS_SUCCESS = 200; // 成功
    public static final int STATUS_ERROR = 400; // 失败

    private int status;
    private Object data;
    private String message;
    private Long timestamp;


    public ResponseMessage(int status, Object data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
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