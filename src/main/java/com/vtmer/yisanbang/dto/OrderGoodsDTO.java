package com.vtmer.yisanbang.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
public class OrderGoodsDTO extends GoodsSkuDTO{

    @ApiModelProperty(value = "商品id",example = "2")
    private Integer id;

    @ApiModelProperty(value = "订单id",example = "1")
    private Integer orderId;

    @ApiModelProperty(hidden = true,value = "更新时间",example = "147515314")
    private Long updateTime;

    @ApiModelProperty(value = "商品标题",example = "经典两粒扣男式西服套装")
    private String title;

    @ApiModelProperty(value = "商品图片",example = "/imageUrl")
    private String picture;

    @ApiModelProperty(value = "商品单价",example = "1280")
    private Double price;

    @ApiModelProperty(value = "部件名称或者颜色",example = "上衣or黑色")
    private String partOrColor;

    @ApiModelProperty(value = "商品尺寸",example = "XL")
    private String size;

    @ApiModelProperty(name ="afterTotalPrice",value = "单项商品优惠后的总价",example = "12800")
    private Double afterTotalPrice;
}
