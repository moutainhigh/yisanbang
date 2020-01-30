package com.vtmer.yisanbang.dto;

import java.util.Date;

public class CartGoodsDto {

    private Integer colorSizeId;

    private Integer amount;

    private Byte isChosen;

    private String name;

    private String picture;

    private double price;

    private String partOrColor;

    private Integer size;

    private double totalPrice;

    private double afterTotalPrice;

    private Integer isGoods;

    private Date updateTime;

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

    public Byte getIsChosen() {
        return isChosen;
    }

    public void setIsChosen(Byte isChosen) {
        this.isChosen = isChosen;
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

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getIsGoods() {
        return isGoods;
    }

    public void setIsGoods(Integer isGoods) {
        this.isGoods = isGoods;
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
