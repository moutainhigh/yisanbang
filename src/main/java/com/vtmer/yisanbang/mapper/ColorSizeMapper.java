package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.ColorSize;
import com.vtmer.yisanbang.dto.ColorSizeDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface ColorSizeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ColorSize record);

    ColorSize selectByPrimaryKey(Integer id);

    List<ColorSize> selectAll();

    int updateByPrimaryKey(ColorSize record);

    void updateInventoryByPrimaryKey(Map<String,Integer> inventoryMap);

    // Dto
    int insertDto(ColorSizeDto record);

    // Dto
    ColorSizeDto selectDtoByPrimaryKey(Integer id);

    // Dto
    List<ColorSizeDto> selectAllDto();

    // Dto
    int updateDtoByPrimaryKey(ColorSizeDto record);

    // Dto
    List<ColorSizeDto> selectAllDtoByGoodsId(Integer goodsId);
}