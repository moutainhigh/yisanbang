package com.vtmer.yisanbang.service.impl;

import com.sun.org.apache.bcel.internal.generic.DDIV;
import com.vtmer.yisanbang.common.ListSort;
import com.vtmer.yisanbang.domain.CartGoods;
import com.vtmer.yisanbang.dto.CartDto;
import com.vtmer.yisanbang.dto.CartGoodsDto;
import com.vtmer.yisanbang.mapper.CartGoodsMapper;
import com.vtmer.yisanbang.mapper.CartMapper;
import com.vtmer.yisanbang.service.CartGoodsService;
import com.vtmer.yisanbang.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartGoodsService cartGoodsService;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private CartGoodsMapper cartGoodsMapper;

    @Override
    public CartDto selectCartDtosByUserId(Integer userId) {

        // 根据userId查cartId
        Integer cartId = cartMapper.selectCartIdByUserId(userId);
        if (cartId!=null) {
            CartDto cartDto = cartMapper.selectCartDtoByUserId(userId);
            // 由购物车id查询购物车所有普通商品信息
            List<CartGoodsDto> CartGoodsList = cartGoodsMapper.selectCartGoodsByCartId1(cartId);
            // 由购物车id查询购物车所有套装散件信息
            List<CartGoodsDto> CartSuitList = cartGoodsMapper.selectCartGoodsByCartId(cartId);
            // 两个集合合并
            CartGoodsList.addAll(CartSuitList);
            // 根据时间排序
            ListSort.listTimeSort(CartGoodsList);
            cartDto.setCartGoodsDtos(CartGoodsList);
            return cartDto;
        } else {
            return null;
        }
    }
}
