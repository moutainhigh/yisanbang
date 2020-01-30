package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.dto.AddCartGoodsDto;
import com.vtmer.yisanbang.dto.AddGoodsDto;
import com.vtmer.yisanbang.dto.CartDto;
import com.vtmer.yisanbang.dto.DeleteCartGoodsDto;

public interface CartService {

    public CartDto selectCartDtosByUserId(Integer userId);

    public int addCartGoods(AddCartGoodsDto AddCartGoodsDto);

    public double updateChosen(AddGoodsDto addGoodsDto);

    public double addOrSubtractAmount(AddGoodsDto addGoodsDto);

    public double updateAmount(AddGoodsDto addGoodsDto);

    public Boolean deleteCartGoods(DeleteCartGoodsDto deleteCartGoodsDto);
}
