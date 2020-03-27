package com.vtmer.yisanbang.dto.update;

import com.vtmer.yisanbang.dto.insert.InsertCollectionDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class UpdateCollectionDTO extends InsertCollectionDTO {

    @NotNull(message = "收藏夹商品id为空")
    @ApiModelProperty(value = "收藏夹商品id",required = true,example = "2")
    private Integer id;
}
