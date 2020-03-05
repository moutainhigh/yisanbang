package com.vtmer.yisanbang.controller;

import com.github.pagehelper.PageHelper;
import com.vtmer.yisanbang.common.PageResponseMessage;
import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.common.annotation.RequestLog;
import com.vtmer.yisanbang.common.qiniu.QiniuUpload;
import com.vtmer.yisanbang.common.tencentcloud.COSClientUtil;
import com.vtmer.yisanbang.domain.Ad;
import com.vtmer.yisanbang.dto.AdDTO;
import com.vtmer.yisanbang.service.AdService;
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

@SuppressWarnings({"ALL", "AlibabaUndefineMagicConstant"})
@Api(tags = "广告接口")
@RestController
@RequestMapping("/ad")
public class AdController {

    @Autowired
    private AdService adService;

    @RequestLog(module = "首页广告", operationDesc = "查询所有广告信息")
    @ApiOperation(value = "查询所有广告信息", notes = "(默认不分页，传入分页相关参数则返回分页信息)")
    @GetMapping("/get/list")
    public ResponseMessage getAllAdInfo(@ApiParam("查询页数(第几页)") @Param(value = "pageNum") Integer pageNum,
                                        @ApiParam("单页查询数量") @Param(value = "pageSize") Integer pageSize) {
        if (pageNum != null && pageSize != null && pageNum != 0 && pageSize != 0) {
            PageHelper.startPage(pageNum, pageSize);
            List<Ad> ads = adService.listAllAdInfo();
            if (ads != null) {
                return ResponseMessage.newSuccessInstance(PageResponseMessage.restPage(ads), "查询所有广告信息成功");
            }
            return ResponseMessage.newErrorInstance("查询广告信息失败");
        } else {
            List<Ad> ads = adService.listAllAdInfo();
            if (ads != null) {
                return ResponseMessage.newSuccessInstance(ads, "查询所有广告信息成功");
            }
            return ResponseMessage.newErrorInstance("查询广告信息失败");
        }
    }

    @RequestLog(module = "首页广告", operationDesc = "查询指定广告信息")
    @ApiOperation("根据id查询广告信息")
    @GetMapping("/get/{adId}")
    public ResponseMessage getAdInfo(@PathVariable("adId") Integer adId) {
        Ad ad = adService.listAdInfoByAdId(adId);
        if (ad != null) {
            return ResponseMessage.newSuccessInstance(ad, "查询指定广告信息成功");
        }
        return ResponseMessage.newErrorInstance("查询指定广告信息失败");
    }

    @RequestLog(module = "首页广告", operationDesc = "上传广告图片")
    @ApiOperation(value = "上传广告图片", notes = "执行成功后返回图片路径(img.yisanbang.com/ad/图片名称)")
    @PostMapping("/upload")
    public ResponseMessage uploadPic(@ApiParam("选择上传图片") MultipartFile pic) {
        String picType = pic.getOriginalFilename().substring(pic.getOriginalFilename().lastIndexOf(".") + 1);
        System.out.println(picType);
        if ("jpg".equals(picType) || "JPG".equals(picType) || "jpeg".equals(picType) || "JPEG".equals(picType) || "png".equals(picType) || "PNG".equals(picType)) {
            // String picName = UUID.randomUUID().toString();
            try {
                // String picPath = QiniuUpload.updateFile(pic, "ad/" + picName);
                COSClientUtil cosClientUtil = new COSClientUtil();
                String picPath = cosClientUtil.uploadFile(pic, "ad/");
                return ResponseMessage.newSuccessInstance(COSClientUtil.getObjectPath() + picPath, "广告图片上传成功");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseMessage.newErrorInstance("广告图片上传失败");
            }
        } else {
            return ResponseMessage.newErrorInstance("请选择.jpg/.JPG/.jpeg/.JPEG/.png/.PNG图片文件");
        }

    }

