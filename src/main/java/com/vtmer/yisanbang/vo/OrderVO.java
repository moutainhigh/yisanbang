package com.vtmer.yisanbang.vo;

import com.vtmer.yisanbang.common.valid.group.Update;
import com.vtmer.yisanbang.dto.OrderDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 下购物车订单DTO
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "创建订单DTO")
public class OrderVO extends OrderDTO {

    @ApiModelProperty(value = "订单id",example = "6")
    private Integer orderId;

    @ApiModelProperty(value = "优惠前总价",example = "520")
    private Double beforeTotalPrice;
    // 订单编号
    @NotBlank(groups = {Update.class},message = "订单编号为空")
    @ApiModelProperty(value = "订单编号",example = "12345678998765432110")
    private String orderNumber;

    @ApiModelProperty(example = "2",value = "退款状态",notes = "退款状态定义：status 退款状态 0--等待商家处理  1--退款中（待买家发货） 2--退款中（待商家收货） 3--退款成功 4--退款失败")
    private Integer refundStatus;

    @ApiModelProperty(value = "订单创建时间")
    private Date createTime;

    @ApiModelProperty(example = "2",value = "订单状态",notes = "订单状态定义：status 订单状态 0--待付款 1--待发货 2--待收货 3--已完成 4--交易关闭 5--所有订单")
    private Integer orderStatus;

    @ApiModelProperty(example = "1",value = "订单是否提醒发货",notes = "1代表用户提醒发货，0正常")
    private Boolean whetherRemind;

}
