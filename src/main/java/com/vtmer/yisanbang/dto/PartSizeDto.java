package com.vtmer.yisanbang.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "partSize对象", description = "部件尺寸对象partSize")
public class PartSizeDto {
    @ApiModelProperty(value = "部件尺寸id", example = "1")
    private Integer id;

    @ApiModelProperty(value = "套装id", example = "1")
    private Integer suitId;

    @ApiModelProperty(value = "价格", example = "999")
    private Double price;

    @ApiModelProperty(value = "部件", example = "上衣")
    private String part;

    @ApiModelProperty(value = "大小", example = "S")
    private String size;

    @ApiModelProperty(value = "库存", example = "99")
    private Integer inventory;

    @ApiModelProperty(value = "型号", example = "XM1213SD")
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
