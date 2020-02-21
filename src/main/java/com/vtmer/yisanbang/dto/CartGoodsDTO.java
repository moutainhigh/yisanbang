package com.vtmer.yisanbang.dto;

import com.vtmer.yisanbang.common.valid.group.Insert;
import com.vtmer.yisanbang.common.valid.group.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@ApiModel
@Data
public class CartGoodsDTO {

    @ApiModelProperty(value = "商品id",example = "2",readOnly = true)
    private Integer id;

    @ApiModelProperty(value = "用户id",example = "1",readOnly = true)
    private Integer userId;

    @NotNull(groups = {Insert.class, Update.class},message = "colorSizeId is null")
    @ApiModelProperty(value = "颜色尺寸id",example = "2",required = true)
    private Integer colorSizeId;

    @NotNull(groups = {Insert.class,Update.class})
    @ApiModelProperty(value = "是否是普通商品",notes = "1代表是，0代表否",example = "false",required = true)
    private Boolean whetherGoods;

    @NotNull(groups = {Insert.class,Update.class},message = "amount is null")
    @Min(value = -1,groups = {Update.class},message = "修改最小值只能为-1")
    @Max(value = 1,groups = {Update.class},message = "修改最大值只能为1")
    @Min(value = 1,groups = {Insert.class},message = "修改的最小值只能为1")
    @ApiModelProperty(value = "商品数量",example = "6")
    private Integer amount;

    @ApiModelProperty(hidden = true,value = "更新时间",example = "147515314")
    private long updateTime;

    @ApiModelProperty(readOnly = true,value = "是否勾选",example = "true")
    private Boolean whetherChosen = true;

    @ApiModelProperty(readOnly = true,value = "商品标题",example = "经典两粒扣男式西服套装")
    private String title;

    @ApiModelProperty(readOnly = true,value = "商品图片",example = "/imageUrl")
    private String picture;

    @ApiModelProperty(value = "加入购物车的价格",readOnly = true,example = "1280")
    private double price;

    @ApiModelProperty(value = "部件名称或者颜色",readOnly = true,example = "上衣or黑色")
    private String partOrColor;

    @ApiModelProperty(value = "商品尺寸",readOnly = true,example = "XL")
    private String size;

    @ApiModelProperty(name ="afterTotalPrice",value = "单项商品优惠后的总价",readOnly = true,example = "100")
    private double afterTotalPrice;

    @ApiModelProperty(name ="totalPrice",value = "单项商品总价",readOnly = true,example = "100")
    private double totalPrice;

    public CartGoodsDTO() {

    }

    public CartGoodsDTO(Integer colorSizeId, Boolean whetherGoods, Integer cartId) {
        this.colorSizeId = colorSizeId;
        this.whetherGoods = whetherGoods;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getColorSizeId() {
        return colorSizeId;
    }

    public void setColorSizeId(Integer colorSizeId) {
        this.colorSizeId = colorSizeId;
    }

    public Boolean getWhetherGoods() {
        return whetherGoods;
    }

    public void setWhetherGoods(Boolean whetherGoods) {
        this.whetherGoods = whetherGoods;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean getWhetherChosen() {
        return whetherChosen;
    }

    public void setWhetherChosen(Boolean whetherChosen) {
        this.whetherChosen = whetherChosen;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPartOrColor() {
        return partOrColor;
    }

    public void setPartOrColor(String partOrColor) {
        this.partOrColor = partOrColor;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public double getAfterTotalPrice() {
        return afterTotalPrice;
    }

    public void setAfterTotalPrice(double afterTotalPrice) {
        this.afterTotalPrice = afterTotalPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
