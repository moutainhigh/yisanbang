package com.vtmer.yisanbang.controller;

import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.common.annotation.RequestLog;
import com.vtmer.yisanbang.common.validGroup.Insert;
import com.vtmer.yisanbang.common.validGroup.Update;
import com.vtmer.yisanbang.dto.CartGoodsDto;
import com.vtmer.yisanbang.dto.GoodsSkuDto;
import com.vtmer.yisanbang.service.CartService;
import com.vtmer.yisanbang.vo.CartVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
    @RequestLog(module = "购物车",operationDesc = "获取用户购物车商品列表")
    @ApiOperation(value = "获取用户的购物车商品列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "校验token",name = "accessToken",paramType = "header",required = true)
    })
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
    @RequestLog(module = "购物车",operationDesc = "添加商品进入购物车")
    @ApiOperation(value = "添加商品进入购物车")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "校验token",name = "accessToken",paramType = "header",required = true)
    })
    @PostMapping("/addCartGoods")
    public ResponseMessage addCartGoods(@RequestBody @Validated({Insert.class}) List<CartGoodsDto> cartGoodsDtoList) {
        logger.info("购物车添加商品，商品参数[{}]",cartGoodsDtoList);
        cartService.addCartGoods(cartGoodsDtoList);
        return ResponseMessage.newSuccessInstance("加入购物车成功");

    }

    /**
     * 更新购物车商品勾选接口
     * @param goodsSkuDto：colorSizeId、isGoods
     * @return
     */
    @RequestLog(module = "购物车",operationDesc = "更新购物车商品勾选")
    @ApiOperation(value = "更新购物车勾选",notes = "每次用户(取消)勾选请求该接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "校验token",name = "accessToken",paramType = "header",required = true)
    })
    @PutMapping("/updateChosen")
    public ResponseMessage updateChosen(@RequestBody @Validated GoodsSkuDto goodsSkuDto) {
        logger.info("更新购物车商品勾选，商品skuId[{}],是否为普通商品[{}]",
                goodsSkuDto.getColorSizeId(),goodsSkuDto.getIsGoods());
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
    @RequestLog(module = "购物车",operationDesc = "购物车商品增加1或删减1")
    @ApiOperation(value = "购物车商品增加1或删减1",notes = "用于购物车中+ -按钮，+ amount传1，- amount传-1")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "校验token",name = "accessToken",paramType = "header",required = true)
    })
    @PutMapping("/addOrSubtractAmount")
    public ResponseMessage addOrSubtractAmount(@RequestBody @Validated({Update.class}) CartGoodsDto cartGoodsDto) {
        logger.info("购物车商品增减1，商品skuId[{}],是否为普通商品[{}]，数量[{}]",
                cartGoodsDto.getColorSizeId(),cartGoodsDto.getIsGoods(),cartGoodsDto.getAmount());
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
    @RequestLog(module = "购物车",operationDesc = "购物车修改商品数量")
    @ApiOperation(value = "直接修改购物车某件商品的数量",notes = "amount传修改数量")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "校验token",name = "accessToken",paramType = "header",required = true)
    })
    @PutMapping("/updateAmount")
    public ResponseMessage updateAmount(@RequestBody @Validated({Insert.class}) CartGoodsDto cartGoodsDto) {
        logger.info("购物车商品修改数量，商品skuId[{}],是否为普通商品[{}]，数量[{}]",
                cartGoodsDto.getColorSizeId(),cartGoodsDto.getIsGoods(),cartGoodsDto.getAmount());
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
    @RequestLog(module = "购物车",operationDesc = "批量删除购物车商品")
    @ApiOperation(value = "批量删除购物车商品",notes = "在goodsDtoList中传isGoods、sizeId集合，即欲删除的购物车商品集合")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "校验token",name = "accessToken",paramType = "header",required = true)
    })
    @DeleteMapping("/deleteCartGoods")
    public ResponseMessage deleteCartGoods(@RequestBody @Validated List<GoodsSkuDto> goodsSkuDtoList) {
        logger.info("批量删除购物车商品，商品参数[{}]",goodsSkuDtoList);
        Boolean b = cartService.deleteCartGoods(goodsSkuDtoList);
        if (b) {
            return ResponseMessage.newSuccessInstance("删除购物车商品成功");
        } else {
            return ResponseMessage.newErrorInstance("删除购物车商品失败,欲删除的商品不存在");
        }
    }
}
