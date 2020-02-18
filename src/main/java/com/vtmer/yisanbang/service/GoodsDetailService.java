package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.dto.GoodsDetailDTO;

import java.util.List;

public interface GoodsDetailService {
    // 添加商品详细信息
    public boolean addGoodsDetail(GoodsDetailDTO goodsDetail);

    // 删除商品详细信息
    public boolean deleteGoodsDetail(Integer goodsDetailId);

    // 更新商品详细信息
    public boolean updateGoodsDetail(GoodsDetailDTO goodsDetail);

    // 查找所有商品详细信息
    public List<GoodsDetailDTO> selectAllDto();

    // 根据商品id查找商品的所有商品详细信息
    public List<GoodsDetailDTO> selectAllDtoByGoodsId(Integer goodsId);

    // 根据商品详细id查找商品详细信息
    public GoodsDetailDTO selectGoodsDetailByID(Integer goodsDeteilId);

    // 查看商品详细信息是否相同
    public boolean judgeGoodsDetail(GoodsDetailDTO goodsDetail, List<GoodsDetailDTO> goodsDetailDtoList);
}
