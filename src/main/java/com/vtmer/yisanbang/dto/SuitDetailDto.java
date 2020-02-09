package com.vtmer.yisanbang.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "suitDetail对象", description = "套装详情对象suitDetail")
public class SuitDetailDto {
    @ApiModelProperty(value = "套装详情id", example = "1")
    private Integer id;

    @ApiModelProperty(value = "套装id", example = "1")
    private Integer suitId;

    @ApiModelProperty(value = "图片路径", example = "suitDetail/****")
    private String pirtucePath;

    @ApiModelProperty(value = "显示顺序", example = "2")
    private Integer showOrder;

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

    public String getPirtucePath() {
        return pirtucePath;
    }

    public void setPirtucePath(String pirtucePath) {
        this.pirtucePath = pirtucePath;
    }

    public Integer getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }

    @Override
    public String toString() {
        return "SuitDetailDto{" +
                "id=" + id +
                ", suitId=" + suitId +
                ", pirtucePath='" + pirtucePath + '\'' +
                ", showOrder=" + showOrder +
                '}';
    }

    public SuitDetailDto(Integer id, Integer suitId, String pirtucePath, Integer showOrder) {
        this.id = id;
        this.suitId = suitId;
        this.pirtucePath = pirtucePath;
        this.showOrder = showOrder;
    }
}
