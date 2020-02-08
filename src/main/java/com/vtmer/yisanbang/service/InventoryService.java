package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.domain.OrderGoods;
import com.vtmer.yisanbang.domain.RefundGoods;


public interface InventoryService {
    // 交易前判断该商品库存是否足够
    public boolean JudgeInventory(OrderGoods orderGoods);

    // 交易后该商品库存进行减少
    public boolean minusInventory(OrderGoods orderGoods);

    // 取消交易后该商品库存增加
    public boolean addInventory(RefundGoods refundGoods);
}
