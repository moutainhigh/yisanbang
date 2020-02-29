package com.vtmer.yisanbang.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@ApiModel
@Data
public class Refund {

    @ApiModelProperty(value = "退款id",example = "3")
    private Integer id;

    @ApiModelProperty(value = "退款编号",example = "12345678998765432110")
    private String refundNumber;

    @NotNull(message = "refundPrice is null")
    @ApiModelProperty(value = "退款金额",example = "123",required = true)
    private Double refundPrice;

    @ApiModelProperty(hidden = true)
    private Integer userId;

    @NotNull(message = "orderId is null")
    @ApiModelProperty(value = "订单id",example = "1")
    private Integer orderId;

    @ApiModelProperty(value = "退款原因",example = "我不想要了")
    private String reason;

    @ApiModelProperty(value = "退款状态",example = "2")
    private Integer status;

    @ApiModelProperty(hidden = true,value = "是否收到货")
    private Boolean whetherReceived;

    @ApiModelProperty(value = "退款单创建时间")
    private Date createTime;

    @ApiModelProperty(hidden = true)
    private Date updateTime;

}