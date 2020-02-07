package com.vtmer.yisanbang.domain;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class Postage {

    private Integer id;

    @NotNull(message = "price(达标金额) is null")
    private Double price;

    @NotNull(message = "defaultPostage(默认邮费) is null")
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