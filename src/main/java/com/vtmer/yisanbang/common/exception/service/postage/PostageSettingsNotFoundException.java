package com.vtmer.yisanbang.common.exception.service.postage;

public class PostageSettingsNotFoundException extends RuntimeException {
    public PostageSettingsNotFoundException() {
        super("目前暂无邮费设置，不可进行删除或更新操作");
    }

    public PostageSettingsNotFoundException(String message) {
        super(message);
    }
}
