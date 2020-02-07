package com.vtmer.yisanbang.domain;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class Collection {

    private Integer id;

    @NotNull(message = "userId is null")
    private Integer userId;

    @NotNull(message = "goodsId is null")
    private Integer goodsId;

    @NotNull(message = "isGoods is null")
    private Boolean isGoods;

    private Date createTime;

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