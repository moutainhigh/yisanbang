package com.vtmer.yisanbang.service.impl;

import com.vtmer.yisanbang.common.ListSort;
import com.vtmer.yisanbang.domain.Discount;
import com.vtmer.yisanbang.dto.*;
import com.vtmer.yisanbang.mapper.CartGoodsMapper;
import com.vtmer.yisanbang.mapper.CartMapper;
import com.vtmer.yisanbang.mapper.DiscountMapper;
import com.vtmer.yisanbang.service.CartService;
import com.vtmer.yisanbang.vo.AddCartGoodsVo;
import com.vtmer.yisanbang.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private CartGoodsMapper cartGoodsMapper;

    @Autowired
    private DiscountMapper discountMapper;

    // 满X件打折
    private int discountAmount;

    // 打折率
    private double discountRate;

    // 设置优惠信息
    private void setDiscount() {
        Discount discount = discountMapper.select();
        if (discount !=null) {
            discountAmount = discount.getAmount();
            discountRate = discount.getDiscountRate();
        } else {  // 如果未设置优惠，则默认达标数量为0，优惠*1,，即无打折
            discountAmount = 0;
            discountRate = 1;
        }
    }

    @Override
    public CartVo selectCartDtosByUserId(Integer userId) {
        setDiscount();
        double beforeCartTotalPrice = 0;
        // 根据userId查cartId
        Integer cartId = cartMapper.selectCartIdByUserId(userId);
        if (cartId!=null) {
            CartVo cartVo = cartMapper.selectCartDtoByUserId(userId);
            // 由购物车id查询购物车所有普通商品信息
            List<CartGoodsDto> CartGoodsList = cartGoodsMapper.selectCartGoodsByCartId1(cartId);
            // 由购物车id查询购物车所有套装散件信息
            List<CartGoodsDto> CartSuitList = cartGoodsMapper.selectCartGoodsByCartId(cartId);
            // 两个集合合并
            CartGoodsList.addAll(CartSuitList);
            // 根据时间排序
            ListSort.listTimeSort(CartGoodsList);
            for (CartGoodsDto cartGoodsDto : CartGoodsList) {
                double afterTotalPrice = 0;
                // 计算单件商品总价
                double totalPrice = cartGoodsDto.getPrice() * cartGoodsDto.getAmount();
                // 购物车计算优惠前的总价格
                beforeCartTotalPrice += totalPrice;
                cartGoodsDto.setTotalPrice(totalPrice);
                // 如果符合优惠
                if (cartGoodsDto.getAmount() >= discountAmount) {
                    afterTotalPrice = totalPrice * discountRate;
                    cartGoodsDto.setAfterTotalPrice(afterTotalPrice);
                } else {
                    cartGoodsDto.setAfterTotalPrice(totalPrice);
                }
            } // end for
            cartVo.setCartGoodsDtos(CartGoodsList);
            cartVo.setBeforeTotalPrice(beforeCartTotalPrice);
            return cartVo;
        } else {
            return null;
        }
    }

    @Override
    public int addCartGoods(AddCartGoodsVo addCartGoodsVo) {
        // 根据userId获取cartId
        Integer cartId = cartMapper.selectCartIdByUserId(addCartGoodsVo.getUserId());
        if (cartId!=null) {
            List<AddGoodsDto> addGoodsDtoList = addCartGoodsVo.getAddGoodsDtoList();
            for (AddGoodsDto addGoodsDto : addGoodsDtoList) {
                addGoodsDto.setCartId(cartId);
                addGoodsDto.setIsGoods(addCartGoodsVo.getIsGoods());
                boolean isGoodsExist = cartGoodsMapper.checkGoodsExist(addGoodsDto);
                // 如果该商品已经存在，则增加其amount,否则插入新数据
                if (isGoodsExist) {
                    cartGoodsMapper.addOrSubtractAmount(addGoodsDto);
                } else {
                    cartGoodsMapper.insertCartGoods(addGoodsDto);
                }
            } // end for
        } else {
            return -1;
        }
        // 更新价格
        calculateTotalPrice(addCartGoodsVo.getUserId());
        return 1;
    }

    public double updateChosen(AddGoodsDto addGoodsDto) {
        // 根据userId获取cartId
        Integer cartId = cartMapper.selectCartIdByUserId(addGoodsDto.getUserId());
        if (cartId!=null) {
            addGoodsDto.setCartId(cartId);
            Integer isChosen = cartGoodsMapper.selectChosen(addGoodsDto);
            cartGoodsMapper.updateChosen(addGoodsDto,isChosen);
            // 更新价格
            return updateTotalPrice(addGoodsDto);
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
        setDiscount();
        CartVo cartVo = selectCartDtosByUserId(userId);
        double totalPrice = 0;
        List<CartGoodsDto> cartGoodsDtos = cartVo.getCartGoodsDtos();
        for (CartGoodsDto cartGoodsDto : cartGoodsDtos) {
            // 如果勾选了，计算总价
            if (cartGoodsDto.getIsChosen() == 1) {
                // 如果符合优惠
                if (cartGoodsDto.getAmount() >= discountAmount) {
                    totalPrice += cartGoodsDto.getPrice() * cartGoodsDto.getAmount() * discountRate;
                } else {
                    totalPrice += cartGoodsDto.getPrice() * cartGoodsDto.getAmount();
                }
            }
        }
        // 更新总价
        cartMapper.updateTotalPrice(totalPrice, cartVo.getCartId());
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
