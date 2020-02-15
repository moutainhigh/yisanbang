package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.dto.CartGoodsDTO;
import com.vtmer.yisanbang.dto.ColorSizeDto;

import java.util.List;

public interface ColorSizeService {
    // 查找所有颜色尺寸
    public List<ColorSizeDto> selectALL();

    // 添加颜色尺寸
    public boolean addColorSize(ColorSizeDto colorSizeDto);

    // 删除颜色尺寸
    public boolean deleteColorSize(Integer colorSizeId);

    // 更新颜色尺寸
    public boolean updateColorSize(ColorSizeDto colorSizeDto);

    // 根据颜色尺寸id查找颜色尺寸
    public ColorSizeDto selectColorSizeById(Integer colorSizeId);

    // 根据商品id查找所有该商品的颜色尺寸
    public List<ColorSizeDto> selectAllByGoodsId(Integer goodsId);

    // 判断颜色尺寸是否存在
    public boolean judgeColorSize(ColorSizeDto colorSizeDto);

    // 查找所有颜色
    public List<String> selectAllColorById(Integer goodsId);

    // 查找所有尺寸
    public List<String> selectAllSizeById(Integer goodsId);

    // 根据颜色尺寸查找显示库存
    public Integer selectInventoryByColorSize(Integer goodsId, String Color, String Size);

    /**
     * 根据colorSizeId设置商品sku属性——价格、图片、尺寸、颜色
     * @param cartGoodsDto:colorSizeId
     * @return
     */
    CartGoodsDTO setSkuById(CartGoodsDTO cartGoodsDto);
}
