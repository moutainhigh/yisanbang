package com.vtmer.yisanbang.dto;

public class AddGoodsDto {

    private Integer userId;

    private Integer colorSizeId;

    private Integer isGoods;

    private Integer amount;

    private Integer cartId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getColorSizeId() {
        return colorSizeId;
    }

    public void setColorSizeId(Integer colorSizeId) {
        this.colorSizeId = colorSizeId;
    }

    public Integer getIsGoods() {
        return isGoods;
    }

    public void setIsGoods(Integer isGoods) {
        this.isGoods = isGoods;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    @Override
    public String toString() {
        return "AddGoodsDto{" +
                "userId=" + userId +
                ", colorSizeId=" + colorSizeId +
                ", isGoods=" + isGoods +
                ", amount=" + amount +
                ", cartId=" + cartId +
                '}';
    }
}
