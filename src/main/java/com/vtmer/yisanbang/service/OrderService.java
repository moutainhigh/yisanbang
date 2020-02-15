package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.domain.Order;
import com.vtmer.yisanbang.dto.CartGoodsDTO;
import com.vtmer.yisanbang.vo.OrderVo;

import java.util.List;
import java.util.Map;

public interface OrderService {

    OrderVo confirmCartOrder();

    Map<String,String> createCartOrder(OrderVo orderVo);

    List<OrderVo> listOrder(Map<String,Integer> orderMap);

    int updateOrderStatus(String orderNumber);

    int setOrderStatus(Map<String,Integer> orderMap);

    int deleteOrder(Integer orderId);

    OrderVo selectOrderVoByOrderNumber(String orderNumber);

    Order selectOrderByOrderNumber(String orderNumber);

    int setCourierNumber(Order order);

    void setCartGoodsDto(CartGoodsDTO cartGoodsDto, Integer sizeId, Boolean isGoods);

    int updateAddress(OrderVo orderVo);

    int cancelOrder(Integer orderId);

    void updateInventory(String orderNumber,Integer flag);

    List<Order> getNotPayOrder();

    List<Order> getUnRefundPayOrderList();
}
