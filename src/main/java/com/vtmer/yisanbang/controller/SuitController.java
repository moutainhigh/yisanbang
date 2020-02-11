package com.vtmer.yisanbang.controller;

import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.common.validGroup.Delete;
import com.vtmer.yisanbang.common.validGroup.Update;
import com.vtmer.yisanbang.dto.SuitDto;
import com.vtmer.yisanbang.service.SuitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "套装管理接口")
@RestController
@RequestMapping("/suit")
public class SuitController {
    @Autowired
    private SuitService suitService;

    @GetMapping("/selectAllSuit")
    @ApiOperation(value = "查找显示所有套装")
    // 查找所有套装
    public ResponseMessage selectAllSuit() {
        List<SuitDto> suitDtoList = suitService.selectAll();
        if (suitDtoList != null && !suitDtoList.isEmpty())
            return ResponseMessage.newSuccessInstance(suitDtoList, "查找成功");
        else
            return ResponseMessage.newErrorInstance("无套装信息，查找失败");
    }

    @GetMapping("/selectAllSuitOrderByTime")
    @ApiOperation(value = "根据套装更新时间顺序查找显示套装")
    // 根据套装更新时间顺序显示套装
    public ResponseMessage selectAllSuitOrderByTime() {
        List<SuitDto> suitDtoList = suitService.selectSuitOrderByTime();
        if (suitDtoList != null && !suitDtoList.isEmpty())
            return ResponseMessage.newSuccessInstance(suitDtoList, "查找成功");
        else
            return ResponseMessage.newErrorInstance("无商品信息，查找失败");
    }

    @GetMapping("/selectAllSuitOrderByPrice")
    @ApiOperation(value = "根据套装价格顺序查找显示套装")
    // 根据套装价格顺序显示套装
    public ResponseMessage selectAllGoodsOrderByPrice() {
        List<SuitDto> suitDtoList = suitService.selectSuitOrderByPrice();
        if (suitDtoList != null && !suitDtoList.isEmpty())
            return ResponseMessage.newSuccessInstance(suitDtoList, "查找成功");
        else
            return ResponseMessage.newErrorInstance("无商品信息，查找失败");
    }

    @GetMapping("/selectAllSuitBySortId/{id}")
    @ApiOperation(value = "根据套装分类id查找套装")
    // 根据套装分类查找套装
    public ResponseMessage selectAllSuitBySortId(@ApiParam(name = "sortId", value = "分类Id", required = true)
                                                     @PathVariable("id") Integer sortId) {
        List<SuitDto> suitDtoList = suitService.selectSuitBySort(sortId);
        if (suitDtoList != null && !suitDtoList.isEmpty())
            return ResponseMessage.newSuccessInstance(suitDtoList, "查找成功");
        else
            return ResponseMessage.newErrorInstance("无套装信息，查找失败");
    }

    @GetMapping("/selectSuitById/{id}")
    @ApiOperation(value = "根据套装id查找套装")
    // 根据套装id查找套装
    public ResponseMessage selectSuitById(@ApiParam(name = "goodsId", value = "商品Id", required = true)
                                              @PathVariable("id") Integer goodsId) {
        SuitDto suitDto = suitService.selectSuitById(goodsId);
        if (suitDto != null)
            return ResponseMessage.newSuccessInstance(suitDto, "查找成功");
        else return ResponseMessage.newErrorInstance("该套装id错误");
    }

    @GetMapping("/selectSuitByName/{name}")
    @ApiOperation(value = "根据套装名称查找套装")
    // 根据套装名称查找套装
    public ResponseMessage selectSuitByName(@ApiParam(name = "goodsName", value = "商品名称", required = true)
                                                @PathVariable("name") String goodsName) {
        SuitDto suit = suitService.selectSuitByName(goodsName);
        if (suit != null)
            return ResponseMessage.newSuccessInstance(suit, "查找成功");
        else return ResponseMessage.newErrorInstance("该套装名称不存在");
    }

    @PostMapping("/addSuit")
    @ApiOperation(value = "添加套装")
    // 添加套装
    public ResponseMessage addGoods(@ApiParam(name = "套装Dto实体类", value = "传入Json格式", required = true)
                                        @RequestBody
                                            @Validated SuitDto suitDto) {
        List<SuitDto> suitDtoList = suitService.selectAll();
        boolean judgeFlag = suitService.judgeSuit(suitDto, suitDtoList);
        if (judgeFlag) return ResponseMessage.newErrorInstance("该套装名称已经存在");
        boolean addFlag = suitService.addSuit(suitDto);
        if (addFlag) return ResponseMessage.newSuccessInstance("添加成功");
        else return ResponseMessage.newErrorInstance("添加失败");
    }

    @DeleteMapping("/deleteSuit")
    @ApiOperation(value = "删除套装")
    // 删除套装
    public ResponseMessage deleteGoods(@ApiParam(name = "套装Dto实体类", value = "传入Json格式", required = true)
                                           @RequestBody
                                                @Validated(Delete.class) SuitDto suitDto) {
        SuitDto suit = suitService.selectSuitById(suitDto.getId());
        if (suit != null) {
            boolean deleteFlag = suitService.deleteSuitById(suitDto.getId());
            if (deleteFlag) return ResponseMessage.newSuccessInstance("删除成功");
            else return ResponseMessage.newErrorInstance("删除失败");
        } else return ResponseMessage.newSuccessInstance("该商品不存在");
    }

    @PutMapping("/updateSuit")
    @ApiOperation(value = "更新套装")
    // 更新套装
    public ResponseMessage updateSuit(@ApiParam(name = "套装Dto实体类", value = "传入Json格式", required = true)
                                          @RequestBody
                                                @Validated(Update.class) SuitDto suitDto) {
        List<SuitDto> suitDtoList = suitService.selectAll();
        boolean judgeFlag = suitService.judgeSuit(suitDto, suitDtoList);
        if (judgeFlag) return ResponseMessage.newErrorInstance("该套装名称已经存在");
        SuitDto suit = suitService.selectSuitById(suitDto.getId());
        if (suit != null) {
            boolean updateFlag = suitService.updateSuitById(suitDto);
            if (updateFlag) return ResponseMessage.newSuccessInstance("更新成功");
            else return ResponseMessage.newErrorInstance("更新失败");
        } else return ResponseMessage.newSuccessInstance("该商品不存在");
    }

    @PutMapping("/hideSuit")
    @ApiOperation(value = "隐藏套装，即下架")
    // 隐藏套装
    public ResponseMessage hideSuit(@ApiParam(name = "套装Dto实体类", value = "传入Json格式", required = true)
                                        @RequestBody
                                                @Validated(Update.class) SuitDto suitDto) {
        SuitDto suit = suitService.selectSuitById(suitDto.getId());
        if (suit != null) {
            boolean hideFlag = suitService.hideSuit(suitDto);
            if (hideFlag) return ResponseMessage.newSuccessInstance("隐藏成功");
            else return ResponseMessage.newErrorInstance("隐藏失败");
        } else return ResponseMessage.newErrorInstance("该商品不存在");
    }
}
