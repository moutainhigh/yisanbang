package com.vtmer.yisanbang.vo;

import com.vtmer.yisanbang.dto.CartGoodsDto;

import java.util.List;

public class CartVo {

    private Integer cartId;

    private double totalPrice;

    private double beforeTotalPrice;

    private List<CartGoodsDto> cartGoodsDtos;

    public List<CartGoodsDto> getCartGoodsDtos() {
        return cartGoodsDtos;
    }

    public void setCartGoodsDtos(List<CartGoodsDto> cartGoodsDtos) {
        this.cartGoodsDtos = cartGoodsDtos;
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
                ", cartGoodsDtos=" + cartGoodsDtos +
                '}';
    }
}
