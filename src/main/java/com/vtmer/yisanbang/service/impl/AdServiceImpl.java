package com.vtmer.yisanbang.service.impl;

import com.vtmer.yisanbang.domain.Ad;
import com.vtmer.yisanbang.dto.AdDto;
import com.vtmer.yisanbang.dto.ShowAdDto;
import com.vtmer.yisanbang.mapper.AdMapper;
import com.vtmer.yisanbang.service.AdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdServiceImpl implements AdService {

    @Autowired
    private AdMapper adMapper;

    @Override
    public List<Ad> listAllAdInfo() {
        List<Ad> ads = adMapper.selectAll();
        if (ads != null && !ads.isEmpty()) {
            return ads;
        }
        return null;
    }

    @Override
    public List<ShowAdDto> listShowedAd() {
        List<ShowAdDto> ads = adMapper.selectShowedAd();
        if (ads != null && !ads.isEmpty()) {
            return ads;
        }
        return null;
    }

    @Override
    public Ad listAdInfoByAdId(Integer adId) {
        Ad ad = adMapper.selectByPrimaryKey(adId);
        if (ad != null) {
            return ad;
        }
        return null;
    }

    @Override
    public boolean deleteAdInfo(Integer adId) {
        int flag = adMapper.deleteByPrimaryKey(adId);
        if (flag > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean updateUrl(String url, Integer adId) {
        int flag = adMapper.updateUrl(url, adId);
        if (flag > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean updatePic(String picPath, Integer adId) {
        int flag = adMapper.updatePic(picPath, adId);
        if (flag > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean updateAdIsShowed(Integer adId) {
        int flag = 0;
        Ad ad = adMapper.selectByPrimaryKey(adId);
        if (ad != null) {
            if (ad.getIsShow()) {
                flag = adMapper.updateAd2UnShow(adId);
            } else {
                flag = adMapper.updateAd2Show(adId);
            }
        }
        if (flag > 0) {
            return true;
        }
        return false;
    }

    @Override
    public int addAdInfo(AdDto adDto) {
        return adMapper.insertAdInfo(adDto);
    }

    @Override
    public boolean isShowOrderExisted(Integer order) {
        List<Integer> orders = adMapper.selectExitedOrder();
        return orders.contains(order);
    }

    @Override
    public int updateAdInfo(Integer adId, AdDto adDto) {
        adDto.setId(adId);
        return adMapper.updateAdInfoSelective(adDto);
    }

}
