package com.vtmer.yisanbang.service.impl;

import com.vtmer.yisanbang.domain.Carousel;
import com.vtmer.yisanbang.dto.CarouselDTO;
import com.vtmer.yisanbang.mapper.CarouselMapper;
import com.vtmer.yisanbang.service.CarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarouselServiceImpl implements CarouselService {

    @Autowired
    private CarouselMapper carouselMapper;

    @Override
    public List<Carousel> listAllCarouselInfo() {
        return carouselMapper.selectAll();
    }

    @Override
    public Carousel listInfoById(Integer carouselId) {
        Carousel carousel = carouselMapper.selectByPrimaryKey(carouselId);
        if (carousel != null) {
            return carousel;
        }
        return null;
    }

    @Override
    public boolean deleteInfo(Integer carouselId) {
        int flag = carouselMapper.deleteByPrimaryKey(carouselId);
        if (flag > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean updateIsShowed(Integer carouselId) {
        int flag = 0;
        Carousel carousel = carouselMapper.selectByPrimaryKey(carouselId);
        if (carousel != null) {
            if (carousel.getIsShow()) {
                flag = carouselMapper.updateCarousel2UnShow(carouselId);
            } else {
                flag = carouselMapper.updateCarousel2Show(carouselId);
            }
        }
        if (flag > 0) {
            return true;
        }
        return false;
    }

    @Override
    public int addInfo(CarouselDTO carouselDto) {
        return carouselMapper.insertCarouselInfo(carouselDto);
    }

    @Override
    public boolean isShowOrderExisted(Integer order) {
        List<Integer> orders = carouselMapper.selectExitedOrder();
        return orders.contains(order);
    }

    @Override
    public int updateInfo(Integer carouselId, CarouselDTO carouselDto) {
        carouselDto.setId(carouselId);
        return carouselMapper.updateCarouselInfoSelective(carouselDto);
    }
}
