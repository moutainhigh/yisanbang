package com.vtmer.yisanbang.service.impl;


import com.vtmer.yisanbang.common.util.Comparator.ComparatorGoodsOrSuitDetail;
import com.vtmer.yisanbang.domain.Collection;
import com.vtmer.yisanbang.dto.GoodsDetailDTO;
import com.vtmer.yisanbang.mapper.GoodsDetailMapper;
import com.vtmer.yisanbang.service.GoodsDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collections;
import java.util.List;

@Service
public class GoodsDetailServiceImpl implements GoodsDetailService {
    @Autowired
    private GoodsDetailMapper goodsDetailMapper;

    @Override
    // 添加商品详细信息
    public boolean addGoodsDetail(GoodsDetailDTO goodsDetail) {
        int addFlag = goodsDetailMapper.insertDto(goodsDetail);
        if (addFlag > 0) return true;
        return false;
    }

    @Override
    // 删除商品详细信息
    public boolean deleteGoodsDetail(Integer goodsDetailId) {
        int deleteFlag = goodsDetailMapper.deleteByPrimaryKey(goodsDetailId);
        if (deleteFlag > 0) return true;
        return false;
    }

    @Override
    // 更新商品详细信息
    public boolean updateGoodsDetail(GoodsDetailDTO goodsDetail) {
        int updateFlag = goodsDetailMapper.updateDtoByPrimaryKey(goodsDetail);
        if (updateFlag > 0) return true;
        return false;
    }

    @Override
    // 查找所有商品详细信息
    public List<GoodsDetailDTO> selectAllDto() {
        List<GoodsDetailDTO> goodsDetailDtos = goodsDetailMapper.selectAllDto();
        if (goodsDetailDtos != null) return goodsDetailDtos;
        return null;
    }

    @Override
    // 根据商品id查找商品的所有商品详细信息
    public List<GoodsDetailDTO> selectAllDtoByGoodsId(Integer goodsId) {
        List<GoodsDetailDTO> goodsDetailDtos = goodsDetailMapper.selectAllDtoByGoodsId(goodsId);
        if (goodsDetailDtos != null) {
            ComparatorGoodsOrSuitDetail comparatorGoodsOrSuitDetail = new ComparatorGoodsOrSuitDetail();
            Collections.sort(goodsDetailDtos,comparatorGoodsOrSuitDetail);
            return goodsDetailDtos;
        }
        return null;
    }

    @Override
    // 根据商品详细id查找商品详细信息
    public GoodsDetailDTO selectGoodsDetailByID(Integer goodsDeteilId) {
        GoodsDetailDTO goodsDetailDto = goodsDetailMapper.selectDtoByPrimaryKey(goodsDeteilId);
        if (goodsDetailDto != null) return goodsDetailDto;
        return null;
    }
}
