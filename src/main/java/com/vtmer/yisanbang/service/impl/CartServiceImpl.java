package com.vtmer.yisanbang.service.impl;

import com.sun.org.apache.bcel.internal.generic.DDIV;
import com.vtmer.yisanbang.common.ListSort;
import com.vtmer.yisanbang.domain.CartGoods;
import com.vtmer.yisanbang.dto.AddGoodsDto;
import com.vtmer.yisanbang.dto.CartDto;
import com.vtmer.yisanbang.dto.CartGoodsDto;
import com.vtmer.yisanbang.mapper.CartGoodsMapper;
import com.vtmer.yisanbang.mapper.CartMapper;
import com.vtmer.yisanbang.mapper.ColorSizeMapper;
import com.vtmer.yisanbang.mapper.PartSizeMapper;
import com.vtmer.yisanbang.service.CartGoodsService;
import com.vtmer.yisanbang.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private CartGoodsMapper cartGoodsMapper;

    @Autowired
    private PartSizeMapper partSizeMapper;

    @Autowired
    private ColorSizeMapper colorSizeMapper;

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


    @Override
    public double addCartGoods(AddGoodsDto addGoodsDto) {
        // 根据userId获取cartId
        Integer cartId = cartMapper.selectCartIdByUserId(addGoodsDto.getUserId());
        if (cartId!=null) {
            addGoodsDto.setCartId(cartId);
            boolean isGoodsExist = cartGoodsMapper.checkGoodsExist(addGoodsDto);
            // 如果该商品已经存在，则增加其amount,否则插入新数据
            if (isGoodsExist) {
                cartGoodsMapper.updateAmount(addGoodsDto);
            } else {
                cartGoodsMapper.insertCartGoods(addGoodsDto);
            }
        } else {
            return -1;
        }
        // 更新价格
        CartDto cartDto = selectCartDtosByUserId(addGoodsDto.getUserId());
        return calculateTotalPrice(cartDto);
    }

    public double updateChosen(AddGoodsDto addGoodsDto) {
        // 根据userId获取cartId
        Integer cartId = cartMapper.selectCartIdByUserId(addGoodsDto.getUserId());
        if (cartId!=null) {
            addGoodsDto.setCartId(cartId);
            Integer isChosen = cartGoodsMapper.selectChosen(addGoodsDto);
            cartGoodsMapper.updateChosen(addGoodsDto,isChosen);
            // 更新价格
            CartDto cartDto = selectCartDtosByUserId(addGoodsDto.getUserId());
            return calculateTotalPrice(cartDto);
        } else {
            return -1;
        }
    }

    /*
        根据购物车所有商品信息计算总价，并更新
     */
    private double calculateTotalPrice(CartDto cartDto) {
        double totalPrice = 0;
        List<CartGoodsDto> cartGoodsDtos = cartDto.getCartGoodsDtos();
        for (CartGoodsDto cartGoodsDto : cartGoodsDtos) {
            // 如果勾选了，计算总价
            if (cartGoodsDto.getIsChosen() == 1) {
                totalPrice += cartGoodsDto.getPrice() * cartGoodsDto.getAmount();
            }
        }
        // 更新总价
        cartMapper.updateTotalPrice(totalPrice,cartDto.getCartId());
        return totalPrice;
    }
}
