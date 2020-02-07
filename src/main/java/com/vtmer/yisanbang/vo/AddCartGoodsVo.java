package com.vtmer.yisanbang.vo;

import com.vtmer.yisanbang.common.validGroup.Insert;
import com.vtmer.yisanbang.common.validGroup.Update;
import com.vtmer.yisanbang.dto.AddGoodsDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public class AddCartGoodsVo {

    @NotNull(groups = {Insert.class, Update.class},message = "userId is null")
    private Integer userId;

    /**
     * 商品加入购物车时使用的字段，因为在一个页面中商品的isGoods是一样的
     */
    @NotNull(groups = {Insert.class, Update.class},message = "isGoods is null")
    private Integer isGoods;

    @Valid
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

