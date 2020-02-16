package com.vtmer.yisanbang.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
@ApiModel
@Data
public class AgreeRefundDTO {
    // 退款编号
    @NotBlank(message = "refundNumber is null")
    @ApiModelProperty(value = "退款编号",example = "12345678998765432110",required = true)
    private String refundNumber;

    // 订单编号
    @ApiModelProperty(value = "订单编号", example = "12345678998765432110", readOnly = true)
    private String orderNumber;

    // 订单总金额
    @ApiModelProperty(value = "订单总金额",example = "520",readOnly = true)
    private String totalFee;

    // 退款金额
    @ApiModelProperty(value = "退款金额",example = "250",readOnly = true)
    private String refundFee;

    /**
     * 退款原因：非必须
     * 若商户传入，会在下发给用户的退款消息中体现退款原因
     * 注意：若订单退款金额≤1元，且属于部分退款，则不会在退款消息中体现退款原因
     */
    @ApiModelProperty(value = "退款原因(非必需),若商户传入，会在下发给用户的退款消息中体现退款原因",example = "12345678998765432110")
    private String refundDesc;
}
