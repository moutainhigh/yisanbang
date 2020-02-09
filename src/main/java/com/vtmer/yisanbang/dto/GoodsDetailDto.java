package com.vtmer.yisanbang.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "goodsDetail对象", description = "商品对象goodsDetail")
public class GoodsDetailDto {
    @ApiModelProperty(value = "商品详情id", example = "1")
    private Integer id;

    @ApiModelProperty(value = "商品id", example = "1")
    private Integer goodsId;

    @ApiModelProperty(value = "商品详情图片", example = "goodsDetail/****")
    private String pirtucePath;

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

    public GoodsDetailDto(Integer id, Integer goodsId, String pirtucePath, Integer showOrder) {
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
}
