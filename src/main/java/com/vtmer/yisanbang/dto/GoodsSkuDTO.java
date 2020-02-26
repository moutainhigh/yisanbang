package com.vtmer.yisanbang.dto;

import com.vtmer.yisanbang.common.valid.group.Insert;
import com.vtmer.yisanbang.common.valid.group.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class GoodsSkuDTO {

    @NotNull(message = "colorSizeId is null")
    @ApiModelProperty(value = "颜色尺寸id",example = "1",required = true)
    private Integer colorSizeId;

    @NotNull(message = "isGoods is null")
    @ApiModelProperty(value = "是否是普通商品",notes = "商品类别分套装和单件，1代表是普通商品，0代表套装",example = "true",required = true)
    private Boolean whetherGoods;

    @NotNull(message = "amount is null")
    @Min(value = -1,groups = {Update.class},message = "修改最小值只能为-1")
    @Max(value = 1,groups = {Update.class},message = "修改最大值只能为1")
    @Min(value = 1,groups = {Insert.class},message = "修改的最小值只能为1")
    @ApiModelProperty(value = "商品数量",example = "6")
    private Integer amount;

}
