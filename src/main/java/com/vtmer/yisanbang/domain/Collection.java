package com.vtmer.yisanbang.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Date;

@ApiModel
public class Collection {

    @ApiModelProperty(readOnly = true,value = "收藏夹id",example = "3")
    private Integer id;

    @ApiModelProperty(value = "用户id",required = true,example = "3")
    @NotNull(message = "userId is null")
    private Integer userId;

    @NotNull(message = "goodsId is null")
    @ApiModelProperty(value = "商品id",example = "4",required = true)
    private Integer goodsId;

    @NotNull(message = "isGoods is null")
    @ApiModelProperty(value = "是否是普通商品",example = "true",required = true)
    private Boolean isGoods;

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Boolean getIsGoods() {
        return isGoods;
    }

    public void setIsGoods(Boolean isGoods) {
        this.isGoods = isGoods;
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
        return "Collection{" +
                "id=" + id +
                ", userId=" + userId +
                ", goodsId=" + goodsId +
                ", isGoods=" + isGoods +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}