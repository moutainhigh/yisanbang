package com.vtmer.yisanbang.dto;

import com.vtmer.yisanbang.common.validGroup.Delete;
import com.vtmer.yisanbang.common.validGroup.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@ApiModel(value = "goods对象", description = "商品对象goods")
public class GoodsDto {
    @NotNull(groups = {Update.class, Delete.class}, message = "商品id不可为空")
    @ApiModelProperty(value = "商品id", example = "1")
    private Integer id;

    @NotNull(message = "分类id不可为空")
    @ApiModelProperty(value = "分类", example = "1")
    private Integer sortId;

    @NotBlank(message = "商品名称不可为空")
    @ApiModelProperty(value = "商品名称", example = "衣服")
    private String name;

    @NotBlank(message = "库存不可为空")
    @ApiModelProperty(value = "库存", example = "99")
    private String introduce;

    @NotBlank(message = "图片路径不可为空")
    @ApiModelProperty(value = "图片", example = "goods/****")
    private String picture;

    @NotNull(message = "价格不可为空")
    @ApiModelProperty(value = "价格", example = "100")
    private Double price;

    @NotNull(message = "显示标志不可为空")
    @ApiModelProperty(value = "显示标志", example = "true")
    private Boolean isShow;

    private Date updateTime;

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @ApiModelProperty(hidden = true)
    private Integer colorSizeId;

    @ApiModelProperty(hidden = true)
    private Integer isGoods;

    public Integer getColorSizeId() {
        return colorSizeId;
    }

    public void setColorSizeId(Integer colorSizeId) {
        this.colorSizeId = colorSizeId;
    }

    public Integer getIsGoods() {
        return isGoods;
    }

    public void setIsGoods(Integer isGoods) {
        this.isGoods = isGoods;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSortId() {
        return sortId;
    }

    public void setSortId(Integer sortId) {
        this.sortId = sortId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce == null ? null : introduce.trim();
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture == null ? null : picture.trim();
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean getIsShow() {
        return isShow;
    }

    public void setIsShow(Boolean isShow) {
        this.isShow = isShow;
    }

    public GoodsDto(Integer id, Integer sortId, String name, String introduce, String picture, Double price, Boolean isShow) {
        this.id = id;
        this.sortId = sortId;
        this.name = name;
        this.introduce = introduce;
        this.picture = picture;
        this.price = price;
        this.isShow = isShow;
    }

    @Override
    public String toString() {
        return "GoodsDto{" +
                "id=" + id +
                ", sortId=" + sortId +
                ", name='" + name + '\'' +
                ", introduce='" + introduce + '\'' +
                ", picture='" + picture + '\'' +
                ", price=" + price +
                ", isShow=" + isShow +
                '}';
    }
}
