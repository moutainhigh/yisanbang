package com.vtmer.yisanbang.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class CollectionVO {

    @ApiModelProperty(value = "商品id",example = "2")
    private Integer goodsId;

    @ApiModelProperty(value = "是否是普通商品",example = "true")
    private Boolean isGoods;

    @ApiModelProperty(value = "商品名称",example = "职业装")
    private String name;

    @ApiModelProperty(value = "商品图片url",example = "xxxUrl")
    private String picture;

    @ApiModelProperty(value = "商品价格",example = "88")
    private double price;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "CollectionDto{" +
                "goodsId=" + goodsId +
                ", isGoods=" + isGoods +
                ", name='" + name + '\'' +
                ", picture='" + picture + '\'' +
                ", price=" + price +
                '}';
    }
}
