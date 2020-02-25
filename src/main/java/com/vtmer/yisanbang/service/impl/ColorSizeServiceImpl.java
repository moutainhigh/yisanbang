package com.vtmer.yisanbang.service.impl;

import com.vtmer.yisanbang.dto.CartGoodsDTO;
import com.vtmer.yisanbang.dto.ColorSizeDTO;
import com.vtmer.yisanbang.dto.GoodsDTO;
import com.vtmer.yisanbang.mapper.ColorSizeMapper;
import com.vtmer.yisanbang.mapper.GoodsMapper;
import com.vtmer.yisanbang.service.ColorSizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ColorSizeServiceImpl implements ColorSizeService {
    @Autowired
    private ColorSizeMapper colorSizeMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    // 查找所有的颜色尺寸
    public List<ColorSizeDTO> selectALL() {
        List<ColorSizeDTO> colorSizeDtos = colorSizeMapper.selectAllDto();
        if (colorSizeDtos != null) {
            return colorSizeDtos;
        }
        return null;
    }

    @Override
    // 添加颜色尺寸
    public boolean addColorSize(ColorSizeDTO colorSizeDto) {
        int insertFlag = colorSizeMapper.insertDto(colorSizeDto);
        if (insertFlag > 0) {
            return true;
        }
        return false;
    }

    @Override
    // 删除颜色尺寸
    public boolean deleteColorSize(Integer colorSizeId) {
        int deleteFlag = colorSizeMapper.deleteByPrimaryKey(colorSizeId);
        if (deleteFlag > 0) {
            return true;
        }
        return false;
    }

    @Override
    // 更新颜色尺寸
    public boolean updateColorSize(ColorSizeDTO colorSizeDto) {
        int updateFlag = colorSizeMapper.updateDtoByPrimaryKey(colorSizeDto);
        if (updateFlag > 0) {
            return true;
        }
        return false;
    }

    @Override
    // 根据颜色尺寸id查找颜色尺寸
    public ColorSizeDTO selectColorSizeById(Integer colorSizeId) {
        ColorSizeDTO colorSizeDto = colorSizeMapper.selectDtoByPrimaryKey(colorSizeId);
        if (colorSizeDto != null) {
            return colorSizeDto;
        }
        return null;
    }

    @Override
    // 根据商品id查找所有该商品的颜色尺寸
    public List<ColorSizeDTO> selectAllByGoodsId(Integer goodsId) {
        List<ColorSizeDTO> colorSizeDtos = colorSizeMapper.selectAllDtoByGoodsId(goodsId);
        if (colorSizeDtos != null) {
            return colorSizeDtos;
        }
        return null;
    }

    @Override
    // 判断颜色尺寸是否存在相同
    public boolean judgeColorSize(ColorSizeDTO colorSizeDto) {
        List<ColorSizeDTO> colorSizeDtoList = colorSizeMapper.selectAllDto();
        for (ColorSizeDTO colorSize : colorSizeDtoList) {
            if (colorSize.getGoodsId().equals(colorSizeDto.getGoodsId())) {
                if (colorSize.getSize().equals(colorSizeDto.getSize())) {
                    if (colorSize.getColor().equals(colorSizeDto.getColor())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    // 查找所有颜色
    public List<String> selectAllColorById(Integer goodsId) {
        List<ColorSizeDTO> colorSizeDtos = colorSizeMapper.selectAllDtoByGoodsId(goodsId);
        List<String> colorList = new ArrayList<>();
        if (colorSizeDtos != null && !colorSizeDtos.isEmpty()) {
            for (ColorSizeDTO colorSize : colorSizeDtos) {
                String color = colorSize.getColor();
                colorList.add(color);
            }
            return colorList;
        }
        return null;
    }

    @Override
    // 查找所有尺寸
    public List<String> selectAllSizeById(Integer goodsId) {
        List<ColorSizeDTO> colorSizeDtos = colorSizeMapper.selectAllDtoByGoodsId(goodsId);
        List<String> sizeList = new ArrayList<>();
        if (colorSizeDtos != null && !colorSizeDtos.isEmpty()) {
            for (ColorSizeDTO colorSize : colorSizeDtos) {
                String size = colorSize.getSize();
                sizeList.add(size);
            }
            return sizeList;
        }
        return null;
    }

    @Override
    // 根据颜色尺寸查找显示库存
    public Integer selectInventoryByColorSize(Integer goodsId, String Color, String Size) {
        List<ColorSizeDTO> colorSizeDtos = colorSizeMapper.selectAllDtoByGoodsId(goodsId);
        for (ColorSizeDTO colorSize : colorSizeDtos) {
            if (colorSize.getColor().equals(Color)) {
                if (colorSize.getSize().equals(Size)) {
                    return colorSize.getInventory();
                }
            }
        }
        return null;
    }

    public CartGoodsDTO setSkuById(CartGoodsDTO cartGoodsDTO) {
        ColorSizeDTO goodsSku = colorSizeMapper.selectDtoByPrimaryKey(cartGoodsDTO.getColorSizeId());
        // 商品尺寸
        cartGoodsDTO.setSize(goodsSku.getSize());
        // 商品颜色
        cartGoodsDTO.setPartOrColor(goodsSku.getColor());
        // 查询商品信息
        GoodsDTO goodsDto = goodsMapper.selectDtoByPrimaryKey(goodsSku.getGoodsId());
        // 商品标题
        cartGoodsDTO.setTitle(goodsDto.getName());
        // 商品价格
        cartGoodsDTO.setPrice(goodsDto.getPrice());
        // 设置商品id
        cartGoodsDTO.setId(goodsDto.getId());
        // 商品图片
        cartGoodsDTO.setPicture(goodsDto.getPicture());
        return cartGoodsDTO;
    }

}
