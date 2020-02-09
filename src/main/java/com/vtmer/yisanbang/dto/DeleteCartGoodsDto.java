package com.vtmer.yisanbang.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel
public class DeleteCartGoodsDto {

    @NotNull(message = "userId is null")
    @ApiModelProperty(value = "用户id",example = "1")
    private Integer userId;

    private Integer cartId;

    @Valid
    @ApiModelProperty("商品列表")
    private List<GoodsSkuDto> goodsDtoList;

    public DeleteCartGoodsDto getDeleteCartGoodsDto() {
        return this;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<GoodsSkuDto> getGoodsDtoList() {
        return goodsDtoList;
    }

    public void setGoodsDtoList(List<GoodsSkuDto> goodsDtoList) {
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
