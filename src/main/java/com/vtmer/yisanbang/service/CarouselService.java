package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.domain.Carousel;
import com.vtmer.yisanbang.dto.CarouselDTO;

import java.util.List;

public interface CarouselService {
    // 返回所有轮播图的详细信息(后台管理)
    List<Carousel> listAllCarouselInfo();

    // 获取指定轮播图信息
    Carousel listInfoById(Integer carouselId);

    // 删除指定轮播图信息
    boolean deleteInfo(Integer carouselId);

    // 修改轮播图是否显示
    boolean updateIsShowed(Integer carouselId);

    // 新建轮播图信息
    int addInfo(CarouselDTO carouselDto);

    // 判断轮播图显示顺序是否已经存在
    boolean isShowOrderExisted(Integer order);

    // 修改轮播图信息
    int updateInfo(Integer carouselId, CarouselDTO carouselDto);
}
