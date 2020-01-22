package com.vtmer.yisanbang.dto;

import java.util.Date;

public class GoodsDto {

    private String name;

    private String introduce;

    private String picture;

    private Double price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "GoodsDto{" +
                "name='" + name + '\'' +
                ", introduce='" + introduce + '\'' +
                ", picture='" + picture + '\'' +
                ", price=" + price +
                '}';
    }
}
