package com.vtmer.yisanbang.controller;

import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.dto.GoodsDTO;
import com.vtmer.yisanbang.dto.SuitDTO;
import com.vtmer.yisanbang.service.GoodsAndSuitService;
import com.vtmer.yisanbang.service.GoodsService;
import com.vtmer.yisanbang.service.SuitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "单件商品与套装商品管理接口")
@RestController
@RequestMapping("/goodsAndSuit")
public class GoodsSuitController {
    @Autowired
    private GoodsAndSuitService goodsAndSuitService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private SuitService suitService;

    @GetMapping("/selectGoodsAndSuit")
    @ApiOperation(value = "查找所有单件商品与套装商品信息")
    // 查找所有单件商品与套装商品信息
    public ResponseMessage selectGoodsAndSuit() {
        List<GoodsDTO> goodsDTOS = goodsService.selectAllShow();
        List<SuitDTO> suitDTOS = suitService.selectAllShow();
        if (goodsDTOS != null && !goodsDTOS.isEmpty() && suitDTOS != null && !suitDTOS.isEmpty()) {
            List<Object> list = goodsAndSuitService.selectGoodsAndSuit(goodsDTOS, suitDTOS);
            if (list != null && !list.isEmpty()) {
                List uniqueList = list.stream().distinct().collect(Collectors.toList());
                return ResponseMessage.newSuccessInstance(uniqueList, "查找成功");
            } else return ResponseMessage.newErrorInstance("查找失败");
        } else return ResponseMessage.newErrorInstance("查找失败");
    }

    @GetMapping("/selectGoodsAndSuitByPriceAsc")
    @ApiOperation(value = "根据价格从低到高显示单件商品与套装商品")
    // 根据价格从低到高显示单件商品与套装商品
    public ResponseMessage selectGoodsAndSuitByPriceAsc() {
        List<GoodsDTO> goodsDTOS = goodsService.selectAllShow();
        List<SuitDTO> suitDTOS = suitService.selectAllShow();
        if (goodsDTOS != null && !goodsDTOS.isEmpty() && suitDTOS != null && !suitDTOS.isEmpty()) {
            List<Object> list = goodsAndSuitService.selectGoodsAndSuitByPriceAsc(goodsDTOS, suitDTOS);
            if (list != null && !list.isEmpty()) {
                List uniqueList = list.stream().distinct().collect(Collectors.toList());
                return ResponseMessage.newSuccessInstance(uniqueList, "查找成功");
            } else return ResponseMessage.newErrorInstance("查找失败");
        } else return ResponseMessage.newErrorInstance("查找失败");
    }

    @GetMapping("/selectGoodsAndSuitByPriceDec")
    @ApiOperation(value = "根据价格从高到低显示单件商品与套装商品")
    // 根据价格从高到低显示单件商品与套装商品
    public ResponseMessage selectGoodsAndSuitByPriceDec() {
        List<GoodsDTO> goodsDTOS = goodsService.selectAllShow();
        List<SuitDTO> suitDTOS = suitService.selectAllShow();
        if (goodsDTOS != null && !goodsDTOS.isEmpty() && suitDTOS != null && !suitDTOS.isEmpty()) {
            List<Object> list = goodsAndSuitService.selectGoodsAndSuitByPriceDec(goodsDTOS, suitDTOS);
            if (list != null && !list.isEmpty()) {
                List uniqueList = list.stream().distinct().collect(Collectors.toList());
                return ResponseMessage.newSuccessInstance(uniqueList, "查找成功");
            } else return ResponseMessage.newErrorInstance("查找失败");
        } else return ResponseMessage.newErrorInstance("查找失败");
    }

    @GetMapping("/selectGoodsAndSuitByTimeAsc")
    @ApiOperation(value = "根据时间从低到高显示单件商品与套装商品")
    // 根据时间从低到高显示单件商品与套装商品
    public ResponseMessage selectGoodsAndSuitByTimeAsc() {
        List<GoodsDTO> goodsDTOS = goodsService.selectAllShow();
        List<SuitDTO> suitDTOS = suitService.selectAllShow();
        if (goodsDTOS != null && !goodsDTOS.isEmpty() && suitDTOS != null && !suitDTOS.isEmpty()) {
            List<Object> list = goodsAndSuitService.selectGoodsAndSuitByTimeAsc(goodsDTOS, suitDTOS);
            if (list != null && !list.isEmpty()) {
                List uniqueList = list.stream().distinct().collect(Collectors.toList());
                return ResponseMessage.newSuccessInstance(uniqueList, "查找成功");
            } else return ResponseMessage.newErrorInstance("查找失败");
        } else return ResponseMessage.newErrorInstance("查找失败");
    }

    @GetMapping("/selectGoodsAndSuitByTimeDec")
    @ApiOperation(value = "根据时间从高到低显示单件商品与套装商品")
    // 根据时间从高到低显示单件商品与套装商品
    public ResponseMessage selectGoodsAndSuitByTimeDec() {
        List<GoodsDTO> goodsDTOS = goodsService.selectAllShow();
        List<SuitDTO> suitDTOS = suitService.selectAllShow();
        if (goodsDTOS != null && !goodsDTOS.isEmpty() && suitDTOS != null && !suitDTOS.isEmpty()) {
            List<Object> list = goodsAndSuitService.selectGoodsAndSuitByTimeDec(goodsDTOS, suitDTOS);
            if (list != null && !list.isEmpty()) {
                List uniqueList = list.stream().distinct().collect(Collectors.toList());
                return ResponseMessage.newSuccessInstance(uniqueList, "查找成功");
            } else return ResponseMessage.newErrorInstance("查找失败");
        } else return ResponseMessage.newErrorInstance("查找失败");
    }
}
