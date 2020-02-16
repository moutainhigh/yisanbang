package com.vtmer.yisanbang.common.exception.service.postage;

public class PostageSettingsExistException extends RuntimeException {
    public PostageSettingsExistException() {
        super("邮费设置已存在");
    }

    public PostageSettingsExistException(String message) {
        super(message);
    }
}
