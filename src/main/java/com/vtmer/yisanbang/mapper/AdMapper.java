package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.Ad;
import com.vtmer.yisanbang.dto.ShowAdDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Ad record);

    Ad selectByPrimaryKey(Integer id);

    List<Ad> selectAll();

    int updateByPrimaryKey(Ad record);

    // 返回按升序排列的所有用于展示图片及其url
    List<ShowAdDto> selectShowedAd();

    // 修改点击广告跳转的url
    int updateUrl(String url, Integer adId);

    // 修改广告图片
    int updatePic(String picPath, Integer adId);

    // 修改广告为显示状态
    int updateAd2Show(Integer adId);

    // 修改广告为不显示状态
    int updateAd2UnShow(Integer adId);

    // 新增广告图片
    int addAdPic(String picPath);

}