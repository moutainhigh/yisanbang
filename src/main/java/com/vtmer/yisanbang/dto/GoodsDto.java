package com.vtmer.yisanbang.dto;

public class GoodsDto {

    private Integer colorSizeId;

    private Integer isGoods;

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

    @Override
    public String toString() {
        return "GoodsDto{" +
                "colorSizeId=" + colorSizeId +
                ", isGoods=" + isGoods +
                '}';
    }
}
