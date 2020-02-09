package com.vtmer.yisanbang.vo;

import com.vtmer.yisanbang.dto.CartGoodsDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel
public class CartVo {

    @ApiModelProperty(value = "购物车id",readOnly = true,example = "1")
    private Integer cartId;

    @ApiModelProperty(value = "优惠后总价",name = "totalPrice",required = true,example = "250")
    private double totalPrice;

    @ApiModelProperty(value = "优惠前总价",readOnly = true,example = "520")
    private double beforeTotalPrice;

    @ApiModelProperty(value = "预下单的购物车商品列表",name = "cartGoodsList",required = true)
    private List<CartGoodsDto> cartGoodsList;

    public List<CartGoodsDto> getCartGoodsList() {
        return cartGoodsList;
    }

    public void setCartGoodsList(List<CartGoodsDto> cartGoodsList) {
        this.cartGoodsList = cartGoodsList;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getBeforeTotalPrice() {
        return beforeTotalPrice;
    }

    public void setBeforeTotalPrice(double beforeTotalPrice) {
        this.beforeTotalPrice = beforeTotalPrice;
    }

    @Override
    public String toString() {
        return "CartVo{" +
                "cartId=" + cartId +
                ", totalPrice=" + totalPrice +
                ", beforeTotalPrice=" + beforeTotalPrice +
                ", cartGoodsList=" + cartGoodsList +
                '}';
    }
}
