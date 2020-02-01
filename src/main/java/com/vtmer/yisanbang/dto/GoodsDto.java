package com.vtmer.yisanbang.dto;

public class GoodsDto {
    private Integer id;

    private Integer sortId;

    private String name;

    private String introduce;

    private String picture;

    private Double price;

    private Boolean isShow;

    private Integer colorSizeId;

    private Integer isGoods;

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

    public GoodsDto(Integer id, Integer sortId, String name, String introduce, String picture, Double price, Boolean isShow) {
        this.id = id;
        this.sortId = sortId;
        this.name = name;
        this.introduce = introduce;
        this.picture = picture;
        this.price = price;
        this.isShow = isShow;
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
                '}';
    }
}
