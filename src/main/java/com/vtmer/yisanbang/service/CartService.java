package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.dto.CartGoodsDto;
import com.vtmer.yisanbang.dto.GoodsSkuDto;
import com.vtmer.yisanbang.vo.CartVo;

import java.util.List;
import java.util.Map;

public interface CartService {

    CartVo selectCartVo();

    void addCartGoods(List<CartGoodsDto> cartGoodsDtoList);

    boolean updateChosen(GoodsSkuDto goodsSkuDto);

    boolean addOrSubtractAmount(CartGoodsDto cartGoodsDto);

    boolean updateAmount(CartGoodsDto cartGoodsDto);

    Boolean deleteCartGoods(List<GoodsSkuDto> goodsSkuDtoList);

    // 计算购物车总价并更新
    Map<String,Double> calculateTotalPrice(List<CartGoodsDto> cartGoodsList);

    // 根据用户id删除购物车勾选项
    boolean deleteCartGoodsByIsChosen();
}
