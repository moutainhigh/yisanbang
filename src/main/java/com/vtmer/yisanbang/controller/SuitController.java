package com.vtmer.yisanbang.controller;

import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.dto.GoodsDto;
import com.vtmer.yisanbang.dto.SuitDto;
import com.vtmer.yisanbang.service.SuitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/suit")
public class SuitController {
    @Autowired
    private SuitService suitService;

    @GetMapping("/selectAllSuit")
    // 查找所有套装
    public ResponseMessage selectAllSuit() {
        List<SuitDto> suitDtoList = suitService.selectAll();
        if (suitDtoList != null && !suitDtoList.isEmpty())
            return ResponseMessage.newSuccessInstance(suitDtoList, "查找成功");
        else
            return ResponseMessage.newErrorInstance("无套装信息，查找失败");
    }

    @GetMapping("/selectAllSuitOrderByTime")
    // 根据套装更新时间顺序显示商品
    public ResponseMessage selectAllSuitOrderByTime() {
        List<SuitDto> suitDtoList = suitService.selectSuitOrderByTime();
        if (suitDtoList != null && !suitDtoList.isEmpty())
            return ResponseMessage.newSuccessInstance(suitDtoList, "查找成功");
        else
            return ResponseMessage.newErrorInstance("无商品信息，查找失败");
    }

    @GetMapping("/selectAllSuitOrderByPrice")
    // 根据套装更新时间顺序显示商品
    public ResponseMessage selectAllGoodsOrderByPrice() {
        List<SuitDto> suitDtoList = suitService.selectSuitOrderByTime();
        if (suitDtoList != null && !suitDtoList.isEmpty())
            return ResponseMessage.newSuccessInstance(suitDtoList, "查找成功");
        else
            return ResponseMessage.newErrorInstance("无商品信息，查找失败");
    }

    @GetMapping("/selectAllSuitBySortId/{id}")
    // 根据套装分类查找商品
    public ResponseMessage selectAllSuitBySortId(@PathVariable("id") Integer sortId) {
        List<SuitDto> suitDtoList = suitService.selectSuitBySort(sortId);
        if (suitDtoList != null && !suitDtoList.isEmpty())
            return ResponseMessage.newSuccessInstance(suitDtoList, "查找成功");
        else
            return ResponseMessage.newErrorInstance("无套装信息，查找失败");
    }

    @GetMapping("/selectSuitById/{id}")
    // 根据套装id查找套装
    public ResponseMessage selectSuitById(@PathVariable("id") Integer goodsId) {
        SuitDto suitDto = suitService.selectSuitById(goodsId);
        if (suitDto != null)
            return ResponseMessage.newSuccessInstance(suitDto, "查找成功");
        else return ResponseMessage.newErrorInstance("该套装id错误");
    }

    @GetMapping("/selectSuitByName/{name}")
    // 根据套装名称查找套装
    public ResponseMessage selectSuitByName(@PathVariable("name") String goodsName) {
        SuitDto suit = suitService.selectSuitByName(goodsName);
        if (suit != null)
            return ResponseMessage.newSuccessInstance(suit, "查找成功");
        else return ResponseMessage.newErrorInstance("该套装名称不存在");
    }

    @PostMapping("/addSuit")
    // 添加套装
    public ResponseMessage addGoods(@RequestBody SuitDto suitDto) {
        List<SuitDto> suitDtoList = suitService.selectAll();
        boolean judgeFlag = suitService.judgeSuit(suitDto, suitDtoList);
        if (judgeFlag) return ResponseMessage.newErrorInstance("该套装名称已经存在");
        boolean addFlag = suitService.addSuit(suitDto);
        if (addFlag) return ResponseMessage.newSuccessInstance("添加成功");
        else return ResponseMessage.newErrorInstance("添加失败");
    }

    @DeleteMapping("/deleteSuit")
    // 删除套装
    public ResponseMessage deleteGoods(@RequestBody SuitDto suitDto) {
        SuitDto suit = suitService.selectSuitById(suitDto.getId());
        if (suit != null) {
            boolean deleteFlag = suitService.deleteSuitById(suitDto.getId());
            if (deleteFlag) return ResponseMessage.newSuccessInstance("删除成功");
            else return ResponseMessage.newErrorInstance("删除失败");
        } else return ResponseMessage.newSuccessInstance("该商品不存在");
    }

    @PutMapping("/updateSuit")
    // 更新套装
    public ResponseMessage updateSuit(@RequestBody SuitDto suitDto) {
        SuitDto suit = suitService.selectSuitById(suitDto.getId());
        if (suit != null) {
            boolean updateFlag = suitService.updateSuitById(suitDto);
            if (updateFlag) return ResponseMessage.newSuccessInstance("更新成功");
            else return ResponseMessage.newErrorInstance("更新失败");
        } else return ResponseMessage.newSuccessInstance("该商品不存在");
    }

    @PutMapping("/hideSuit")
    // 隐藏套装
    public ResponseMessage hideSuit(@RequestBody SuitDto suitDto) {
        SuitDto suit = suitService.selectSuitById(suitDto.getId());
        if (suit != null) {
            boolean hideFlag = suitService.hideSuit(suitDto);
            if (hideFlag) return ResponseMessage.newSuccessInstance("隐藏成功");
            else return ResponseMessage.newErrorInstance("隐藏失败");
        } else return ResponseMessage.newErrorInstance("该商品不存在");
    }
}
