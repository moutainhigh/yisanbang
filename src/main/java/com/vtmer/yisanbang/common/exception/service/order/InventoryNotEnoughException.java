package com.vtmer.yisanbang.common.exception.service.order;

/**
 * 库存不足异常
 */
public class InventoryNotEnoughException extends RuntimeException {
    public InventoryNotEnoughException() {
        super("商品库存不足");
    }
    public InventoryNotEnoughException(String message) {
        super(message);
    }
}
