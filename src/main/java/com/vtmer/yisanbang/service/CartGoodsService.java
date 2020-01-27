package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.dto.CartGoodsDto;

import java.util.List;

public interface CartGoodsService {

    public List<CartGoodsDto> selectCartGoodsByCartId(Integer cartId);
}
