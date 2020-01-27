package com.vtmer.yisanbang.service.impl;

import com.vtmer.yisanbang.dto.CartGoodsDto;
import com.vtmer.yisanbang.mapper.CartGoodsMapper;
import com.vtmer.yisanbang.service.CartGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartGoodsServiceImpl implements CartGoodsService {

    @Autowired
    private CartGoodsMapper cartGoodsMapper;

    @Override
    /*
        根据购物车id查询商品信息，包括颜色尺寸，商品基本信息
     */
    public List<CartGoodsDto> selectCartGoodsByCartId(Integer cartId) {
        return cartGoodsMapper.selectCartGoodsByCartId(cartId);
    }
}
