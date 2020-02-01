package com.vtmer.yisanbang.vo;

import com.vtmer.yisanbang.domain.UserAddress;

public class OrderVo {

    private UserAddress userAddress;

    private CartVo orderGoodsList;

    // 留言
    private String message;

    // 订单编号
    private String orderNumber;

    public UserAddress getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(UserAddress userAddress) {
        this.userAddress = userAddress;
    }

    public CartVo getOrderGoodsList() {
        return orderGoodsList;
    }

    public void setOrderGoodsList(CartVo orderGoodsList) {
        this.orderGoodsList = orderGoodsList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
}
