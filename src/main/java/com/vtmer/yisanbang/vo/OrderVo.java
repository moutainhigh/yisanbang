package com.vtmer.yisanbang.vo;

import com.vtmer.yisanbang.common.validGroup.Insert;
import com.vtmer.yisanbang.common.validGroup.Update;
import com.vtmer.yisanbang.domain.UserAddress;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class OrderVo {

    @Valid
    @NotNull(groups = {Insert.class,Update.class},message = "用户地址信息为空")
    private UserAddress userAddress;

    @Valid
    @NotNull(groups = {Insert.class},message = "订单商品信息为空")
    private CartVo orderGoodsList;

    // 留言
    private String message;

    // 订单编号
    @NotBlank(groups = {Update.class},message = "订单编号为空")
    private String orderNumber;

    private double postage;

    private Integer refundStatus;

    private Date createTime;

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

    public double getPostage() {
        return postage;
    }

    public void setPostage(double postage) {
        this.postage = postage;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(Integer refundStatus) {
        this.refundStatus = refundStatus;
    }
}
