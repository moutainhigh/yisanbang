package com.vtmer.yisanbang.dto;

public class PartSizeDto {
    private Integer id;

    private Integer suitId;

    private Double price;

    private String part;

    private String size;

    private Integer inventory;

    private String model;

    public PartSizeDto(Integer id, Integer suitId, Double price, String part, String size, Integer inventory, String model) {
        this.id = id;
        this.suitId = suitId;
        this.price = price;
        this.part = part;
        this.size = size;
        this.inventory = inventory;
        this.model = model;
    }

    @Override
    public String toString() {
        return "PartSizeDto{" +
                "id=" + id +
                ", suitId=" + suitId +
                ", price=" + price +
                ", part='" + part + '\'' +
                ", size='" + size + '\'' +
                ", inventory=" + inventory +
                ", model='" + model + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSuitId() {
        return suitId;
    }

    public void setSuitId(Integer suitId) {
        this.suitId = suitId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
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
        this.model = model;
    }
}
