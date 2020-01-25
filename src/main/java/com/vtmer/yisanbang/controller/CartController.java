package com.vtmer.yisanbang.controller;

import com.sun.org.apache.regexp.internal.RE;
import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.dto.AddGoodsDto;
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

    @PostMapping("/addCartGoods")
    public ResponseMessage addCartGoods(@RequestBody AddGoodsDto addGoodsDto) {
        double totalPrice = cartService.addCartGoods(addGoodsDto);
        if (totalPrice!=-1) return ResponseMessage.newSuccessInstance(totalPrice,"加入购物车成功");
        else {
            logger.warn("传入的userId有误");
            return ResponseMessage.newErrorInstance("加入购物车失败，请检查传入的参数");
        }
    }

    @PutMapping("/updateChosen")
    public ResponseMessage updateChosen(@RequestBody AddGoodsDto addGoodsDto) {
        double totalPrice = cartService.updateChosen(addGoodsDto);
        if (totalPrice!=-1) return ResponseMessage.newSuccessInstance(totalPrice,"修改勾选成功");
        else {
            logger.warn("传入的userId有误");
            return ResponseMessage.newErrorInstance("修改勾选失败，请检查传入的参数");
        }
    }
}
