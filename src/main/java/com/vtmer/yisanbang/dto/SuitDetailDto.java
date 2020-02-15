package com.vtmer.yisanbang.dto;

import com.vtmer.yisanbang.common.valid.group.Delete;
import com.vtmer.yisanbang.common.valid.group.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel(value = "suitDetail对象", description = "套装详情对象suitDetail")
public class SuitDetailDto {
    @NotNull(groups = {Update.class, Delete.class}, message = "套装详情id不可为空")
    @ApiModelProperty(value = "套装详情id", example = "1")
    private Integer id;

    @NotNull(message = "套装id不可为空")
    @ApiModelProperty(value = "套装id", example = "1")
    private Integer suitId;

    @NotBlank(message = "图片路径不可为空")
    @ApiModelProperty(value = "图片路径", example = "suitDetail/****")
    private String pirtucePath;

    @NotNull(message = "显示顺序不可为空")
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
