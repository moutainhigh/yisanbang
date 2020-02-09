package com.vtmer.yisanbang.vo;

import com.vtmer.yisanbang.common.validGroup.Insert;
import com.vtmer.yisanbang.common.validGroup.Update;
import com.vtmer.yisanbang.dto.AddGoodsDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel
public class AddCartGoodsVo {

    @NotNull(groups = {Insert.class, Update.class},message = "userId is null")
    @ApiModelProperty(value = "用户id",required = true,example = "1")
    private Integer userId;

    /**
     * 商品加入购物车时使用的字段，因为在一个页面中商品的isGoods是一样的
     */
    @NotNull(groups = {Insert.class, Update.class},message = "isGoods is null")
    @ApiModelProperty(value = "是否是普通商品(非套装散件)",required = true,example = "true")
    private Boolean isGoods;

    @Valid
    @ApiModelProperty(value = "添加进购物车的商品列表",required = true)
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

    public Boolean getIsGoods() {
        return isGoods;
    }

    public void setIsGoods(Boolean goods) {
        isGoods = goods;
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

