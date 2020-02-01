package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.dto.GoodsDto;

import java.util.List;

public interface GoodsService {
    // 添加商品
    public boolean addGoods(GoodsDto goods);

    // 根据商品id删除商品
    public boolean deleteGoodsById(Integer goodsId);

    // 根据商品id查找商品
    public GoodsDto selectDtoByPrimaryKey(Integer goodsId);

    // 查找所有商品
    public List<GoodsDto> selectAllDto();

    // 根据商品名称查找商品
    public GoodsDto selectDtoByGoodsName(String goodsName);

    // 根据商品id更新商品
    public boolean updateGoods(GoodsDto goods);

    // 查看是否存在相同商品
    public boolean judgeGoods(GoodsDto goods, List<GoodsDto> goodsList);

    // 隐藏商品，不展示
    public boolean hideGoods(GoodsDto goods);
}
