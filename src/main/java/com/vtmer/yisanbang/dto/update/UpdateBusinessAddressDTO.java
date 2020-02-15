package com.vtmer.yisanbang.dto.update;

import com.vtmer.yisanbang.dto.insert.InsertBusinessAddressDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel
@Data
public class UpdateBusinessAddressDTO extends InsertBusinessAddressDTO {

    @NotNull(message = "商家收货地址id为空")
    @ApiModelProperty(value = "商家收货地址id",example = "3",required = true)
    private Integer id;

}