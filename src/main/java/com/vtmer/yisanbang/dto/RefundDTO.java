package com.vtmer.yisanbang.dto;

import com.vtmer.yisanbang.domain.Refund;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import java.util.List;

@Data
@ApiModel
public class RefundDTO {

    @Valid
    @ApiModelProperty(value = "退款基础信息")
    private Refund refund;

    // 退款商品列表
    @Valid
    @ApiModelProperty(value = "退款商品sku")
    private List<GoodsSkuDTO> RefundGoodsList;


}
