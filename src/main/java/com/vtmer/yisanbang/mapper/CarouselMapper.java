package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.Carousel;
import java.util.List;

public interface CarouselMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Carousel record);

    Carousel selectByPrimaryKey(Integer id);

    List<Carousel> selectAll();

    int updateByPrimaryKey(Carousel record);
}