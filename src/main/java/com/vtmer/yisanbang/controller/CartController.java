package com.vtmer.yisanbang.controller;

import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.dto.CartDto;
import com.vtmer.yisanbang.dto.CartGoodsDto;
import com.vtmer.yisanbang.service.CartService;
import com.vtmer.yisanbang.service.impl.CartServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private CartService cartService;

    /*
        购物车商品列表
     */
    @GetMapping("/listCartGoods/{id}")
    public ResponseMessage listCart(@PathVariable("id") Integer userId) {
        CartDto cartDto = cartService.selectCartDtosByUserId(userId);
        if (cartDto!=null) {
            if (!cartDto.getCartGoodsDtos().isEmpty())  // 购物车不为空
                return ResponseMessage.newSuccessInstance(cartDto,"获取购物车商品列表成功");
            else
                return ResponseMessage.newSuccessInstance("购物车为空");
        } else {
            logger.warn("传入的userId有误");
            return ResponseMessage.newSuccessInstance("获取购物车商品列表失败");
        }
    }


}
