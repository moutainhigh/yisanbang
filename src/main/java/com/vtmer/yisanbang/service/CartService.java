package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.dto.AddCartGoodsDto;
import com.vtmer.yisanbang.dto.AddGoodsDto;
import com.vtmer.yisanbang.dto.DeleteCartGoodsDto;
import com.vtmer.yisanbang.vo.CartVo;

public interface CartService {

    public CartVo selectCartDtosByUserId(Integer userId);

    public int addCartGoods(AddCartGoodsDto AddCartGoodsDto);

    public double updateChosen(AddGoodsDto addGoodsDto);

    public double addOrSubtractAmount(AddGoodsDto addGoodsDto);

    public double updateAmount(AddGoodsDto addGoodsDto);

    public Boolean deleteCartGoods(DeleteCartGoodsDto deleteCartGoodsDto);
}
