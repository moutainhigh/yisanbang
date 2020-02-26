package com.vtmer.yisanbang.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@ApiModel
public class Discount {

    @ApiModelProperty(example = "1",hidden = true)
    private Integer id;

    @NotNull(message = "amount is null")
    @ApiModelProperty(value = "满x件打折",example = "5",required = true)
    private Integer amount;

    @NotNull(message = "discountRate is null")
    @Max(value = 1,message = "打折率大于1")
    @Min(value = 0,message = "打折率小于0")
    @ApiModelProperty(value = "打x折",example = "0.8",required = true)
    private double discountRate;

    @ApiModelProperty(hidden = true)
    private Date createTime;

    @ApiModelProperty(hidden = true)
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(Double discountRate) {
        this.discountRate = discountRate;
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

    @Override
    public String toString() {
        return "Discount{" +
                "id=" + id +
                ", amount=" + amount +
                ", discountRate=" + discountRate +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}