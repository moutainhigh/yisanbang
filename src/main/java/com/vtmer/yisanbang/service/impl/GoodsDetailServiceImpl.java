package com.vtmer.yisanbang.service.impl;

import com.vtmer.yisanbang.domain.GoodsDetail;
import com.vtmer.yisanbang.dto.GoodsDetailDto;
import com.vtmer.yisanbang.mapper.GoodsDetailMapper;
import com.vtmer.yisanbang.service.GoodsDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.vtmer.yisanbang.common.Md5Util;

import java.io.File;
import java.util.List;

@Service
public class GoodsDetailServiceImpl implements GoodsDetailService {
    @Autowired
    private GoodsDetailMapper goodsDetailMapper;

    @Override
    // 添加商品详细信息
    public boolean addGoodsDetail(GoodsDetailDto goodsDetail) {
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
    public boolean updateGoodsDetail(GoodsDetailDto goodsDetail) {
        int updateFlag = goodsDetailMapper.updateDtoByPrimaryKey(goodsDetail);
        if (updateFlag > 0) return true;
        return false;
    }

    @Override
    // 查找所有商品详细信息
    public List<GoodsDetailDto> selectAllDto() {
        List<GoodsDetailDto> goodsDetailDtos = goodsDetailMapper.selectAllDto();
        if (goodsDetailDtos != null) return goodsDetailDtos;
        return null;
    }

    @Override
    // 根据商品id查找商品的所有商品详细信息
    public List<GoodsDetailDto> selectAllDtoByGoodsId(Integer goodsId) {
        List<GoodsDetailDto> goodsDetailDtos = goodsDetailMapper.selectAllDtoByGoodsId(goodsId);
        if (goodsDetailDtos != null) return goodsDetailDtos;
        return null;
    }

    @Override
    // 根据商品详细id查找商品详细信息
    public GoodsDetailDto selectGoodsDetailByID(Integer goodsDeteilId) {
        GoodsDetailDto goodsDetailDto = goodsDetailMapper.selectDtoByPrimaryKey(goodsDeteilId);
        if (goodsDetailDto != null) return goodsDetailDto;
        return null;
    }

    @Override
    // 查看商品详细信息是否相同
    public boolean judgeGoodsDetail(GoodsDetailDto goodsDetail, List<GoodsDetailDto> goodsDetailDtoList) {
        File file1 = new File(goodsDetail.getPirtucePath());
        String img1 = Md5Util.getFileMD5(file1);
        for (GoodsDetailDto goodDetailDto : goodsDetailDtoList) {
            File file2 = new File(goodDetailDto.getPirtucePath());
            String img2 = Md5Util.getFileMD5(file2);
            if (img1.equals(img2))
                return true;
        }
        return false;
    }
}
