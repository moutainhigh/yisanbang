package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.vo.OrderVo;

import java.util.List;
import java.util.Map;

public interface OrderService {

    OrderVo confirmCartOrder(Integer userId);

    String createCartOrder(OrderVo orderVo);

    List<OrderVo> listOrder(Map<String,Integer> orderMap);

    int updateOrderStatus(Integer orderId);

    int setOrderStatus(Map<String,Integer> orderMap);

    int deleteOrder(Integer orderId);

}
