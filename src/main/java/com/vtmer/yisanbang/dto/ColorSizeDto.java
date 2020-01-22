package com.vtmer.yisanbang.dto;

import com.vtmer.yisanbang.domain.Goods;

public class ColorSizeDto {

    private String color;

    private Integer size;

    private Integer goodsId;

    private GoodsDto goodsDto;


    public GoodsDto getGoodsDto() {
        return goodsDto;
    }

    public void setGoodsDto(GoodsDto goodsDto) {
        this.goodsDto = goodsDto;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    @Override
    public String toString() {
        return "ColorSizeDto{" +
                "color='" + color + '\'' +
                ", size=" + size +
                ", goodsId=" + goodsId +
                ", goodsDto=" + goodsDto +
                '}';
    }
}
