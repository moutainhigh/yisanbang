package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.domain.Order;
import com.vtmer.yisanbang.dto.OrderDTO;
import com.vtmer.yisanbang.dto.OrderGoodsDTO;
import com.vtmer.yisanbang.vo.OrderVO;

import java.util.List;
import java.util.Map;

public interface OrderService {

    OrderVO confirmCartOrder();

    OrderVO confirmDirectOrder(List<OrderGoodsDTO> orderGoodsDTOList);

    String createCartOrder(OrderDTO orderDTO);

    String createDirectOrder(OrderDTO orderDTO);

    /**
     * 获取用户的状态订单
     * @param status
     * @return
     */
    List<OrderVO> getUserOrderList(Integer status);

    /**
     * 获取商城的状态订单
     * @param status
     * @return
     */
    List<OrderVO> getOrderList(Integer status);

    int updateOrderStatus(String orderNumber);

    void setOrderStatus(Map<String,Integer> orderMap);

    void deleteOrder(Integer orderId);

    OrderVO selectOrderVOByOrderNumber(String orderNumber);

    Order selectOrderByOrderNumber(String orderNumber);

    void setCourierNumber(Order order);

    void setOrderGoodsDTO(OrderGoodsDTO orderGoodsDTO, Integer sizeId, Boolean isGoods);

    void updateAddress(OrderVO orderVO);

    void cancelOrder(String orderNumber);

    void updateInventory(String orderNumber,Integer flag);

    List<Order> getUnRefundPayOrderList();

    /**
     * 订单超时处理
     */
    void orderTimeOutLogic();
}
