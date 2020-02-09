package com.vtmer.yisanbang.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Date;
@ApiModel(value = "goods对象", description = "商品对象goods")
public class GoodsDto {
    @ApiModelProperty(value = "商品id", example = "1")
    private Integer id;

    @ApiModelProperty(value = "分类id", example = "1")
    private Integer sortId;

    @ApiModelProperty(value = "商品名称", example = "舒适短袖")
    private String name;

    @ApiModelProperty(value = "商品简介", example = "服务承诺 正品保证 极速退款 七天无理由退换")
    private String introduce;

    @ApiModelProperty(value = "商品图片", example = "goods/****")
    private String picture;

    @ApiModelProperty(value = "价格", example = "99")
    private Double price;

    @ApiModelProperty(value = "是否显示", example = "true")
    private Boolean isShow;

    @NotNull(message = "colorSizeId is null")
    @ApiModelProperty(hidden = true)
    private Integer colorSizeId;

    @NotNull(message = "isGoods is null")
    @ApiModelProperty(hidden = true)
    private Integer isGoods;

    private Date updateTime;

    public Integer getColorSizeId() {
        return colorSizeId;
    }

    public void setColorSizeId(Integer colorSizeId) {
        this.colorSizeId = colorSizeId;
    }

    public Integer getIsGoods() {
        return isGoods;
    }

    public void setIsGoods(Integer isGoods) {
        this.isGoods = isGoods;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSortId() {
        return sortId;
    }

    public void setSortId(Integer sortId) {
        this.sortId = sortId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce == null ? null : introduce.trim();
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture == null ? null : picture.trim();
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean getIsShow() {
        return isShow;
    }

    public void setIsShow(Boolean isShow) {
        this.isShow = isShow;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public GoodsDto(Integer id, Integer sortId, String name, String introduce, String picture, Double price, Boolean isShow, Integer colorSizeId, Integer isGoods, Date updateTime) {
        this.id = id;
        this.sortId = sortId;
        this.name = name;
        this.introduce = introduce;
        this.picture = picture;
        this.price = price;
        this.isShow = isShow;
        this.colorSizeId = colorSizeId;
        this.isGoods = isGoods;
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "GoodsDto{" +
                "id=" + id +
                ", sortId=" + sortId +
                ", name='" + name + '\'' +
                ", introduce='" + introduce + '\'' +
                ", picture='" + picture + '\'' +
                ", price=" + price +
                ", isShow=" + isShow +
                ", colorSizeId=" + colorSizeId +
                ", isGoods=" + isGoods +
                ", updateTime=" + updateTime +
                '}';
    }
}
