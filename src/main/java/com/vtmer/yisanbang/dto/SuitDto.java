package com.vtmer.yisanbang.dto;

public class SuitDto {
    private Integer id;

    private Integer sortId;

    private String introduce;

    private Double lowestPrice;

    private Double highestPrice;

    private Boolean isShow;

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

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public Double getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(Double lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public Double getHighestPrice() {
        return highestPrice;
    }

    public void setHighestPrice(Double highestPrice) {
        this.highestPrice = highestPrice;
    }

    public Boolean getShow() {
        return isShow;
    }

    public void setShow(Boolean show) {
        isShow = show;
    }

    @Override
    public String toString() {
        return "SuitDto{" +
                "id=" + id +
                ", sortId=" + sortId +
                ", introduce='" + introduce + '\'' +
                ", lowestPrice=" + lowestPrice +
                ", highestPrice=" + highestPrice +
                ", isShow=" + isShow +
                ", name='" + name + '\'' +
                ", picture='" + picture + '\'' +
                ", price=" + price +
                ", part='" + part + '\'' +
                ", size='" + size + '\'' +
                '}';
    }

    public SuitDto(Integer id, Integer sortId, String introduce, Double lowestPrice, Double highestPrice, Boolean isShow, String name, String picture, double price, String part, String size) {
        this.id = id;
        this.sortId = sortId;
        this.introduce = introduce;
        this.lowestPrice = lowestPrice;
        this.highestPrice = highestPrice;
        this.isShow = isShow;
        this.name = name;
        this.picture = picture;
        this.price = price;
        this.part = part;
        this.size = size;
    }
}
