package com.vtmer.yisanbang.dto;

import com.vtmer.yisanbang.common.validGroup.Delete;
import com.vtmer.yisanbang.common.validGroup.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel(value = "partSize对象", description = "部件尺寸对象partSize")
public class PartSizeDto {
    @NotNull(groups = {Update.class, Delete.class}, message = "部件尺寸id不可为空")
    @ApiModelProperty(value = "部件尺寸id", example = "1")
    private Integer id;

    @NotNull(message = "套装id不可为空")
    @ApiModelProperty(value = "套装id", example = "1")
    private Integer suitId;

    @NotNull(message = "价格不可为空")
    @ApiModelProperty(value = "价格", example = "999")
    private Double price;

    @NotBlank(message = "部件不可为空")
    @ApiModelProperty(value = "部件", example = "上衣")
    private String part;

    @NotBlank(message = "大小不可为空")
    @ApiModelProperty(value = "大小", example = "S")
    private String size;

    @NotNull(message = "库存不可为空")
    @ApiModelProperty(value = "库存", example = "99")
    private Integer inventory;

    @NotBlank(message = "型号不可为空")
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
