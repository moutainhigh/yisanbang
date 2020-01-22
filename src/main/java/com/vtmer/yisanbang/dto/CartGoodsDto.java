package com.vtmer.yisanbang.dto;

import com.vtmer.yisanbang.domain.ColorSize;
import com.vtmer.yisanbang.domain.Goods;

import java.util.Date;
/*
    添加goods屬性方便传输
 */
public class CartGoodsDto {

    private Integer colorSizeId;

    private Integer amount;

    private Byte isChosen;

    private ColorSizeDto colorSizeDto;

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

    public Byte getIsChosen() {
        return isChosen;
    }

    public void setIsChosen(Byte isChosen) {
        this.isChosen = isChosen;
    }

    public ColorSizeDto getColorSizeDto() {
        return colorSizeDto;
    }

    public void setColorSizeDto(ColorSizeDto colorSizeDto) {
        this.colorSizeDto = colorSizeDto;
    }

    @Override
    public String toString() {
        return "CartGoodsDto{" +
                " colorSizeId=" + colorSizeId +
                ", amount=" + amount +
                ", isChosen=" + isChosen +
                ", colorSizeDto=" + colorSizeDto +
                '}';
    }
}
