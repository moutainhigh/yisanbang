package com.vtmer.yisanbang.dto.insert;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
@Data
@ApiModel
public class InsertCollectionDTO {
    @NotNull(message = "goodsId is null")
    @ApiModelProperty(value = "商品id",example = "4",required = true)
    private Integer goodsId;

    @NotNull(message = "whetherGoods is null")
    @ApiModelProperty(value = "是否是普通商品",example = "true",required = true)
    private Boolean whetherGoods;
}
