package com.vtmer.yisanbang.dto;

import java.util.List;

public class CartDto {

    private Integer id;

    private String totalPrice;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "CartDto{" +
                "id=" + id +
                ", totalPrice='" + totalPrice + '\'' +
                ", cartGoodsDtos=" + cartGoodsDtos +
                '}';
    }
}
