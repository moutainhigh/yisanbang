package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.domain.Ad;
import com.vtmer.yisanbang.dto.AdDTO;
import com.vtmer.yisanbang.dto.ShowAdDTO;

import java.util.List;

public interface AdService {

    // 返回所有广告的详细信息(后台管理)
    List<Ad> listAllAdInfo();

    // 返回所有展示的广告(前台展示)
    List<ShowAdDTO> listShowedAd();

    // 获取指定广告信息
    Ad listAdInfoByAdId(Integer adId);

    // 删除指定广告信息
    boolean deleteAdInfo(Integer adId);

    // 修改广告图片的url
    boolean updateUrl(String url, Integer adId);

    // 修改广告图片
    boolean updatePic(String picPath, Integer adId);

    // 修改广告是否显示
    boolean updateAdIsShowed(Integer adId);

    // 新建广告信息
    int addAdInfo(AdDTO adDto);

    // 判断广告显示顺序是否已经存在
    boolean isShowOrderExisted(Integer order);

    // 修改广告信息
    int updateAdInfo(Integer adId, AdDTO adDto);
}