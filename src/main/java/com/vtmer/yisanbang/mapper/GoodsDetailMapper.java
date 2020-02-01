package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.GoodsDetail;
import java.util.List;

public interface GoodsDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsDetail record);

    GoodsDetail selectByPrimaryKey(Integer id);

    List<GoodsDetail> selectAll();

    int updateByPrimaryKey(GoodsDetail record);
}