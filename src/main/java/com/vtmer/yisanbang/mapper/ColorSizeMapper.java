package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.ColorSize;
import com.vtmer.yisanbang.dto.ColorSizeDTO;
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

    int updateInventoryByPrimaryKey(Map<String,Integer> inventoryMap);

    // Dto
    int insertDto(ColorSizeDTO record);

    // Dto
    ColorSizeDTO selectDtoByPrimaryKey(Integer id);

    // Dto
    List<ColorSizeDTO> selectAllDto();

    // Dto
    int updateDtoByPrimaryKey(ColorSizeDTO record);

    // Dto
    List<ColorSizeDTO> selectAllDtoByGoodsId(Integer goodsId);
}