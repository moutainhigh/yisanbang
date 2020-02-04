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
    public List<ColorSizeDto> selectALL() {
        List<ColorSizeDto> colorSizeDtos = colorSizeMapper.selectAllDto();
        if (colorSizeDtos != null) return colorSizeDtos;
        return null;
    }

    @Override
    public boolean addColorSize(ColorSizeDto colorSizeDto) {
        int insertFlag = colorSizeMapper.insertDto(colorSizeDto);
        if (insertFlag > 0) return true;
        return false;
    }

    @Override
    public boolean deleteColorSize(ColorSizeDto colorSizeDto) {
        int deleteFlag = colorSizeMapper.deleteByPrimaryKey(colorSizeDto.getId());
        if (deleteFlag > 0) return true;
        return false;
    }

    @Override
    public boolean updateColorSize(ColorSizeDto colorSizeDto) {
        int updateFlag = colorSizeMapper.updateDtoByPrimaryKey(colorSizeDto);
        if (updateFlag > 0) return true;
        return false;
    }

    @Override
    public ColorSizeDto selectColorSizeById(Integer colorSizeId) {
        ColorSizeDto colorSizeDto = colorSizeMapper.selectDtoByPrimaryKey(colorSizeId);
        if (colorSizeDto != null) return colorSizeDto;
        return null;
    }

    @Override
    public List<ColorSizeDto> selectAllByGoodsId(Integer goodsId) {
        List<ColorSizeDto> colorSizeDtos = colorSizeMapper.selectAllDtoByGoodsId(goodsId);
        if (colorSizeDtos != null) return colorSizeDtos;
        return null;
    }

}
