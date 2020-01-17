package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.OrderGoods;
import java.util.List;

public interface OrderGoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderGoods record);

    OrderGoods selectByPrimaryKey(Integer id);

    List<OrderGoods> selectAll();

    int updateByPrimaryKey(OrderGoods record);
}