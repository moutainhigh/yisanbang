package com.vtmer.yisanbang.dto;

import com.vtmer.yisanbang.common.validGroup.AddGoods;
import com.vtmer.yisanbang.common.validGroup.Insert;
import com.vtmer.yisanbang.common.validGroup.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

@ApiModel
public class AddGoodsDto {


    @ApiModelProperty(hidden = true)
    private Integer userId;

    @NotNull(groups = {Insert.class, Update.class,AddGoods.class},message = "colorSizeId is null")
    @ApiModelProperty(name = "颜色尺寸id")
    private Integer colorSizeId;

    @NotNull(groups = {AddGoods.class})
    @ApiModelProperty(hidden = true)
    private Integer isGoods;

    @NotNull(groups = {Insert.class,AddGoods.class},message = "amount is null")
    @ApiModelProperty(name = "商品数量")
    private Integer amount;

    @NotNull(groups = {AddGoods.class})
    @ApiModelProperty(hidden = true)
    private Integer cartId;

    public AddGoodsDto() {

    }

    public AddGoodsDto(Integer colorSizeId,Integer isGoods,Integer cartId) {
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
