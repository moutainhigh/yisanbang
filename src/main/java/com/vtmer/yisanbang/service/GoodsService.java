package com.vtmer.yisanbang.service;


import com.vtmer.yisanbang.domain.GoodsDetail;
import com.vtmer.yisanbang.dto.GoodsDTO;

import java.util.List;

public interface GoodsService {
    // 添加商品
    public boolean addGoods(GoodsDTO goods);

    // 根据商品id删除商品
    public boolean deleteGoodsById(Integer goodsId);

    // 根据商品id查找商品
    public GoodsDTO selectDtoByPrimaryKey(Integer goodsId);

    // 查找所有商品
    public List<GoodsDTO> selectAllDto();

    // 根据商品名称与简介查找商品
    public List<GoodsDTO> selectDtoByContent(String content);

    // 根据商品id更新商品
    public boolean updateGoods(GoodsDTO goods);

    // 查看是否存在相同商品
    public boolean judgeGoods(GoodsDTO goods, List<GoodsDTO> goodsList);

    // 隐藏商品，不展示
    public boolean hideGoods(GoodsDTO goods);

    // 根据分类id显示商品
    public List<GoodsDTO> selectAllDtoBySort(Integer sortId);

    // 根据分类以及商品价格排序显示商品
    public List<GoodsDTO> selectAllDtoBySortOrderByPrice(Integer sortId);

    // 根据分类商品更新时间排序显示商品
    public List<GoodsDTO> selectAllDtoBySortOrderByTime(Integer sortId);

    // 根据商品价格排序显示商品
    public List<GoodsDTO> selectAllDtoOrderByPrice();

    // 根据商品更新时间排序显示商品
    public List<GoodsDTO> selectAllDtoOrderByTime();

    public List<GoodsDTO> selectAllShow();
}
