package com.vtmer.yisanbang.dto;

import com.vtmer.yisanbang.common.valid.group.Delete;
import com.vtmer.yisanbang.common.valid.group.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel(value = "goodsDetail对象", description = "商品对象goodsDetail")
public class GoodsDetailDTO {
    @NotNull(groups = {Update.class, Delete.class}, message = "商品详情id不可为空")
    @ApiModelProperty(value = "商品详情id", example = "1")
    private Integer id;

    @NotNull(message = "商品id不可为空")
    @ApiModelProperty(value = "商品id", example = "1")
    private Integer goodsId;

    @NotBlank(message = "商品详情图片不可为空")
    @ApiModelProperty(value = "商品详情图片", example = "goodsDetail/****")
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

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getPirtucePath() {
        return pirtucePath;
    }

    public void setPirtucePath(String pirtucePath) {
        this.pirtucePath = pirtucePath == null ? null : pirtucePath.trim();
    }

    public Integer getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }

    public GoodsDetailDTO(Integer id, Integer goodsId, String pirtucePath, Integer showOrder) {
        this.id = id;
        this.goodsId = goodsId;
        this.pirtucePath = pirtucePath;
        this.showOrder = showOrder;
    }

    @Override
    public String toString() {
        return "GoodsDetailDto{" +
                "id=" + id +
                ", goodsId=" + goodsId +
                ", pirtucePath='" + pirtucePath + '\'' +
                ", showOrder=" + showOrder +
                '}';
    }

    public GoodsDetailDTO() {
    }
}
