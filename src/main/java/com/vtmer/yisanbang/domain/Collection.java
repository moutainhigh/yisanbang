package com.vtmer.yisanbang.domain;

import java.util.Date;

public class Collection {
    private Integer id;

    private Integer userId;

    private Integer goodsId;

    private Byte isGoods;

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

    public Byte getIsGoods() {
        return isGoods;
    }

    public void setIsGoods(Byte isGoods) {
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