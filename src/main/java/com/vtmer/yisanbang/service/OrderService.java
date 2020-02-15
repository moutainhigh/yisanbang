package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.domain.Order;
import com.vtmer.yisanbang.dto.CartOrderDTO;
import com.vtmer.yisanbang.dto.OrderDTO;
import com.vtmer.yisanbang.dto.OrderGoodsDTO;

import java.util.List;
import java.util.Map;

public interface OrderService {

    OrderDTO confirmCartOrder();

    Map<String,String> createCartOrder(CartOrderDTO cartOrderDTO);

    List<OrderDTO> getOrderList(Map<String,Integer> orderMap);

    int updateOrderStatus(String orderNumber);

    void setOrderStatus(Map<String,Integer> orderMap);

    void deleteOrder(Integer orderId);

    OrderDTO selectOrderDTOByOrderNumber(String orderNumber);

    Order selectOrderByOrderNumber(String orderNumber);

    void setCourierNumber(Order order);

    void setOrderGoodsDTO(OrderGoodsDTO orderGoodsDTO, Integer sizeId, Boolean isGoods);

    void updateAddress(OrderDTO orderDTO);

    void cancelOrder(String orderNumber);

    void updateInventory(String orderNumber,Integer flag);

    List<Order> getNotPayOrder();

    List<Order> getUnRefundPayOrderList();
}
