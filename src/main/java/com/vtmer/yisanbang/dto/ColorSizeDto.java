package com.vtmer.yisanbang.dto;

import com.vtmer.yisanbang.common.validGroup.Delete;
import com.vtmer.yisanbang.common.validGroup.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

@ApiModel(value = "colorSize对象", description = "颜色尺寸对象colorSize")
public class ColorSizeDto {
    @NotNull(groups = {Update.class, Delete.class}, message = "颜色尺寸id不可为空")
    @ApiModelProperty(value = "颜色尺寸id", example = "1")
    private Integer id;

    @NotNull(message = "商品id不可为空")
    @ApiModelProperty(value = "商品id", example = "1")
    private Integer goodsId;

    @NotNull(message = "颜色不可为空")
    @ApiModelProperty(value = "颜色", example = "1")
    private String color;

    @NotNull(message = "尺寸不可为空")
    @ApiModelProperty(value = "尺寸", example = "1")
    private String size;

    @NotNull(message = "库存不可为空")
    @ApiModelProperty(value = "库存", example = "1")
    private Integer inventory;

    @NotNull(message = "型号不可为空")
    @ApiModelProperty(value = "型号", example = "1")
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
