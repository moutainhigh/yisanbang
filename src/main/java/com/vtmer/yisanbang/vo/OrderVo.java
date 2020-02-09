package com.vtmer.yisanbang.vo;

import com.vtmer.yisanbang.common.validGroup.Insert;
import com.vtmer.yisanbang.common.validGroup.Update;
import com.vtmer.yisanbang.domain.UserAddress;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@ApiModel
public class OrderVo {

    @Valid
    @NotNull(groups = {Insert.class,Update.class},message = "用户地址信息为空")
    @ApiModelProperty(value = "用户地址信息",required = true,example = "广东工业大学教学三号楼创客基地E102")
    private UserAddress userAddress;

    @Valid
    @NotNull(groups = {Insert.class},message = "订单商品信息为空")
    @ApiModelProperty(value = "订单商品列表")
    private CartVo orderGoodsList;

    // 留言
    @ApiModelProperty(value = "订单留言",required = false,example = "不要发货，我钱多")
    private String message;

    // 订单编号
    @NotBlank(groups = {Update.class},message = "订单编号为空")
    @ApiModelProperty(readOnly = true,value = "订单编号",example = "12345678998765432110")
    private String orderNumber;

    @ApiModelProperty(readOnly = true,value = "邮费",example = "8")
    private double postage;

    @ApiModelProperty(readOnly = true,example = "2",value = "退款状态",notes = "退款状态定义：status 退款状态 0--等待商家处理  1--退款中（待买家发货） 2--退款中（待商家收货） 3--退款成功 4--退款失败")
    private Integer refundStatus;

    @ApiModelProperty(required = true,value = "订单创建时间")
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
