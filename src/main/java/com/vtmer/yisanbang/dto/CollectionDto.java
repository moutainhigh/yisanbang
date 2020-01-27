package com.vtmer.yisanbang.dto;

public class CollectionDto {

    private Integer goodsId;

    private Integer isGoods;

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    @Override
    public String toString() {
        return "CollectionDto{" +
                "goodsId=" + goodsId +
                ", isGoods=" + isGoods +
                '}';
    }

    public Integer getIsGoods() {
        return isGoods;
    }

    public void setIsGoods(Integer isGoods) {
        this.isGoods = isGoods;
    }
}
