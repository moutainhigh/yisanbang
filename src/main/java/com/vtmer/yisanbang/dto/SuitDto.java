package com.vtmer.yisanbang.dto;

public class SuitDto {

    private String name;

    private String picture;

    private double price;

    private String part;

    private String size;

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

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "SuitDto{" +
                "name='" + name + '\'' +
                ", picture='" + picture + '\'' +
                ", price=" + price +
                ", part='" + part + '\'' +
                ", size=" + size +
                '}';
    }
}
