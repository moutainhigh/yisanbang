package com.vtmer.yisanbang.vo;

import com.vtmer.yisanbang.dto.AddGoodsDto;

import java.util.List;

public class AddCartGoodsVo {

    private Integer userId;

    private Integer isGoods;

    private List<AddGoodsDto> addGoodsDtoList;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }



    public List<AddGoodsDto> getAddGoodsDtoList() {
        return addGoodsDtoList;
    }

    public void setAddGoodsDtoList(List<AddGoodsDto> addGoodsDtoList) {
        this.addGoodsDtoList = addGoodsDtoList;
    }

    public Integer getIsGoods() {
        return isGoods;
    }

    public void setIsGoods(Integer isGoods) {
        this.isGoods = isGoods;
    }

    @Override
    public String toString() {
        return "AddCartGoodsDto{" +
                "userId=" + userId +
                ", isGoods=" + isGoods +
                ", addGoodsDtoList=" + addGoodsDtoList +
                '}';
    }
}

