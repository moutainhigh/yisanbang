package com.vtmer.yisanbang.dto;

import com.vtmer.yisanbang.domain.Goods;

import java.util.List;

public class DeleteCartGoodsDto {

    private Integer userId;

    private Integer cartId;

    private List<GoodsDto> goodsDtoList;

    public DeleteCartGoodsDto getDeleteCartGoodsDto() {
        return this;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<GoodsDto> getGoodsDtoList() {
        return goodsDtoList;
    }

    public void setGoodsDtoList(List<GoodsDto> goodsDtoList) {
        this.goodsDtoList = goodsDtoList;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    @Override
    public String toString() {
        return "DeleteCartGoodsDto{" +
                "userId=" + userId +
                ", cartId=" + cartId +
                ", goodsDtoList=" + goodsDtoList +
                '}';
    }
}
