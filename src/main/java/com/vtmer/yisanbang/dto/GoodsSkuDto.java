package com.vtmer.yisanbang.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class GoodsSkuDto {

    @NotNull(message = "colorSizeId is null")
    @ApiModelProperty(value = "颜色尺寸id",example = "!",required = true)
    private Integer colorSizeId;

    @NotNull(message = "isGoods is null")
    @ApiModelProperty(value = "是否是普通商品",example = "true",required = true)
    private Boolean isGoods;
}
