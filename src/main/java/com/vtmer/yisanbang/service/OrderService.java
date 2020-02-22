package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.domain.Order;
import com.vtmer.yisanbang.dto.CartOrderDTO;
import com.vtmer.yisanbang.dto.OrderDTO;
import com.vtmer.yisanbang.dto.OrderGoodsDTO;

import java.util.List;
import java.util.Map;

public interface OrderService {

    OrderDTO confirmCartOrder();

    OrderDTO confirmDirectOrder(List<OrderGoodsDTO> orderGoodsDTOList);

    Map<String,String> createCartOrder(CartOrderDTO cartOrderDTO);

    /**
     * 获取用户的状态订单
     * @param status
     * @return
     */
    List<OrderDTO> getUserOrderList(Integer status);

    /**
     * 获取商城的状态订单
     * @param status
     * @return
     */
    List<OrderDTO> getOrderList(Integer status);

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

    List<Order> getUnRefundPayOrderList();

    /**
     * 订单超时处理
     */
    void orderTimeOutLogic();
}
