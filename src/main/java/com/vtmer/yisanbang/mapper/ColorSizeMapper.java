package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.ColorSize;
import com.vtmer.yisanbang.dto.ColorSizeDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ColorSizeMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(ColorSize record);

    ColorSize selectByPrimaryKey(Integer id);

    List<ColorSize> selectAll();

    int updateByPrimaryKey(ColorSize record);

    ColorSizeDto selectColorSizeDtoById(Integer id);
}