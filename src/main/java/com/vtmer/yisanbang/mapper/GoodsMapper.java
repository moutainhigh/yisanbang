package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.Goods;

import java.util.List;

public interface GoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Goods record);

    Goods selectByPrimaryKey(Integer id);

    List<Goods> selectAll();

    int updateByPrimaryKey(Goods record);

}