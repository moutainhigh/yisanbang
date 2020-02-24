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
public class OrderGoodsDTO {

    @ApiModelProperty(value = "商品id",example = "2",readOnly = true)
    private Integer id;

    @ApiModelProperty(value = "订单id",example = "1",readOnly = true)
    private Integer orderId;

    @NotNull(message = "colorSizeId is null")
    @ApiModelProperty(value = "颜色尺寸id",example = "2",required = true)
    private Integer colorSizeId;

    @NotNull(message = "whetherGoods is null")
    @ApiModelProperty(value = "是否是普通商品",notes = "1代表是，0代表否",example = "false",required = true)
    private Boolean whetherGoods;

    @NotNull(message = "amount is null")
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
    private Double price;

    @ApiModelProperty(value = "部件名称或者颜色",readOnly = true,example = "上衣or黑色")
    private String partOrColor;

    @ApiModelProperty(value = "商品尺寸",readOnly = true,example = "XL")
    private String size;

    @ApiModelProperty(name ="afterTotalPrice",value = "单项商品优惠后的总价",readOnly = true,example = "12800")
    private double afterTotalPrice;
}
