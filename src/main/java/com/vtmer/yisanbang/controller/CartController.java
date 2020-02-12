package com.vtmer.yisanbang.controller;

import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.common.validGroup.Insert;
import com.vtmer.yisanbang.common.validGroup.Update;
import com.vtmer.yisanbang.dto.CartGoodsDto;
import com.vtmer.yisanbang.dto.GoodsSkuDto;
import com.vtmer.yisanbang.service.CartService;
import com.vtmer.yisanbang.vo.CartVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "购物车接口")
@RestController
@RequestMapping("/cart")
public class CartController {

    private final Logger logger = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private CartService cartService;


    /**
     * 获取用户购物车商品列表接口
     * @return
     */
    @ApiOperation(value = "获取用户的购物车商品列表")
    @GetMapping("/listCartGoods")
    public ResponseMessage<CartVo> listCartGoods() {
        CartVo cartVo = cartService.selectCartVo();
        if (cartVo !=null) {
            return ResponseMessage.newSuccessInstance(cartVo,"获取购物车商品列表成功");
        } else {
            return ResponseMessage.newErrorInstance("购物车为空");
        }
    }

    /**
     * 添加商品进入购物车接口
     * @param cartGoodsDtoList：添加进购物车商品列表
     * @return
     */
    @ApiOperation(value = "添加商品进入购物车")
    @PostMapping("/addCartGoods")
    public ResponseMessage addCartGoods(@RequestBody @Validated({Insert.class}) List<CartGoodsDto> cartGoodsDtoList) {

        cartService.addCartGoods(cartGoodsDtoList);
        return ResponseMessage.newSuccessInstance("加入购物车成功");

    }

    /**
     * 更新购物车勾选接口
     * @param goodsSkuDto：colorSizeId、isGoods
     * @return
     */
    @ApiOperation(value = "更新购物车勾选",notes = "每次用户(取消)勾选请求该接口")
    @PutMapping("/updateChosen")
    public ResponseMessage updateChosen(@RequestBody @Validated GoodsSkuDto goodsSkuDto) {
        boolean res = cartService.updateChosen(goodsSkuDto);
        if (res) {
            return ResponseMessage.newSuccessInstance("修改勾选成功");
        }
        else {
            return ResponseMessage.newErrorInstance("修改勾选失败，该商品不存在");
        }
    }

    /**
     * 购物车商品增加1或删减1接口
     * @param cartGoodsDto:colorSizeId、isGoods、amount
     * @return
     */
    @ApiOperation(value = "购物车商品增加1或删减1",notes = "用于购物车中+ -按钮，+ amount传1，- amount传-1")
    @PutMapping("/addOrSubtractAmount")
    public ResponseMessage addOrSubtractAmount(@RequestBody @Validated({Update.class}) CartGoodsDto cartGoodsDto) {
        boolean res = cartService.addOrSubtractAmount(cartGoodsDto);
        if (res) {
            return ResponseMessage.newSuccessInstance("更新购物车商品数量成功");
        } else {
            return ResponseMessage.newErrorInstance("更新购物车商品数量失败，该商品不存在");
        }
    }

    /**
     * 购物车商品直接修改商品数量接口
     * @param cartGoodsDto：colorSizeId、isGoods、amount
     * @return
     */
    @ApiOperation(value = "直接修改购物车某件商品的数量",notes = "amount传修改数量")
    @PutMapping("/updateAmount")
    public ResponseMessage updateAmount(@RequestBody @Validated({Insert.class}) CartGoodsDto cartGoodsDto) {
        boolean res = cartService.updateAmount(cartGoodsDto);
        if (res) {
            return ResponseMessage.newSuccessInstance("更新购物车商品数量成功");
        } else {
            return ResponseMessage.newErrorInstance("更新购物车商品失败，该商品不存在");
        }
    }

    /**
     *
     * @param goodsSkuDtoList:isGoods、sizeId
     * @return
     */
    @ApiOperation(value = "批量删除购物车商品",notes = "在goodsDtoList中传isGoods、sizeId集合，即欲删除的购物车商品集合")
    @DeleteMapping("/deleteCartGoods")
    public ResponseMessage deleteCartGoods(@RequestBody @Validated List<GoodsSkuDto> goodsSkuDtoList) {
        Boolean b = cartService.deleteCartGoods(goodsSkuDtoList);
        if (b) {
            return ResponseMessage.newSuccessInstance("删除购物车商品成功");
        } else {
            return ResponseMessage.newErrorInstance("删除购物车商品失败,欲删除的商品不存在");
        }
    }
}
