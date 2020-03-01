package com.vtmer.yisanbang.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class CollectionVO {

    @ApiModelProperty(value = "商品id",example = "2")
    private Integer goodsId;

    @ApiModelProperty(value = "是否是普通商品",example = "true")
    private Boolean whetherGoods;

    @ApiModelProperty(value = "商品名称",example = "职业装")
    private String name;

    @ApiModelProperty(value = "商品图片url",example = "xxxUrl")
    private String picture;

    @ApiModelProperty(value = "商品价格",example = "88")
    private double price;

    @ApiModelProperty(value = "商品简介",example = "KOREAN WOMAN‘S")
    private String introduce;

}
