package com.vtmer.yisanbang.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CarouselDTO {

    private Integer id;

    @ApiModelProperty(value = "图片路径", required = true, example = "img.yisanbang.com/carousel/图片名称")
    @NotBlank(message = "图片路径不能为空")
    private String picturePath;

    @ApiModelProperty(value = "点击轮播图后跳转的路径", example = "goods/selectGoodsById/1")
    private String url;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
