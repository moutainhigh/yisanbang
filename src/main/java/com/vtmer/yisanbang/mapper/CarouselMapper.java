package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.Carousel;
import com.vtmer.yisanbang.dto.CarouselDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarouselMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Carousel record);

    Carousel selectByPrimaryKey(Integer id);

    List<Carousel> selectAll();

    int updateByPrimaryKey(Carousel record);

    // 修改轮播图为显示状态
    int updateCarousel2Show(Integer carouselId);

    // 修改轮播图为不显示状态
    int updateCarousel2UnShow(Integer carouselId);

    // 新增轮播图信息
    int insertCarouselInfo(CarouselDTO carouselDto);

    // 查询已存在的显示排序
    List<Integer> selectExitedOrder();

    // 修改轮播图信息
    int updateCarouselInfoSelective(CarouselDTO carouselDto);
}