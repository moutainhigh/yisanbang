package com.vtmer.yisanbang.controller;

import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.common.qiniu.QiniuDelete;
import com.vtmer.yisanbang.common.qiniu.QiniuUpload;
import com.vtmer.yisanbang.domain.Ad;
import com.vtmer.yisanbang.dto.ShowAdDto;
import com.vtmer.yisanbang.service.AdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/ad")
public class AdController {

    @Autowired
    private AdService adService;

    @GetMapping("/upload")
    public ResponseMessage uploadPic(MultipartFile pic) {
        String picName = UUID.randomUUID().toString();
        try {
            String picPath = QiniuUpload.updateFile(pic, "ad/" + picName);
            return ResponseMessage.newSuccessInstance(picPath, "广告图片上传成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseMessage.newErrorInstance("广告图片上传失败");
        }
    }

    @PostMapping("/add")
    public ResponseMessage addAdPic(@RequestBody Map<String, String> pic) {
        if (!pic.containsKey("picPath") || pic.get("picPath") == null || pic.get("picPath").equals("")) {
            return ResponseMessage.newErrorInstance("图片路径不能为空");
        }
        if (adService.addAdPic(pic.get("picPath"))) {
            return ResponseMessage.newSuccessInstance("广告图片插入成功");
        }
        return ResponseMessage.newErrorInstance("广告图片插入失败");
    }

    @GetMapping("/ads")
    public ResponseMessage getAllAdInfo() {
        List<Ad> ads = adService.listAllAdInfo();
        if (ads != null) {
            return ResponseMessage.newSuccessInstance(ads, "获取所有广告信息成功");
        }
        return ResponseMessage.newErrorInstance("获取广告信息失败");
    }

    @GetMapping("/showedAds")
    public ResponseMessage getShowedAd() {
        List<ShowAdDto> ads = adService.listShowedAd();
        if (ads != null) {
            return ResponseMessage.newSuccessInstance(ads, "获取前台展示广告成功");
        }
        return ResponseMessage.newErrorInstance("获取前台展示广告失败");
    }

    @GetMapping("/{adId}")
    public ResponseMessage getAdInfo(@PathVariable("adId") Integer adId) {
        Ad ad = adService.listAdInfoByAdId(adId);
        if (ad != null) {
            return ResponseMessage.newSuccessInstance(ad, "获取指定广告信息成功");
        }
        return ResponseMessage.newErrorInstance("获取指定广告信息失败");
    }

    @DeleteMapping("/{adId}")
    public ResponseMessage deleteAdInfo(@PathVariable("adId") Integer adId) {
        if (adService.deleteAdInfo(adId)) {
            return ResponseMessage.newSuccessInstance("删除指定广告信息成功");
        }
        return ResponseMessage.newErrorInstance("删除指定广告信息失败");
    }

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

    @PutMapping("/isShowed/{adId}")
    public ResponseMessage updateAdIsShowed(@PathVariable("adId") Integer adId) {
        if (adService.updateAdIsShowed(adId)) {
            return ResponseMessage.newSuccessInstance("广告显示状态修改成功");
        }
        return ResponseMessage.newErrorInstance("广告显示状态修改失败");
    }


}
