package com.vtmer.yisanbang.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ApiModel
@Data
public class DeliverGoodsDTO {

    @ApiModelProperty(value = "订单编号",example = "12345678998765432110")
    @NotBlank(message = "订单编号为空")
    private String orderNumber;

    @ApiModelProperty(value = "快递编号",required = true,example = "12345678998765432110")
    @NotBlank(message = "快递编号为空")
    private String courierNumber;
}
