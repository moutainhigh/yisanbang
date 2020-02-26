package com.vtmer.yisanbang.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Date;

@ApiModel
public class Refund {

    @ApiModelProperty(value = "退款id",example = "3")
    private Integer id;

    @ApiModelProperty(value = "退款编号",example = "12345678998765432110")
    private String refundNumber;

    @NotNull(message = "refundPrice is null")
    @ApiModelProperty(value = "退款金额",example = "123",required = true)
    private Double refundPrice;

    @ApiModelProperty(hidden = true)
    private Integer userId;

    @NotNull(message = "orderId is null")
    @ApiModelProperty(value = "订单id",example = "1")
    private Integer orderId;

    @ApiModelProperty(value = "退款原因",example = "我不想要了")
    private String reason;

    @ApiModelProperty(value = "退款状态",example = "2")
    private Integer status;

    @ApiModelProperty(hidden = true)
    private Integer isReceived;

    @ApiModelProperty(value = "退款单创建时间")
    private Date createTime;

    @ApiModelProperty(hidden = true)
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRefundNumber() {
        return refundNumber;
    }

    public void setRefundNumber(String refundNumber) {
        this.refundNumber = refundNumber == null ? null : refundNumber.trim();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Double getRefundPrice() {
        return refundPrice;
    }

    public void setRefundPrice(Double refundPrice) {
        this.refundPrice = refundPrice;
    }

    public Integer getIsReceived() {
        return isReceived;
    }

    public void setIsReceived(Integer isReceived) {
        this.isReceived = isReceived;
    }
}