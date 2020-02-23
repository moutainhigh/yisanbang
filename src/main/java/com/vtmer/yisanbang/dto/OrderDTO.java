package com.vtmer.yisanbang.dto;

import com.vtmer.yisanbang.common.valid.group.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Data
@ApiModel
public class OrderDTO extends CreateOrderDTO {

    @ApiModelProperty(value = "优惠后总价",name = "totalPrice",required = true,example = "250")
    private Double totalPrice;

    @ApiModelProperty(value = "优惠前总价",readOnly = true,example = "520")
    private Double beforeTotalPrice;

    @ApiModelProperty(value = "订单商品列表",name = "cartGoodsList",required = true)
    private List<OrderGoodsDTO> orderGoodsDTOList;

    // 订单编号
    @NotBlank(groups = {Update.class},message = "订单编号为空")
    @ApiModelProperty(readOnly = true,value = "订单编号",example = "12345678998765432110")
    private String orderNumber;

    @ApiModelProperty(readOnly = true,example = "2",value = "退款状态",notes = "退款状态定义：status 退款状态 0--等待商家处理  1--退款中（待买家发货） 2--退款中（待商家收货） 3--退款成功 4--退款失败")
    private Integer refundStatus;

    @ApiModelProperty(readOnly = true,value = "订单创建时间")
    private Date createTime;
}
