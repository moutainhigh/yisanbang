package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.domain.GoodsDetail;
import com.vtmer.yisanbang.dto.GoodsDetailDto;

import java.util.List;

public interface GoodsDetailService {
    // 添加商品详细信息
    public boolean addGoodsDetail(GoodsDetailDto goodsDetail);

    // 删除商品详细信息
    public boolean deleteGoodsDetail(Integer goodsDetailId);

    // 更新商品详细信息
    public boolean updateGoodsDetail(GoodsDetailDto goodsDetail);

    // 查找所有商品详细信息
    public List<GoodsDetailDto> selectAllDto();

    // 根据商品id查找商品的所有商品详细信息
    public List<GoodsDetailDto> selectAllDtoByGoodsId(Integer goodsId);

    // 根据商品详细id查找商品详细信息
    public GoodsDetailDto selectGoodsDetailByID(Integer goodsDeteilId);

    // 查看商品详细信息是否相同
    public boolean judgeGoodsDetail(GoodsDetailDto goodsDetail, List<GoodsDetailDto> goodsDetailDtoList);
}
