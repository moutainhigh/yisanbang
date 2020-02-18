package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.dto.GoodsDto;

public interface OutrightPurchaseService {
    // 检验库存是否足够
    public Boolean judgeInventory(GoodsDto goodsDto);

    // 生成订单
}
