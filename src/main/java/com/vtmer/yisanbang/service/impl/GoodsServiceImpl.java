package com.vtmer.yisanbang.service.impl;

import com.vtmer.yisanbang.dto.GoodsDto;
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
    public boolean addGoods(GoodsDto goods) {
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
    public GoodsDto selectDtoByPrimaryKey(Integer goodsId) {
        GoodsDto goods = goodsMapper.selectDtoByPrimaryKey(goodsId);
        if (goods != null) return goods;
        return null;
    }

    @Override
    // 查找所有商品
    public List<GoodsDto> selectAllDto() {
        List<GoodsDto> goodsDtos = goodsMapper.selectAllDto();
        if (goodsDtos != null && !goodsDtos.isEmpty()) return goodsDtos;
        return null;
    }

    @Override
    // 根据商品名称查找商品
    public GoodsDto selectDtoByGoodsName(String goodsName) {
        GoodsDto goods = goodsMapper.selectDtoByGoodsName(goodsName);
        if (goods != null) return goods;
        return null;
    }

    @Override
    // 根据商品id更新商品
    public boolean updateGoods(GoodsDto goods) {
        int updateFlag = goodsMapper.updateDtoByPrimaryKey(goods);
        if (updateFlag > 0) return true;
        return false;
    }

    @Override
    // 查看是否存在相同商品
    public boolean judgeGoods(GoodsDto goods, List<GoodsDto> goodsList) {
        for (GoodsDto goodsDto : goodsList) {
            if (goodsDto.getName().equals(goods.getName())) return true;
        }
        return false;
    }

    @Override
    // 隐藏商品，不展示
    public boolean hideGoods(GoodsDto goods) {
        goods.setIsShow(false);
        int updateFlag = goodsMapper.updateDtoByPrimaryKey(goods);
        if (updateFlag > 0) return true;
        return false;
    }

    @Override
    // 根据商品分类显示商品
    public List<GoodsDto> selectAllDtoBySort(Integer sortId) {
        List<GoodsDto> goodsDtos = goodsMapper.selectAllDtoBySort(sortId);
        if (goodsDtos != null && !goodsDtos.isEmpty()) return goodsDtos;
        return null;
    }

    @Override
    // 根据分类以及商品价格排序显示商品
    public List<GoodsDto> selectAllDtoBySortOrderByPrice(Integer sortId) {
        List<GoodsDto> goodsDtos = goodsMapper.selectAllDtoBySortOrderByPrice(sortId);
        if (goodsDtos != null && !goodsDtos.isEmpty()) return goodsDtos;
        return null;
    }

    @Override
    // 根据分类以及商品更新时间排序显示商品
    public List<GoodsDto> selectAllDtoBySortOrderByTime(Integer sortId) {
        List<GoodsDto> goodsDtos = goodsMapper.selectAllDtoBySortOrderByTime(sortId);
        if (goodsDtos != null && !goodsDtos.isEmpty()) return goodsDtos;
        return null;
    }

    @Override
    // 根据商品价格排序显示商品
    public List<GoodsDto> selectAllDtoOrderByPrice() {
        List<GoodsDto> goodsDtos = goodsMapper.selectAllDtoOrderByPrice();
        if (goodsDtos != null && !goodsDtos.isEmpty()) return goodsDtos;
        return null;
    }

    @Override
    // 根据商品更新时间排序显示商品
    public List<GoodsDto> selectAllDtoOrderByTime() {
        List<GoodsDto> goodsDtos = goodsMapper.selectAllDtoOrderByTime();
        if (goodsDtos != null && !goodsDtos.isEmpty()) return goodsDtos;
        return null;
    }
}
