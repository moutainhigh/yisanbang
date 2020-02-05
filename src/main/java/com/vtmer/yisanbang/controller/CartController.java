package com.vtmer.yisanbang.controller;

import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.vo.AddCartGoodsVo;
import com.vtmer.yisanbang.dto.AddGoodsDto;
import com.vtmer.yisanbang.dto.DeleteCartGoodsDto;
import com.vtmer.yisanbang.service.CartService;
import com.vtmer.yisanbang.vo.CartVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

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
    public ResponseMessage listCartGoods(@PathVariable("id") Integer userId) {
        CartVo cartVo = cartService.selectCartVoByUserId(userId);
        if (cartVo !=null) {
            if (!cartVo.getCartGoodsList().isEmpty())  // 购物车不为空
                return ResponseMessage.newSuccessInstance(cartVo,"获取购物车商品列表成功");
            else
                return ResponseMessage.newSuccessInstance("购物车为空");
        } else {
            logger.warn("传入的userId有误");
            return ResponseMessage.newErrorInstance("获取购物车商品列表失败");
        }
    }

    /*
        添加商品到购物车
     */
    @PostMapping("/addCartGoods")
    public ResponseMessage addCartGoods(@RequestBody AddCartGoodsVo AddCartGoodsVo) {
        int res = cartService.addCartGoods(AddCartGoodsVo);
        if (res == 1) return ResponseMessage.newSuccessInstance("加入购物车成功");
        else {
            logger.warn("传入的userId有误");
            return ResponseMessage.newErrorInstance("加入购物车失败，请检查传入的参数");
        }
    }

    /*
        更新购物车勾选商品
     */
    @PutMapping("/updateChosen")
    public ResponseMessage updateChosen(@RequestBody AddGoodsDto addGoodsDto) {
        double totalPrice = cartService.updateChosen(addGoodsDto);
        HashMap<String,Double> map = new HashMap<>();
        map.put("totalPrice",totalPrice);
        if (totalPrice!=-1) return ResponseMessage.newSuccessInstance(map,"修改勾选成功");
        else {
            logger.warn("传入的userId有误");
            return ResponseMessage.newErrorInstance("修改勾选失败，请检查传入的参数");
        }
    }

    @PutMapping("/addOrSubtractAmount")
    public ResponseMessage addOrSubtractAmount(@RequestBody AddGoodsDto addGoodsDto) {
        double totalPrice = cartService.addOrSubtractAmount(addGoodsDto);
        HashMap<String,Double> map = new HashMap<>();
        map.put("totalPrice",totalPrice);
        if (totalPrice == -1) {
            return ResponseMessage.newErrorInstance("更新购物车商品数量失败，请检查传入的参数");
        } else if (totalPrice == 0) {
            return ResponseMessage.newSuccessInstance(null,"更新购物车商品数量成功，价格不变");
        } else return ResponseMessage.newSuccessInstance(map,"更新购物车数量成功");
    }

    @PutMapping("/updateAmount")
    public ResponseMessage updateAmount(@RequestBody AddGoodsDto addGoodsDto) {
        double totalPrice = cartService.updateAmount(addGoodsDto);
        HashMap<String,Double> map = new HashMap<>();
        map.put("totalPrice",totalPrice);
        if (totalPrice == -1) {
            return ResponseMessage.newErrorInstance("更新购物车商品数量失败，请检查传入的参数");
        } else if (totalPrice == 0) {
            return ResponseMessage.newSuccessInstance(null,"更新购物车商品数量成功，价格不变");
        } else return ResponseMessage.newSuccessInstance(map,"更新购物车数量成功");
    }

    @DeleteMapping("/deleteCartGoods")
    public ResponseMessage deleteCartGoods(@RequestBody DeleteCartGoodsDto deleteCartGoodsDto) {
        Boolean b = cartService.deleteCartGoods(deleteCartGoodsDto);
        if (b) {
            return ResponseMessage.newSuccessInstance("删除购物车商品成功");
        } else {
            return ResponseMessage.newErrorInstance("删除购物车商品失败");
        }
    }
}
