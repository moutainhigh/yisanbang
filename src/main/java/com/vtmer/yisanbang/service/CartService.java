package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.dto.CartGoodsDTO;
import com.vtmer.yisanbang.dto.GoodsSkuDTO;
import com.vtmer.yisanbang.vo.CartVO;

import java.util.List;
import java.util.Map;

public interface CartService {

    CartVO selectCartVo(int userId);

    void addCartGoods(List<CartGoodsDTO> cartGoodsDTOList);

    void updateChosen(GoodsSkuDTO goodsSkuDto);

    void addOrSubtractAmount(CartGoodsDTO cartGoodsDto);

    void updateAmount(CartGoodsDTO cartGoodsDto);

    void deleteCartGoods(List<GoodsSkuDTO> goodsSkuDTOList);

    /**
     * 计算购物车总价并更新
     * @param cartGoodsList：购物车商品列表
     * @return Map:totalPrice、beforeTotalPrice，优惠前总价，优惠后总价
     */
    Map<String,Double> calculateTotalPrice(List<CartGoodsDTO> cartGoodsList);

    /**
     * 删除某用户的购物车商品
     * @return
     */
    boolean deleteCartGoodsByIsChosen(int userId);

    /**
     * 购物车数据持久化到数据库
     */
    void cartDataPersistence();

    void recoveryCartData();
}
