package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.Order;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    Order selectByPrimaryKey(Integer id);

    List<Order> selectAll();

    int updateByPrimaryKey(Order record);

    List<Order> selectAllByUserIdAndStatus(Map<String,Integer> orderMap);

    List<Order> selectAllByUserId(Integer userId);

    int updateOrderStatus(Integer orderId);

    int setOrderStatus(Map<String,Integer> orderMap);
}