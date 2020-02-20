package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.dto.GoodsDTO;
import com.vtmer.yisanbang.dto.SuitDTO;

import java.util.List;

public interface GoodsAndSuitService {
    // 显示单件商品与套装商品
    public List selectGoodsAndSuit(List<GoodsDTO> goodsDTOList, List<SuitDTO> suitDTOList);

    // 根据价格从低到高显示单件商品与套装商品
    public List selectGoodsAndSuitByPriceAsc(List<GoodsDTO> goodsDTOList, List<SuitDTO> suitDTOList);

    // 根据价格从高到低显示单件商品与套装商品
    public List selectGoodsAndSuitByPriceDec(List<GoodsDTO> goodsDTOList, List<SuitDTO> suitDTOList);

    // 根据时间从低到高显示单件商品与套装商品
    public List selectGoodsAndSuitByTimeAsc(List<GoodsDTO> goodsDTOList, List<SuitDTO> suitDTOList);

    // 根据时间从高到低显示单件商品与套装商品
    public List selectGoodsAndSuitByTimeDec(List<GoodsDTO> goodsDTOList, List<SuitDTO> suitDTOList);
}
