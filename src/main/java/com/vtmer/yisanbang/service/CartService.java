package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.dto.AddGoodsDto;
import com.vtmer.yisanbang.dto.CartDto;
import com.vtmer.yisanbang.dto.CartGoodsDto;
import com.vtmer.yisanbang.dto.DeleteCartGoodsDto;

import java.util.List;

public interface CartService {

    public CartDto selectCartDtosByUserId(Integer userId);

    public double addCartGoods(AddGoodsDto addGoodsDto);

    public double updateChosen(AddGoodsDto addGoodsDto);

    public double addOrSubtractAmount(AddGoodsDto addGoodsDto);

    public double updateAmount(AddGoodsDto addGoodsDto);

    public Boolean deleteCartGoods(DeleteCartGoodsDto deleteCartGoodsDto);
}
