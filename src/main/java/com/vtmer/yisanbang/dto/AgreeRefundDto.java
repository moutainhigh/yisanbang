package com.vtmer.yisanbang.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 同意退款申请需要的数据
 */
@Data
public class AgreeRefundDto {

    // 退款编号
    @NotBlank(message = "refundNumber is null")
    private String refundNumber;

    // 订单编号
    private String orderNumber;

    // 订单总金额
    private String totalFee;

    // 退款金额
    private String refundFee;

    /**
     * 退款原因：非必须
     * 若商户传入，会在下发给用户的退款消息中体现退款原因
     * 注意：若订单退款金额≤1元，且属于部分退款，则不会在退款消息中体现退款原因
     */
    private String refundDesc;
}
