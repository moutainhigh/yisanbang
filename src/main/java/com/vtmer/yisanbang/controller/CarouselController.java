package com.vtmer.yisanbang.controller;

import com.github.pagehelper.PageHelper;
import com.vtmer.yisanbang.common.PageResponseMessage;
import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.common.qiniu.QiniuUpload;
import com.vtmer.yisanbang.domain.Carousel;
import com.vtmer.yisanbang.dto.CarouselDTO;
import com.vtmer.yisanbang.service.CarouselService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Api(tags = "轮播图接口")
@RestController
@RequestMapping("/carousel")
public class CarouselController {

    @Autowired
    private CarouselService carouselService;

    @ApiOperation(value = "上传轮播图图片", notes = "执行成功后返回图片路径(img.yisanbang.com/carousel/图片名称)")
    @PostMapping("/upload")
    public ResponseMessage uploadPic(@ApiParam("选择上传图片") MultipartFile pic) {
        String picType = pic.getOriginalFilename().substring(pic.getOriginalFilename().lastIndexOf(".") + 1);
        System.out.println(picType);
        if ("jpg".equals(picType) || "JPG".equals(picType) || "jpeg".equals(picType) || "JPEG".equals(picType) || "png".equals(picType) || "PNG".equals(picType)) {
            String picName = UUID.randomUUID().toString();
            try {
                String picPath = QiniuUpload.updateFile(pic, "carousel/" + picName);
                return ResponseMessage.newSuccessInstance(picPath, "轮播图图片上传成功");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseMessage.newErrorInstance("轮播图图片上传失败");
            }
        } else {
            return ResponseMessage.newErrorInstance("请选择.jpg/.JPG/.jpeg/.JPEG/.png/.PNG图片文件");
        }
    }

    @ApiOperation("添加轮播图信息")
    @PostMapping("/add")
    public ResponseMessage addCarouselInfo(@Validated @RequestBody CarouselDTO carouselDto) {
        if (carouselService.isShowOrderExisted(carouselDto.getShowOrder())) {
            return ResponseMessage.newErrorInstance("显示顺序已存在，轮播图信息添加失败");
        }
        int count = carouselService.addInfo(carouselDto);
        if (count > 0) {
            return ResponseMessage.newSuccessInstance("轮播图信息添加成功");
        }
        return ResponseMessage.newErrorInstance("轮播图信息添加失败");
    }

    @ApiOperation("根据id修改轮播图信息")
    @PutMapping("/{carouselId}")
    public ResponseMessage updateCarouselInfo(@PathVariable("carouselId") Integer carouselId,@Validated @RequestBody CarouselDTO carouselDto) {
        if (!carouselDto.getShowOrder().equals(carouselService.listInfoById(carouselId).getShowOrder()) && carouselService.isShowOrderExisted(carouselDto.getShowOrder())) {
            return ResponseMessage.newErrorInstance("显示顺序已存在，广告信息修改失败");
        }
        int count = carouselService.updateInfo(carouselId, carouselDto);
        if (count > 0) {
            return ResponseMessage.newSuccessInstance("轮播图信息修改成功");
        }
        return ResponseMessage.newErrorInstance("轮播图信息修改失败");
    }

    @ApiOperation(value = "查询轮播图信息", notes = "(默认不分页，传入分页相关参数则返回分页信息)")
    @GetMapping("/get/list")
    public ResponseMessage getAllCarouselInfo(@ApiParam("查询页数(第几页)") @Param(value = "pageNum") Integer pageNum,
                                              @ApiParam("单页查询数量") @Param(value = "pageSize") Integer pageSize) {
        if (pageNum != null && pageSize != null && pageNum != 0 && pageSize != 0) {
            PageHelper.startPage(pageNum, pageSize);
            List<Carousel> carousels = carouselService.listAllCarouselInfo();
            if (carousels != null) {
                return ResponseMessage.newSuccessInstance(PageResponseMessage.restPage(carousels), "获取所有轮播图信息成功");
            }
            return ResponseMessage.newErrorInstance("获取轮播图信息失败");
        } else {
            List<Carousel> carousels = carouselService.listAllCarouselInfo();
            if (carousels != null) {
                return ResponseMessage.newSuccessInstance(carousels, "获取所有轮播图信息成功");
            }
            return ResponseMessage.newErrorInstance("获取轮播图信息失败");
        }
    }

    @ApiOperation("根据id查询轮播图信息")
    @GetMapping("/get/{carouselId}")
    public ResponseMessage getAdInfo(@PathVariable("carouselId") Integer carouselId) {
        Carousel carousel = carouselService.listInfoById(carouselId);
        if (carousel != null) {
            return ResponseMessage.newSuccessInstance(carousel, "获取指定轮播图信息成功");
        }
        return ResponseMessage.newErrorInstance("获取指定轮播图信息失败");
    }

    @ApiOperation("根据id删除轮播图信息")
    @DeleteMapping("/{carouselId}")
    public ResponseMessage deleteAdInfo(@PathVariable("carouselId") Integer carouselId) {
        if (carouselService.deleteInfo(carouselId)) {
            return ResponseMessage.newSuccessInstance("删除指定轮播图信息成功");
        }
        return ResponseMessage.newErrorInstance("删除指定轮播图信息失败");
    }

    @ApiOperation("根据id修改轮播图是否显示")
    @PutMapping("/isShowed/{carouselId}")
    public ResponseMessage updateAdIsShowed(@PathVariable("carouselId") Integer carouselId) {
        if (carouselService.updateIsShowed(carouselId)) {
            return ResponseMessage.newSuccessInstance("轮播图显示状态修改成功");
        }
        return ResponseMessage.newErrorInstance("轮播图显示状态修改失败");
    }
}
