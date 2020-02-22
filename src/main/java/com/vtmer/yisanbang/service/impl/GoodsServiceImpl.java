package com.vtmer.yisanbang.service.impl;

import com.vtmer.yisanbang.dto.GoodsDTO;
import com.vtmer.yisanbang.mapper.GoodsMapper;
import com.vtmer.yisanbang.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    // 添加商品
    public boolean addGoods(GoodsDTO goods) {
        int addFlag = goodsMapper.insertDto(goods);
        if (addFlag > 0) return true;
        return false;
    }

    @Override
    // 根据商品id删除商品
    public boolean deleteGoodsById(Integer goodsId) {
        int deleteFlag = goodsMapper.deleteByPrimaryKey(goodsId);
        if (deleteFlag > 0) return true;
        return false;
    }

    @Override
    // 根据商品id查找商品
    public GoodsDTO selectDtoByPrimaryKey(Integer goodsId) {
        GoodsDTO goods = goodsMapper.selectDtoByPrimaryKey(goodsId);
        if (goods != null) return goods;
        return null;
    }

    @Override
    // 查找所有商品
    public List<GoodsDTO> selectAllDto() {
        List<GoodsDTO> goodsDtos = goodsMapper.selectAllDto();
        if (goodsDtos != null && !goodsDtos.isEmpty()) return goodsDtos;
        return null;
    }

    @Override
    // 根据商品名称查找商品
    public GoodsDTO selectDtoByGoodsName(String goodsName) {
        GoodsDTO goods = goodsMapper.selectDtoByGoodsName(goodsName);
        if (goods != null) return goods;
        return null;
    }

    @Override
    // 根据商品id更新商品
    public boolean updateGoods(GoodsDTO goods) {
        int updateFlag = goodsMapper.updateDtoByPrimaryKey(goods);
        if (updateFlag > 0) return true;
        return false;
    }

    @Override
    // 查看是否存在相同商品
    public boolean judgeGoods(GoodsDTO goods, List<GoodsDTO> goodsList) {
        for (GoodsDTO goodsDto : goodsList) {
            if (goodsDto.getName().equals(goods.getName()))
                if (goodsDto.getSortId() == goods.getSortId())
                    return true;
        }
        return false;
    }

    @Override
    // 隐藏商品，不展示
    public boolean hideGoods(GoodsDTO goods) {
        if (goods.getIsShow()) {
            int hideFlag = goodsMapper.hideGoods(goods.getId());
            if (hideFlag > 0) return true;
        } else {
            int showFlag = goodsMapper.showGoods(goods.getId());
            if (showFlag > 0) return true;
        }
        return false;
    }

    @Override
    // 根据商品分类显示商品
    public List<GoodsDTO> selectAllDtoBySort(Integer sortId) {
        List<GoodsDTO> goodsDtos = goodsMapper.selectAllDtoBySort(sortId);
        if (goodsDtos != null && !goodsDtos.isEmpty()) return goodsDtos;
        return null;
    }

    @Override
    // 根据分类以及商品价格排序显示商品
    public List<GoodsDTO> selectAllDtoBySortOrderByPrice(Integer sortId) {
        List<GoodsDTO> goodsDtos = goodsMapper.selectAllDtoBySortOrderByPrice(sortId);
        if (goodsDtos != null && !goodsDtos.isEmpty()) return goodsDtos;
        return null;
    }

    @Override
    // 根据分类以及商品更新时间排序显示商品
    public List<GoodsDTO> selectAllDtoBySortOrderByTime(Integer sortId) {
        List<GoodsDTO> goodsDtos = goodsMapper.selectAllDtoBySortOrderByTime(sortId);
        if (goodsDtos != null && !goodsDtos.isEmpty()) return goodsDtos;
        return null;
    }

    @Override
    // 根据商品价格排序显示商品
    public List<GoodsDTO> selectAllDtoOrderByPrice() {
        List<GoodsDTO> goodsDtos = goodsMapper.selectAllDtoOrderByPrice();
        if (goodsDtos != null && !goodsDtos.isEmpty()) return goodsDtos;
        return null;
    }

    @Override
    // 根据商品更新时间排序显示商品
    public List<GoodsDTO> selectAllDtoOrderByTime() {
        List<GoodsDTO> goodsDtos = goodsMapper.selectAllDtoOrderByTime();
        if (goodsDtos != null && !goodsDtos.isEmpty()) return goodsDtos;
        return null;
    }
}
