package com.vtmer.yisanbang.dto.update;

import com.vtmer.yisanbang.dto.insert.InsertBusinessAddressDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
public class UpdateBusinessAddressDTO extends InsertBusinessAddressDTO {

    @NotNull(message = "商家收货地址id为空")
    @ApiModelProperty(value = "商家收货地址id",example = "3",required = true)
    private Integer id;

    @ApiModelProperty(example = "true",value = "是否是默认地址")
    private Boolean whetherDefault;

}
