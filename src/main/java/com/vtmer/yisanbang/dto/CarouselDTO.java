package com.vtmer.yisanbang.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CarouselDTO {

    private Integer id;

    @ApiModelProperty(value = "图片路径", required = true, example = "img.yisabang.com/carousel/图片名称")
    @NotBlank(message = "图片路径不能为空")
    private String picturePath;

    @ApiModelProperty(value = "跳转商品类型，0：不跳转(静态轮播图)，1：单件商品，2：套装商品", required = true, example = "1")
    @NotNull(message = "跳转商品类型不能为空(请选择单件/套装/不跳转)")
    private Integer goodsType;

    @ApiModelProperty(value = "单件商品id/套装商品id/0(若跳转商品类型为\"不跳转\"则goodsId应为0)", example = "5")
    private Integer goodsId;

    @ApiModelProperty(value = "轮播图显示顺序", required = true, example = "1")
    @NotNull(message = "轮播图显示顺序不能为空")
    private Integer showOrder;

    @ApiModelProperty(value = "轮播图是否在首页显示")
    private boolean isShow;

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public Integer getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(Integer goodsType) {
        this.goodsType = goodsType;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }

    public boolean getIsShow() {
        return isShow;
    }

    public void setIsShow(boolean show) {
        isShow = show;
    }

}
