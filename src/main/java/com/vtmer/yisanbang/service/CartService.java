package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.vo.AddCartGoodsVo;
import com.vtmer.yisanbang.dto.AddGoodsDto;
import com.vtmer.yisanbang.dto.DeleteCartGoodsDto;
import com.vtmer.yisanbang.vo.CartVo;

public interface CartService {

    CartVo selectCartVoByUserId(Integer userId);

    int addCartGoods(AddCartGoodsVo AddCartGoodsVo);

    double updateChosen(AddGoodsDto addGoodsDto);

    double addOrSubtractAmount(AddGoodsDto addGoodsDto);

    double updateAmount(AddGoodsDto addGoodsDto);

    Boolean deleteCartGoods(DeleteCartGoodsDto deleteCartGoodsDto);

    // 计算购物车总价并更新
    double calculateTotalPrice(Integer userId);
}
