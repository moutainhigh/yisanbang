package com.vtmer.yisanbang.controller;

import com.vtmer.yisanbang.common.PageResponseMessage;
import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.common.util.PageUtil;
import com.vtmer.yisanbang.dto.GoodsDTO;
import com.vtmer.yisanbang.dto.SuitDTO;
import com.vtmer.yisanbang.service.GoodsAndSuitService;
import com.vtmer.yisanbang.service.GoodsService;
import com.vtmer.yisanbang.service.SuitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
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

    @GetMapping("/get/selectGoodsAndSuit")
    @ApiOperation(value = "查找所有单件商品与套装商品信息")
    // 查找所有单件商品与套装商品信息
    public ResponseMessage selectGoodsAndSuit(@ApiParam("查询页数(第几页)")
                                              @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                              @ApiParam("单页数量")
                                              @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        List<GoodsDTO> goodsDTOS = goodsService.selectAllShow();
        List<SuitDTO> suitDTOS = suitService.selectAllShow();
        if (goodsDTOS != null && !goodsDTOS.isEmpty() && suitDTOS != null && !suitDTOS.isEmpty()) {
            List<Object> list = goodsAndSuitService.selectGoodsAndSuit(goodsDTOS, suitDTOS);
            if (list != null && !list.isEmpty()) {
                List uniqueList = list.stream().distinct().collect(Collectors.toList());
                List pager = PageUtil.Pager(pageSize, pageNum, uniqueList);
                return ResponseMessage.newSuccessInstance(PageResponseMessage.restPage(pager), "查找成功");
            } else return ResponseMessage.newErrorInstance("查找失败");
        } else return ResponseMessage.newErrorInstance("查找失败");
    }

    @GetMapping("/get/selectGoodsAndSuitByPrice")
    @ApiOperation(value = "根据价格显示单件商品与套装商品")
    // 根据价格显示单件商品与套装商品
    public ResponseMessage selectGoodsAndSuitByPriceAsc(@ApiParam(name = "ifDec", value = "降序标志", required = true)
                                                        @RequestParam(value = "ifDec", defaultValue = "1") Integer ifDec,
                                                        @ApiParam("查询页数(第几页)")
                                                        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                        @ApiParam("单页数量")
                                                        @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        List<GoodsDTO> goodsDTOS = goodsService.selectAllShow();
        List<SuitDTO> suitDTOS = suitService.selectAllShow();
        if (goodsDTOS != null && !goodsDTOS.isEmpty() && suitDTOS != null && !suitDTOS.isEmpty()) {
            List<Object> list = goodsAndSuitService.selectGoodsAndSuitByPrice(goodsDTOS, suitDTOS);
            if (list != null && !list.isEmpty()) {
                List uniqueList = list.stream().distinct().collect(Collectors.toList());
                if (ifDec > 0)
                    Collections.reverse(uniqueList);
                List pager = PageUtil.Pager(pageSize, pageNum, uniqueList);
                return ResponseMessage.newSuccessInstance(PageResponseMessage.restPage(pager), "查找成功");
            } else return ResponseMessage.newErrorInstance("查找失败");
        } else return ResponseMessage.newErrorInstance("查找失败");
    }

    @GetMapping("/get/selectGoodsAndSuitByTime")
    @ApiOperation(value = "根据时间显示单件商品与套装商品")
    // 根据时间显示单件商品与套装商品
    public ResponseMessage selectGoodsAndSuitByTime(@ApiParam(name = "ifDec", value = "降序标志", required = true)
                                                    @RequestParam(value = "ifDec", defaultValue = "1") Integer ifDec,
                                                    @ApiParam("查询页数(第几页)")
                                                    @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                    @ApiParam("单页数量")
                                                    @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        List<GoodsDTO> goodsDTOS = goodsService.selectAllShow();
        List<SuitDTO> suitDTOS = suitService.selectAllShow();
        if (goodsDTOS != null && !goodsDTOS.isEmpty() && suitDTOS != null && !suitDTOS.isEmpty()) {
            List<Object> list = goodsAndSuitService.selectGoodsAndSuitByTime(goodsDTOS, suitDTOS);
            if (list != null && !list.isEmpty()) {
                List uniqueList = list.stream().distinct().collect(Collectors.toList());
                if (ifDec > 0)
                    Collections.reverse(uniqueList);
                List pager = PageUtil.Pager(pageSize, pageNum, uniqueList);
                return ResponseMessage.newSuccessInstance(PageResponseMessage.restPage(pager), "查找成功");
            } else return ResponseMessage.newErrorInstance("查找失败");
        } else return ResponseMessage.newErrorInstance("查找失败");
    }

    @GetMapping("/get/selectGoodsAndSuitBySortId")
    @ApiOperation(value = "根据分类id查找所有单件商品与套装商品信息")
    // 根据分类id查找所有单件商品与套装商品信息
    public ResponseMessage selectGoodsAndSuitBySortId(@ApiParam(name = "sortId", value = "分类Id", required = true)
                                                      @RequestParam(value = "sortId", defaultValue = "5") Integer sortId,
                                                      @ApiParam("查询页数(第几页)")
                                                      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                      @ApiParam("单页数量")
                                                      @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        List<GoodsDTO> goodsDTOS = goodsService.selectAllDtoBySort(sortId);
        List<SuitDTO> suitDTOS = suitService.selectSuitBySort(sortId);
        if (goodsDTOS != null && !goodsDTOS.isEmpty() && suitDTOS != null && !suitDTOS.isEmpty()) {
            List<Object> list = goodsAndSuitService.selectGoodsAndSuit(goodsDTOS, suitDTOS);
            if (list != null && !list.isEmpty()) {
                List uniqueList = list.stream().distinct().collect(Collectors.toList());
                List pager = PageUtil.Pager(pageSize, pageNum, uniqueList);
                return ResponseMessage.newSuccessInstance(PageResponseMessage.restPage(pager), "查找成功");
            } else return ResponseMessage.newErrorInstance("查找失败");
        } else return ResponseMessage.newErrorInstance("查找失败");
    }

    @GetMapping("/get/selectGoodsAndSuitBySortIdAndPrice")
    @ApiOperation(value = "根据分类id与价格显示单件商品与套装商品")
    // 根据分类id与价格显示单件商品与套装商品
    public ResponseMessage selectGoodsAndSuitBySortIdAndPrice(@ApiParam(name = "sortId", value = "分类Id", required = true)
                                                              @RequestParam(value = "sortId", defaultValue = "5") Integer sortId,
                                                              @ApiParam(name = "ifDec", value = "降序标志", required = true)
                                                              @RequestParam(value = "ifDec", defaultValue = "1") Integer ifDec,
                                                              @ApiParam("查询页数(第几页)")
                                                              @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                              @ApiParam("单页数量")
                                                              @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        List<GoodsDTO> goodsDTOS = goodsService.selectAllDtoBySortOrderByPrice(sortId);
        List<SuitDTO> suitDTOS = suitService.selectSuitBySortIdOrderByPrice(sortId);
        if (goodsDTOS != null && !goodsDTOS.isEmpty() && suitDTOS != null && !suitDTOS.isEmpty()) {
            List<Object> list = goodsAndSuitService.selectGoodsAndSuitByPrice(goodsDTOS, suitDTOS);
            if (list != null && !list.isEmpty()) {
                List uniqueList = list.stream().distinct().collect(Collectors.toList());
                if (ifDec > 0)
                    Collections.reverse(uniqueList);
                List pager = PageUtil.Pager(pageSize, pageNum, uniqueList);
                return ResponseMessage.newSuccessInstance(PageResponseMessage.restPage(pager), "查找成功");
            } else return ResponseMessage.newErrorInstance("查找失败");
        } else return ResponseMessage.newErrorInstance("查找失败");
    }

    @GetMapping("/get/selectGoodsAndSuitBySortIdAndTime")
    @ApiOperation(value = "根据分类id与时间显示单件商品与套装商品")
    // 根据分类id与时间显示单件商品与套装商品
    public ResponseMessage selectGoodsAndSuitBySortIdAndTime(@ApiParam(name = "sortId", value = "分类Id", required = true)
                                                             @RequestParam(value = "sortId", defaultValue = "5") Integer sortId,
                                                             @ApiParam(name = "ifDec", value = "降序标志", required = true)
                                                             @RequestParam(value = "ifDec", defaultValue = "1") Integer ifDec,
                                                             @ApiParam("查询页数(第几页)")
                                                             @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                             @ApiParam("单页数量")
                                                             @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        List<GoodsDTO> goodsDTOS = goodsService.selectAllDtoBySortOrderByTime(sortId);
        List<SuitDTO> suitDTOS = suitService.selectSuitBySortIdOrderByTime(sortId);
        if (goodsDTOS != null && !goodsDTOS.isEmpty() && suitDTOS != null && !suitDTOS.isEmpty()) {
            List<Object> list = goodsAndSuitService.selectGoodsAndSuitByTime(goodsDTOS, suitDTOS);
            if (list != null && !list.isEmpty()) {
                List uniqueList = list.stream().distinct().collect(Collectors.toList());
                if (ifDec > 0)
                    Collections.reverse(uniqueList);
                List pager = PageUtil.Pager(pageSize, pageNum, uniqueList);
                return ResponseMessage.newSuccessInstance(PageResponseMessage.restPage(pager), "查找成功");
            } else return ResponseMessage.newErrorInstance("查找失败");
        } else return ResponseMessage.newErrorInstance("查找失败");
    }

    @GetMapping("/get/selectGoodsAndSuitByContent")
    @ApiOperation(value = "根据名称与简介查找并显示单件商品与套装商品")
    // 根据名称与简介查找并显示单件商品与套装商品
    public ResponseMessage selectGoodsAndSuitBySortIdAndTimeDec(@ApiParam(name = "content", value = "查找内容", required = true)
                                                                @RequestParam(value = "content", defaultValue = "学生") String content,
                                                                @ApiParam("查询页数(第几页)")
                                                                @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                                @ApiParam("单页数量")
                                                                @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        List<GoodsDTO> goodsDTOS = goodsService.selectDtoByContent(content);
        List<SuitDTO> suitDTOS = suitService.selectSuitByContent(content);
        if (goodsDTOS != null && !goodsDTOS.isEmpty() && suitDTOS != null && !suitDTOS.isEmpty()) {
            List<Object> list = goodsAndSuitService.selectGoodsAndSuit(goodsDTOS, suitDTOS);
            if (list != null && !list.isEmpty()) {
                List uniqueList = list.stream().distinct().collect(Collectors.toList());
                List pager = PageUtil.Pager(pageSize, pageNum, uniqueList);
                return ResponseMessage.newSuccessInstance(PageResponseMessage.restPage(pager), "查找成功");
            } else return ResponseMessage.newErrorInstance("查找失败");
        } else return ResponseMessage.newErrorInstance("查找失败");
    }

}
