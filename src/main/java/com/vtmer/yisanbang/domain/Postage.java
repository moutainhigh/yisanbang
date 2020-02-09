package com.vtmer.yisanbang.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Date;

@ApiModel
public class Postage {

    @ApiModelProperty(readOnly = true,example = "1",value = "邮费设置id")
    private Integer id;

    @NotNull(message = "price(达标金额) is null")
    @ApiModelProperty(required = true,example = "88",value = "满x包邮，达标金额")
    private Double price;

    @NotNull(message = "defaultPostage(默认邮费) is null")
    @ApiModelProperty(example = "12",required = true,value = "默认邮费")
    private Double defaultPostage;

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