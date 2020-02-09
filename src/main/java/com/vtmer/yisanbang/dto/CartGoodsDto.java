package com.vtmer.yisanbang.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel
public class CartGoodsDto {

    // 商品id或套装id,返回时使用
    @ApiModelProperty(readOnly = true,value = "商品或套装id",example = "1")
    private Integer id;

    @ApiModelProperty(name = "colorSizeId",value = "颜色尺寸id",required = true,example = "1")
    private Integer colorSizeId;

    @ApiModelProperty(name = "amount",value = "商品数量",required = true,example = "2")
    private Integer amount;

    @ApiModelProperty(readOnly = true,value = "是否勾选",example = "true")
    private Boolean isChosen;

    @ApiModelProperty(readOnly = true,value = "商品名称",example = "职业装")
    private String name;

    @ApiModelProperty(readOnly = true,value = "商品图片路径",example = "xxxUrl")
    private String picture;

    @ApiModelProperty(readOnly = true,value = "商品单价",example = "66")
    private double price;

    @ApiModelProperty(readOnly = true,value = "部件或颜色",example = "上衣or黑色")
    private String partOrColor;

    @ApiModelProperty(readOnly = true,value = "商品尺寸",example = "XL")
    private String size;

    @ApiModelProperty(readOnly = true,value = "单项商品总价",example = "132")
    private double totalPrice;

    @ApiModelProperty(name ="afterTotalPrice",value = "单项商品优惠后的总价",required = true,example = "100")
    private double afterTotalPrice;

    @ApiModelProperty(name = "isGoods",value = "是否是普通商品",required = true,example = "true")
    private Boolean isGoods;

    @ApiModelProperty(hidden = true)
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getColorSizeId() {
        return colorSizeId;
    }

    public void setColorSizeId(Integer colorSizeId) {
        this.colorSizeId = colorSizeId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
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

    public String getPartOrColor() {
        return partOrColor;
    }

    public void setPartOrColor(String partOrColor) {
        this.partOrColor = partOrColor;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Boolean getIsChosen() {
        return isChosen;
    }

    public void setIsChosen(Boolean chosen) {
        isChosen = chosen;
    }

    public Boolean getIsGoods() {
        return isGoods;
    }

    public void setIsGoods(Boolean goods) {
        isGoods = goods;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getAfterTotalPrice() {
        return afterTotalPrice;
    }

    public void setAfterTotalPrice(double afterTotalPrice) {
        this.afterTotalPrice = afterTotalPrice;
    }

    @Override
    public String toString() {
        return "CartGoodsDto{" +
                "colorSizeId=" + colorSizeId +
                ", amount=" + amount +
                ", isChosen=" + isChosen +
                ", name='" + name + '\'' +
                ", picture='" + picture + '\'' +
                ", price=" + price +
                ", partOrColor='" + partOrColor + '\'' +
                ", size=" + size +
                ", isGoods=" + isGoods +
                ", updateTime=" + updateTime +
                '}';
    }
}
