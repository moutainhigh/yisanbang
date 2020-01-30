package com.vtmer.yisanbang.dto;

import java.util.List;

public class CartDto {

    private Integer cartId;

    private String totalPrice;

    private String afterTotalPrice;

    private List<CartGoodsDto> cartGoodsDtos;

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

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

    @Override
    public String toString() {
        return "CartDto{" +
                "cartId=" + cartId +
                ", totalPrice='" + totalPrice + '\'' +
                ", cartGoodsDtos=" + cartGoodsDtos +
                '}';
    }
}
