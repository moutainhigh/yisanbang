package com.vtmer.yisanbang.service.impl;

import com.sun.org.apache.bcel.internal.generic.DDIV;
import com.vtmer.yisanbang.dto.CartDto;
import com.vtmer.yisanbang.dto.CartGoodsDto;
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

    @Override
    public CartDto selectCartDtosByUserId(Integer userId) {

        // 根据userId查cartId
        Integer cartId = cartMapper.selectCartIdByUserId(userId);
        if (cartId!=null) {
            return cartMapper.selectCartDtosByUserId(userId);
        } else {
            return null;
        }
    }
}
