package com.vtmer.yisanbang.domain;

import java.util.Date;

public class CartGoods {
    private Integer id;

    private Integer cartId;

    private Integer colorSizeId;

    private Integer amount;

    private Byte isGoods;

    private Byte isChosen;

    private Date createTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Integer getColorSizeId() {
        return colorSizeId;
    }

    public void setColorSizeId(Integer colorSizeId) {
        this.colorSizeId = colorSizeId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Byte getIsGoods() {
        return isGoods;
    }

    public void setIsGoods(Byte isGoods) {
        this.isGoods = isGoods;
    }

    public Byte getIsChosen() {
        return isChosen;
    }

    public void setIsChosen(Byte isChosen) {
        this.isChosen = isChosen;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}