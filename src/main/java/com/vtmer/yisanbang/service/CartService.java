package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.dto.AddGoodsDto;
import com.vtmer.yisanbang.dto.CartDto;
import com.vtmer.yisanbang.dto.CartGoodsDto;

import java.util.List;

public interface CartService {

    public CartDto selectCartDtosByUserId(Integer userId);

    public double addCartGoods(AddGoodsDto addGoodsDto);

}
