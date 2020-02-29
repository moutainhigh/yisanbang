package com.vtmer.yisanbang.dto;

import com.vtmer.yisanbang.common.valid.group.Insert;
import com.vtmer.yisanbang.common.valid.group.Update;
import com.vtmer.yisanbang.domain.UserAddress;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel
public class OrderDTO {

    @Valid
    @NotNull(message = "用户地址信息为空",groups = {Insert.class, Update.class})
    @ApiModelProperty(value = "用户默认收货地址信息",required = true,example = "广东工业大学教学三号楼创客基地E102")
    private UserAddress userAddress;

    @NotNull(message = "优惠后总价为空")
    @ApiModelProperty(value = "优惠后总价",name = "totalPrice",required = true,example = "250")
    private Double totalPrice;

    @ApiModelProperty(value = "订单商品列表",name = "cartGoodsList",required = true,notes = "创建订单时只需要标识为true的属性，以及数量")
    private List<OrderGoodsDTO> orderGoodsDTOList;

    // 留言
    @NotNull(message = "订单留言为空")
    @ApiModelProperty(value = "订单留言",required = true,example = "不要发货，我钱多",notes = "若订单留言为空传空字符串")
    private String message;

    @NotNull(groups = {Insert.class},message = "邮费金额为空")
    @ApiModelProperty(value = "邮费",example = "8",notes = "提交订单时需要传邮费")
    private Double postage;

}
