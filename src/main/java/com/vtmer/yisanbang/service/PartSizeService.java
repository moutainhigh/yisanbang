package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.dto.CartGoodsDTO;
import com.vtmer.yisanbang.dto.PartSizeDTO;

import java.util.List;

public interface PartSizeService {
    // 查找所有部件尺寸
    public List<PartSizeDTO> selectAll();

    // 添加部件尺寸
    public boolean addPartSize(PartSizeDTO partSizeDto);

    // 删除部件尺寸
    public boolean deletePartSize(Integer partSizeId);

    // 更新部件尺寸
    public boolean updatePartSize(PartSizeDTO partSizeDto);

    // 根据部件尺寸id查找部件尺寸
    public PartSizeDTO selectPartSizeById(Integer partSizeId);

    // 根据套装id查找所有该套装的部件尺寸
    public List<PartSizeDTO> selectAllBySuitId(Integer suitId);

    // 判断部件尺寸是否存在
    public boolean judgePartSize(PartSizeDTO partSizeDto);

    // 查找所有部件
    public List<String> selectAllPartById(Integer suitId);

    // 查找所有尺寸
    public List<String> selectAllSizeById(Integer suitId);

    // 根据部件尺寸查找库存
    public Integer selectInventoryByPartSize(Integer suitId, String part, String size);

    // 根据部件尺寸查找价格
    public Double selectPriceByPartSize(Integer suitId, String part, String size);

    // 根据套装id返回最低价
    public Double selectLowPriceBySuitId(Integer suitId);

    // 根据套装id返回最高价
    public Double selecgHighPriceBySuitId(Integer suitId);

    /**
     * 根据colorSizeId设置套装sku属性——价格、图片、尺寸、部件
     * @param cartGoodsDto:colorSizeId
     * @return
     */
    CartGoodsDTO setSkuById(CartGoodsDTO cartGoodsDto);
}
