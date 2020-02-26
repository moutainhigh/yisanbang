package com.vtmer.yisanbang.controller;

import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.common.annotation.RequestLog;
import com.vtmer.yisanbang.common.exception.api.ApiException;
import com.vtmer.yisanbang.common.exception.api.cart.ApiCartGoodsNotExistException;
import com.vtmer.yisanbang.common.exception.service.cart.CartGoodsNotExistException;
import com.vtmer.yisanbang.common.valid.group.Insert;
import com.vtmer.yisanbang.common.valid.group.Update;
import com.vtmer.yisanbang.dto.CartGoodsDTO;
import com.vtmer.yisanbang.dto.GoodsSkuDTO;
import com.vtmer.yisanbang.service.CartService;
import com.vtmer.yisanbang.vo.CartVO;
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

@Api(tags = "购物车接口",value = "用户部分")
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
    @ApiImplicitParams({
            @ApiImplicitParam(value = "校验token", name = "Authorization", paramType = "header", required = true)
    })
    @ApiOperation(value = "获取用户的购物车商品列表")
    @GetMapping("/listCartGoods")
    public ResponseMessage<CartVO> listCartGoods() {
        CartVO cartVo = cartService.selectCartVo();
        if (cartVo !=null) {
            return ResponseMessage.newSuccessInstance(cartVo,"获取购物车商品列表成功");
        } else {
            return ResponseMessage.newSuccessInstance("购物车为空");
        }
    }

    /**
     * 添加商品进入购物车接口
     * @param cartGoodsDTOList：添加进购物车商品列表
     * @return
     */
    @RequestLog(module = "购物车",operationDesc = "添加商品进入购物车")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "校验token", name = "Authorization", paramType = "header", required = true)
    })
    @ApiOperation(value = "添加商品进入购物车")
    @PostMapping("/addCartGoods")
    public ResponseMessage addCartGoods(@RequestBody @Validated({Insert.class}) List<CartGoodsDTO> cartGoodsDTOList) {
        cartService.addCartGoods(cartGoodsDTOList);
        return ResponseMessage.newSuccessInstance("加入购物车成功");
    }

    /**
     * 更新购物车商品勾选接口
     * @param goodsSkuDTO：colorSizeId、isGoods
     * @return
     */
    @RequestLog(module = "购物车",operationDesc = "更新购物车商品勾选")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "校验token", name = "Authorization", paramType = "header", required = true)
    })
    @ApiOperation(value = "更新购物车勾选",notes = "每次用户(取消)勾选请求该接口")
    @PutMapping("/updateChosen")
    public ResponseMessage updateChosen(@RequestBody @Validated GoodsSkuDTO goodsSkuDTO) {
        try {
            cartService.updateChosen(goodsSkuDTO);
        } catch (CartGoodsNotExistException e) {
            throw new ApiCartGoodsNotExistException(e.getMessage());
        } catch (Exception e) {
            throw new ApiException(e);
        }
        return ResponseMessage.newSuccessInstance("修改勾选成功");
    }

    /**
     * 购物车商品增加1或删减1接口
     * @param cartGoodsDto:colorSizeId、isGoods、amount
     * @return
     */
    @RequestLog(module = "购物车",operationDesc = "购物车商品增加1或删减1")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "校验token", name = "Authorization", paramType = "header", required = true)
    })
    @ApiOperation(value = "购物车商品增加1或删减1",notes = "用于购物车中+ -按钮，+ amount传1，- amount传-1")
    @PutMapping("/addOrSubtractAmount")
    public ResponseMessage addOrSubtractAmount(@RequestBody @Validated({Update.class}) CartGoodsDTO cartGoodsDto) {
        try {
            cartService.addOrSubtractAmount(cartGoodsDto);
        } catch (CartGoodsNotExistException e) {
            throw new ApiCartGoodsNotExistException(e.getMessage());
        } catch (Exception e) {
            throw new ApiException(e);
        }
        return ResponseMessage.newSuccessInstance("更新购物车商品数量成功");
    }

    /**
     * 购物车商品直接修改商品数量接口
     * @param cartGoodsDto：colorSizeId、isGoods、amount
     * @return
     */
    @RequestLog(module = "购物车",operationDesc = "购物车修改商品数量")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "校验token", name = "Authorization", paramType = "header", required = true)
    })
    @ApiOperation(value = "直接修改购物车某件商品的数量",notes = "amount传修改数量")
    @PutMapping("/updateAmount")
    public ResponseMessage updateAmount(@RequestBody @Validated({Insert.class}) CartGoodsDTO cartGoodsDto) {
        try {
            cartService.updateAmount(cartGoodsDto);
        } catch (CartGoodsNotExistException e) {
            throw new ApiCartGoodsNotExistException(e.getMessage());
        } catch (Exception e) {
            throw new ApiException(e);
        }
        return ResponseMessage.newSuccessInstance("更新购物车商品数量成功");
    }

    /**
     *
     * @param goodsSkuDTOList:isGoods、sizeId
     * @return
     */
    @RequestLog(module = "购物车",operationDesc = "批量删除购物车商品")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "校验token", name = "Authorization", paramType = "header", required = true)
    })
    @ApiOperation(value = "批量删除购物车商品")
    @DeleteMapping("/deleteCartGoods")
    public ResponseMessage deleteCartGoods(@RequestBody @Validated List<GoodsSkuDTO> goodsSkuDTOList) {
        try {
            cartService.deleteCartGoods(goodsSkuDTOList);
        } catch (CartGoodsNotExistException e) {
            throw new ApiCartGoodsNotExistException(e.getMessage());
        } catch (Exception e) {
            throw new ApiException(e);
        }
        return ResponseMessage.newSuccessInstance("删除购物车商品成功");
    }
}
