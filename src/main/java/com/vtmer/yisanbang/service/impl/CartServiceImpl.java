package com.vtmer.yisanbang.service.impl;

import com.sun.org.apache.bcel.internal.generic.DDIV;
import com.vtmer.yisanbang.common.ListSort;
import com.vtmer.yisanbang.domain.CartGoods;
import com.vtmer.yisanbang.dto.*;
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
                cartGoodsMapper.addOrSubtractAmount(addGoodsDto);
            } else {
                cartGoodsMapper.insertCartGoods(addGoodsDto);
            }
        } else {
            return -1;
        }
        // 更新价格
        return calculateTotalPrice(addGoodsDto.getUserId());
    }

    public double updateChosen(AddGoodsDto addGoodsDto) {
        // 根据userId获取cartId
        Integer cartId = cartMapper.selectCartIdByUserId(addGoodsDto.getUserId());
        if (cartId!=null) {
            addGoodsDto.setCartId(cartId);
            Integer isChosen = cartGoodsMapper.selectChosen(addGoodsDto);
            cartGoodsMapper.updateChosen(addGoodsDto,isChosen);
            // 更新价格
            return calculateTotalPrice(addGoodsDto.getUserId());
        } else {
            return -1;
        }
    }

    @Override
    public double addOrSubtractAmount(AddGoodsDto addGoodsDto) {
        // 更新数量
        boolean b = cartGoodsMapper.addOrSubtractAmount(addGoodsDto);
        if (b) {
            return updateTotalPrice(addGoodsDto);
        } else {
            return -1;
        }
    }

    @Override
    public double updateAmount(AddGoodsDto addGoodsDto) {
        boolean b = cartGoodsMapper.updateAmount(addGoodsDto);
        if (b) {
            return updateTotalPrice(addGoodsDto);
        } else {
            return -1;
        }
    }

    @Override
    public Boolean deleteCartGoods(DeleteCartGoodsDto deleteCartGoodsDto) {
        // 删除商品
        Integer cartId = deleteCartGoodsDto.getCartId();
        List<GoodsDto> goodsDtoList = deleteCartGoodsDto.getGoodsDtoList();
        for (GoodsDto goodsDto : goodsDtoList) {
            // 使用AddGoodsDto进行数据传输——colorSizeId,isGoods,cartId
            AddGoodsDto addGoodsDto = new AddGoodsDto(goodsDto.getColorSizeId(), goodsDto.getIsGoods(), cartId);
            Boolean b = cartGoodsMapper.deleteCartGoods(addGoodsDto);
            // 删除失败返回false
            if (!b) return false;
        }
        // 更新总价
        calculateTotalPrice(deleteCartGoodsDto.getUserId());
        return true;
    }


    /*
        根据购物车所有商品信息计算总价，并更新
    */
    private double calculateTotalPrice(Integer userId) {
        CartDto cartDto = selectCartDtosByUserId(userId);
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

    /*
        检查是否需要更新总价
     */
    private double updateTotalPrice(AddGoodsDto addGoodsDto) {
        Integer isChosen = cartGoodsMapper.selectChosen(addGoodsDto);
        // 如果是已勾选的商品，则需要更新价格
        if (isChosen == 1) {
            // 更新价格并返回更新后的总价
            return calculateTotalPrice(addGoodsDto.getUserId());
        } else return 0;
    }

}