    @RequestLog(module = "首页广告", operationDesc = "添加广告信息")
    @ApiOperation("添加广告信息")
    @PostMapping("/add")
    public ResponseMessage addAdInfo(@Validated @RequestBody AdDTO adDto) {
        if (adDto.getGoodsType() == 0) {
            adDto.setGoodsId(0);
        }
        if (adService.isShowOrderExisted(adDto.getShowOrder())) {
            return ResponseMessage.newErrorInstance("显示顺序已存在，广告信息添加失败");
        }
        int count = adService.addAdInfo(adDto);
        if (count > 0) {
            return ResponseMessage.newSuccessInstance("广告信息添加成功");
        }
        return ResponseMessage.newErrorInstance("广告信息添加失败");
    }

    @RequestLog(module = "首页广告", operationDesc = "修改广告信息")
    @ApiOperation("根据id修改广告信息")
    @PutMapping("/{adId}")
    public ResponseMessage updateAdInfo(@PathVariable("adId") Integer adId, @Validated @RequestBody AdDTO adDto) {
        if (adDto.getGoodsType() == 0) {
            adDto.setGoodsId(0);
        }
        if (!adDto.getShowOrder().equals(adService.listAdInfoByAdId(adId).getShowOrder()) && adService.isShowOrderExisted(adDto.getShowOrder())) {
            return ResponseMessage.newErrorInstance("显示顺序已存在，广告信息修改失败");
        }
        int count = adService.updateAdInfo(adId, adDto);
        if (count > 0) {
            return ResponseMessage.newSuccessInstance("广告信息修改成功");
        }
        return ResponseMessage.newErrorInstance("广告信息修改失败");
    }

    @RequestLog(module = "首页广告", operationDesc = "修改广告显示状态")
    @ApiOperation("根据id修改广告是否显示")
    @PutMapping("/isShowed/{adId}")
    public ResponseMessage updateAdIsShowed(@PathVariable("adId") Integer adId) {
        if (adService.updateAdIsShowed(adId)) {
            return ResponseMessage.newSuccessInstance("广告显示状态修改成功");
        }
        return ResponseMessage.newErrorInstance("广告显示状态修改失败");
    }

    @RequestLog(module = "首页广告", operationDesc = "删除广告信息")
    @ApiOperation("根据id删除广告信息")
    @DeleteMapping("/{adId}")
    public ResponseMessage deleteAdInfo(@PathVariable("adId") Integer adId) {
        if (adService.deleteAdInfo(adId)) {
            return ResponseMessage.newSuccessInstance("删除指定广告信息成功");
        }
        return ResponseMessage.newErrorInstance("删除指定广告信息失败");
    }

    /*
    @GetMapping("/showedAds")
    public ResponseMessage getShowedAd() {
        List<ShowAdDTO> ads = adService.listShowedAd();
        if (ads != null) {
            return ResponseMessage.newSuccessInstance(ads, "获取前台展示广告成功");
        }
        return ResponseMessage.newErrorInstance("获取前台展示广告失败");
    }
     */

    /*
    @PutMapping("/url/{adId}")
    public ResponseMessage updateUrl(@PathVariable("adId") Integer adId, @RequestBody Map<String, String> url) {
        if (!url.containsKey("url") || url.get("url").equals("") || url.get("url") == null) {
            return ResponseMessage.newErrorInstance("url不能为空");
        }
        if (adService.updateUrl(url.get("url"), adId)) {
            return ResponseMessage.newSuccessInstance("广告url修改成功");
        }
        return ResponseMessage.newErrorInstance("广告url修改失败");
    }
     */

    /*
    @PutMapping("/picture/{adId}")
    public ResponseMessage updatePic(@PathVariable("adId") Integer adId, MultipartFile pic) {
        String picName = UUID.randomUUID().toString();
        String oldPic = adService.listAdInfoByAdId(adId).getPicturePath();
        try {
            String picPath = QiniuUpload.updateFile(pic, "ad/" + picName);
            if (adService.updatePic(picPath, adId)) {
                // 删除被替换的图片
                QiniuDelete.deleteFile(oldPic);
                return ResponseMessage.newSuccessInstance("广告图片修改成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseMessage.newErrorInstance("广告图片修改失败");
        }
        return ResponseMessage.newErrorInstance("广告图片修改失败");
    }
     */

}
