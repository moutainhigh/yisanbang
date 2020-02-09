package com.vtmer.yisanbang.dto;

import com.vtmer.yisanbang.common.validGroup.AddGoods;
import com.vtmer.yisanbang.common.validGroup.Insert;
import com.vtmer.yisanbang.common.validGroup.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

@ApiModel
public class AddGoodsDto {

    @NotNull(message = "userId is null",groups = {Update.class})
    @ApiModelProperty(value = "用户id",example = "1")
    private Integer userId;

    @NotNull(groups = {Insert.class, Update.class,AddGoods.class},message = "colorSizeId is null")
    @ApiModelProperty(value = "颜色尺寸id",example = "2",required = true)
    private Integer colorSizeId;

    @NotNull(groups = {AddGoods.class,Update.class})
    @ApiModelProperty(value = "是否是普通商品",notes = "1代表是，0代表否",example = "false",required = true)
    private Boolean isGoods;

    @NotNull(groups = {Insert.class,AddGoods.class},message = "amount is null")
    @ApiModelProperty(value = "商品数量",example = "6")
    private Integer amount;

    @NotNull(groups = {AddGoods.class})
    @ApiModelProperty(readOnly = true,example = "1",value = "购物车id")
    private Integer cartId;

    public AddGoodsDto() {

    }

    public AddGoodsDto(Integer colorSizeId,Boolean isGoods,Integer cartId) {
        this.colorSizeId = colorSizeId;
        this.isGoods = isGoods;
        this.cartId = cartId;
    }

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

    public Boolean getIsGoods() {
        return isGoods;
    }

    public void setIsGoods(Boolean goods) {
        isGoods = goods;
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
