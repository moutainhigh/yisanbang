package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.dto.GoodsDTO;

public interface OutrightPurchaseService {
    // 检验库存是否足够
    public Boolean judgeInventory(GoodsDTO goodsDto);

    // 生成订单
}
