package com.vtmer.yisanbang.dto;

import com.vtmer.yisanbang.domain.UserAddress;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 下购物车订单DTO
 */
@Data
@ApiModel(value = "创建订单DTO")
public class CreateOrderDTO {

    @Valid
    @NotNull(message = "用户地址信息为空")
    @ApiModelProperty(value = "用户默认收货地址信息",required = true,example = "广东工业大学教学三号楼创客基地E102")
    private UserAddress userAddress;

    // 留言
    @ApiModelProperty(value = "订单留言",required = false,example = "不要发货，我钱多")
    private String message;

    @ApiModelProperty(readOnly = true,value = "邮费",example = "8")
    private Double postage;
}
