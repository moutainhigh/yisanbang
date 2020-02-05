package com.vtmer.yisanbang.dto;

public class ColorSizeDto {
    private Integer id;

    private Integer goodsId;

    private String color;

    private String size;

    private Integer inventory;

    private String model;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color == null ? null : color.trim();
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size == null ? null : size.trim();
    }

    public Integer getInventory() {
        return inventory;
    }

    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model == null ? null : model.trim();
    }

    public ColorSizeDto(Integer id, Integer goodsId, String color, String size, Integer inventory, String model) {
        this.id = id;
        this.goodsId = goodsId;
        this.color = color;
        this.size = size;
        this.inventory = inventory;
        this.model = model;
    }

    @Override
    public String toString() {
        return "ColorSizeDto{" +
                "id=" + id +
                ", goodsId=" + goodsId +
                ", color='" + color + '\'' +
                ", size='" + size + '\'' +
                ", inventory=" + inventory +
                ", model='" + model + '\'' +
                '}';
    }
}
