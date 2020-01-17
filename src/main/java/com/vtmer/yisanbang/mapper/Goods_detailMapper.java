package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.Goods_detail;
import java.util.List;

public interface Goods_detailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Goods_detail record);

    Goods_detail selectByPrimaryKey(Integer id);

    List<Goods_detail> selectAll();

    int updateByPrimaryKey(Goods_detail record);
}