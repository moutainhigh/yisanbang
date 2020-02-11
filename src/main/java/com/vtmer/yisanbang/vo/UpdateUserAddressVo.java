package com.vtmer.yisanbang.vo;

import com.vtmer.yisanbang.domain.UserAddress;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class UpdateUserAddressVo {

    @ApiModelProperty(name = "userAddress",value = "用户地址信息",required = true)
    @Valid
    @NotNull(message = "用户地址信息为空")
    private UserAddress userAddress;

    @ApiModelProperty(name = "orderNumber",value = "订单编号",required = true,example = "12345678998765432110")
    @NotBlank(message = "订单编号为空")
    private String orderNumber;

    public UserAddress getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(UserAddress userAddress) {
        this.userAddress = userAddress;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
}
