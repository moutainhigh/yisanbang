package com.vtmer.yisanbang.domain;

import java.util.Date;

public class Postage {
    private Integer id;

    private Double price;

    private Double defaultPostage;

    private Date createTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDefaultPostage() {
        return defaultPostage;
    }

    public void setDefaultPostage(Double defaultPostage) {
        this.defaultPostage = defaultPostage;
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
}