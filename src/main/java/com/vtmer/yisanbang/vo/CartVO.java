package com.vtmer.yisanbang.vo;

import com.vtmer.yisanbang.dto.CartGoodsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel
@Data
public class CartVO {

    @ApiModelProperty(value = "优惠后总价",name = "totalPrice",required = true,example = "520")
    private double totalPrice;

    @ApiModelProperty(value = "优惠前总价",example = "250")
    private double beforeTotalPrice;

    @ApiModelProperty(value = "商品列表",name = "cartGoodsList",required = true)
    private List<CartGoodsDTO> cartGoodsList;

}
