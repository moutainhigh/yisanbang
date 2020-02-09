package com.vtmer.yisanbang.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

public class AdDto {

    @ApiModelProperty(value = "广告id", example = "1")
    private Integer id;

    @ApiModelProperty(value = "图片路径", required = true, example = "img.yisanbang.com/ad/图片名称")
    @NotBlank(message = "图片路径不能为空")
    private String picturePath;

    @ApiModelProperty(value = "点击广告图片后的跳转路径", example = "/goods/selectGoodsById/1")
    private String url;

    @ApiModelProperty(value = "广告图片在首页的显示顺序", required = true, example = "1")
    @NotBlank(message = "显示顺序不能为空")
    private Integer showOrder;

    @ApiModelProperty(value = "广告图片是否在首页显示(1: 显示, 0: 不显示)", example = "0")
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
