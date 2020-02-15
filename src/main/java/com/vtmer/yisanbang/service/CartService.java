package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.dto.CartGoodsDTO;
import com.vtmer.yisanbang.dto.GoodsSkuDTO;
import com.vtmer.yisanbang.vo.CartVo;

import java.util.List;
import java.util.Map;

public interface CartService {

    CartVo selectCartVo();

    void addCartGoods(List<CartGoodsDTO> cartGoodsDTOList);

    void updateChosen(GoodsSkuDTO goodsSkuDto);

    void addOrSubtractAmount(CartGoodsDTO cartGoodsDto);

    void updateAmount(CartGoodsDTO cartGoodsDto);

    void deleteCartGoods(List<GoodsSkuDTO> goodsSkuDTOList);

    // 计算购物车总价并更新
    Map<String,Double> calculateTotalPrice(List<CartGoodsDTO> cartGoodsList);

    // 根据用户id删除购物车勾选项
    boolean deleteCartGoodsByIsChosen();
}
