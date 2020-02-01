package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.OrderGoods;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface OrderGoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderGoods record);

    OrderGoods selectByPrimaryKey(Integer id);

    List<OrderGoods> selectAll();

    int updateByPrimaryKey(OrderGoods record);

    List<OrderGoods> selectByOrderId(Integer orderId);
}