package com.vtmer.yisanbang.dto;

import java.util.Date;

public class CartGoodsDto {

    // 商品id或套装id
    private Integer id;

    private Integer colorSizeId;

    private Integer amount;

    private Boolean isChosen;

    private String name;

    private String picture;

    private double price;

    private String partOrColor;

    private String size;

    private double totalPrice;

    private double afterTotalPrice;

    private Boolean isGoods;

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
