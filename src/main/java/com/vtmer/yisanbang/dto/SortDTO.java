package com.vtmer.yisanbang.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class SortDTO {

    private Integer id;

    @ApiModelProperty(value = "分类名称", required = true, example = "男装")
    @NotBlank(message = "分类名称不能为空")
    private String name;

    @ApiModelProperty(value = "分类属性(校服/职业装), 0: 校服, 1: 职业装", example = "0")
    private Boolean isSuit;

    @ApiModelProperty(value = "上级分类id, 若为一级分类(无上级分类)则为0", example = "1")
    private Integer parentId;

    @ApiModelProperty(value = "分类等级, 0: 一级分类, 1: 二级分类", example = "1")
    private Integer level;

    @ApiModelProperty(value = "分类显示顺序(最小排序为1)", required = true, example = "1")
    @NotNull(message = "分类显示顺序不能为空")
    @Min(value = 1, message = "排序最小为1")
    private Integer showOrder;

    @ApiModelProperty(value = "分类是否显示, 0: 不显示, 1: 显示", example = "1")
    private Boolean isShow;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsSuit() {
        return isSuit;
    }

    public void setIsSuit(Boolean suit) {
        isSuit = suit;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }

    public Boolean getIsShow() {
        return isShow;
    }

    public void setIsShow(Boolean show) {
        isShow = show;
    }

}
