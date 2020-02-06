package com.vtmer.yisanbang.service.impl;

import com.vtmer.yisanbang.domain.ColorSize;
import com.vtmer.yisanbang.dto.ColorSizeDto;
import com.vtmer.yisanbang.mapper.ColorSizeMapper;
import com.vtmer.yisanbang.service.ColorSizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColorSizeServiceImpl implements ColorSizeService {
    @Autowired
    private ColorSizeMapper colorSizeMapper;

    @Override
    // 查找所有的颜色尺寸
    public List<ColorSizeDto> selectALL() {
        List<ColorSizeDto> colorSizeDtos = colorSizeMapper.selectAllDto();
        if (colorSizeDtos != null) return colorSizeDtos;
        return null;
    }

    @Override
    // 添加颜色尺寸
    public boolean addColorSize(ColorSizeDto colorSizeDto) {
        int insertFlag = colorSizeMapper.insertDto(colorSizeDto);
        if (insertFlag > 0) return true;
        return false;
    }

    @Override
    // 删除颜色尺寸
    public boolean deleteColorSize(ColorSizeDto colorSizeDto) {
        int deleteFlag = colorSizeMapper.deleteByPrimaryKey(colorSizeDto.getId());
        if (deleteFlag > 0) return true;
        return false;
    }

    @Override
    // 更新颜色尺寸
    public boolean updateColorSize(ColorSizeDto colorSizeDto) {
        int updateFlag = colorSizeMapper.updateDtoByPrimaryKey(colorSizeDto);
        if (updateFlag > 0) return true;
        return false;
    }

    @Override
    // 根据颜色尺寸id查找颜色尺寸
    public ColorSizeDto selectColorSizeById(Integer colorSizeId) {
        ColorSizeDto colorSizeDto = colorSizeMapper.selectDtoByPrimaryKey(colorSizeId);
        if (colorSizeDto != null) return colorSizeDto;
        return null;
    }

    @Override
    // 根据商品id查找所有该商品的颜色尺寸
    public List<ColorSizeDto> selectAllByGoodsId(Integer goodsId) {
        List<ColorSizeDto> colorSizeDtos = colorSizeMapper.selectAllDtoByGoodsId(goodsId);
        if (colorSizeDtos != null) return colorSizeDtos;
        return null;
    }

    @Override
    // 判断颜色尺寸是否存在相同
    public boolean judgeColorSize(ColorSizeDto colorSizeDto) {
        List<ColorSizeDto> colorSizeDtoList = colorSizeMapper.selectAllDto();
        for (ColorSizeDto colorSize : colorSizeDtoList) {
            if (colorSize.getGoodsId() == colorSizeDto.getGoodsId())
                if (colorSize.getSize() == colorSizeDto.getSize())
                    if (colorSize.getInventory() == colorSizeDto.getInventory())
                        if (colorSize.getColor().equals(colorSizeDto.getColor()))
                            return true;
        }
        return false;
    }

}