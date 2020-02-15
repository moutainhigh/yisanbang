package com.vtmer.yisanbang.dto.insert;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@ApiModel
@Data
public class InsertBusinessAddressDTO {

    @NotBlank(message = "商家地址不能为空")
    @ApiModelProperty(value = "商家收货地址",example = "广州市白云区xxx",required = true)
    private String addressName;

    @NotBlank(message = "联系号码不能为空")
    @Pattern(regexp = "^1([34578])\\d{9}$",message = "手机号码格式不正确")
    @ApiModelProperty(value = "电话号码",example = "17666289644",required = true)
    private String phoneNumber;


    @NotBlank(message = "联系人姓名不能为空")
    @ApiModelProperty(value = "联系人姓名",example = "大老板",required = true)
    private String contactName;

    @ApiModelProperty(readOnly = true,example = "true",value = "是否是默认地址")
    private Boolean isDefault;
}
