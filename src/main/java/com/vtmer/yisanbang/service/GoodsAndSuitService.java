package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.dto.GoodsDTO;
import com.vtmer.yisanbang.dto.SuitDTO;

import java.util.List;
import java.util.Objects;

public interface GoodsAndSuitService {
    // 显示单件商品与套装商品
    public List selectGoodsAndSuit(List objectList1, List ObjectList2);

    // 根据价格从低到高显示单件商品与套装商品
    public List selectGoodsAndSuitByPrice(List objectList1, List ObjectList2);

    // 根据时间从低到高显示单件商品与套装商品
    public List selectGoodsAndSuitByTime(List objectList1, List ObjectList2);
}
