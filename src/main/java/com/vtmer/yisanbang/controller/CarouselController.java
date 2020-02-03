package com.vtmer.yisanbang.controller;

import com.github.pagehelper.PageHelper;
import com.vtmer.yisanbang.common.PageResponseMessage;
import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.common.qiniu.QiniuUpload;
import com.vtmer.yisanbang.domain.Carousel;
import com.vtmer.yisanbang.dto.CarouselDto;
import com.vtmer.yisanbang.service.CarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/carousel")
public class CarouselController {

    @Autowired
    private CarouselService carouselService;

    @GetMapping("/upload")
    public ResponseMessage uploadPic(MultipartFile pic) {
        String picName = UUID.randomUUID().toString();
        try {
            String picPath = QiniuUpload.updateFile(pic, "carousel/" + picName);
            return ResponseMessage.newSuccessInstance(picPath, "轮播图图片上传成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseMessage.newErrorInstance("轮播图图片上传失败");
        }
    }

    @PostMapping("/add")
    public ResponseMessage addCarouselInfo(@RequestBody CarouselDto carouselDto) {
        if (carouselService.isShowOrderExisted(carouselDto.getShowOrder())) {
            return ResponseMessage.newErrorInstance("显示顺序已存在，轮播图信息添加失败");
        }
        int count = carouselService.addInfo(carouselDto);
        if (count > 0) {
            return ResponseMessage.newSuccessInstance("轮播图信息添加成功");
        }
        return ResponseMessage.newErrorInstance("轮播图信息添加失败");
    }

    @PutMapping("/{carouselId}")
    public ResponseMessage updatecarouselInfo(@PathVariable("carouselId") Integer carouselId, @RequestBody CarouselDto carouselDto) {
        if (!carouselDto.getShowOrder().equals(carouselService.listInfoById(carouselId).getShowOrder()) && carouselService.isShowOrderExisted(carouselDto.getShowOrder())) {
            return ResponseMessage.newErrorInstance("显示顺序已存在，广告信息修改失败");
        }
        int count = carouselService.updateInfo(carouselId, carouselDto);
        if (count > 0) {
            return ResponseMessage.newSuccessInstance("轮播图信息修改成功");
        }
        return ResponseMessage.newErrorInstance("轮播图信息修改失败");
    }

    @GetMapping("/list")
    public ResponseMessage getAllCarouselInfo(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                              @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Carousel>carousels = carouselService.listAllCarouselInfo();
        if (carousels != null) {
            return ResponseMessage.newSuccessInstance(PageResponseMessage.restPage(carousels), "获取所有轮播图信息成功");
        }
        return ResponseMessage.newErrorInstance("获取轮播图信息失败");
    }

    @GetMapping("/{carouselId}")
    public ResponseMessage getAdInfo(@PathVariable("carouselId") Integer carouselId) {
        Carousel carousel = carouselService.listInfoById(carouselId);
        if (carousel != null) {
            return ResponseMessage.newSuccessInstance(carousel, "获取指定轮播图信息成功");
        }
        return ResponseMessage.newErrorInstance("获取指定轮播图信息失败");
    }

    @DeleteMapping("/{carouselId}")
    public ResponseMessage deleteAdInfo(@PathVariable("carouselId") Integer carouselId) {
        if (carouselService.deleteInfo(carouselId)) {
            return ResponseMessage.newSuccessInstance("删除指定轮播图信息成功");
        }
        return ResponseMessage.newErrorInstance("删除指定轮播图信息失败");
    }

    @PutMapping("/isShowed/{carouselId}")
    public ResponseMessage updateAdIsShowed(@PathVariable("carouselId") Integer carouselId) {
        if (carouselService.updateIsShowed(carouselId)) {
            return ResponseMessage.newSuccessInstance("轮播图显示状态修改成功");
        }
        return ResponseMessage.newErrorInstance("轮播图显示状态修改失败");
    }
}
