package com.vtmer.yisanbang.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public class DeleteCartGoodsDto {

    @NotNull(message = "userId is null")
    private Integer userId;

    @NotNull(message = "cartId is null")
    private Integer cartId;

    @Valid
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
