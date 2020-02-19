package com.vtmer.yisanbang.controller;

import com.github.pagehelper.PageHelper;
import com.vtmer.yisanbang.common.PageResponseMessage;
import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.common.valid.group.Delete;
import com.vtmer.yisanbang.common.valid.group.Update;
import com.vtmer.yisanbang.dto.ColorSizeDTO;
import com.vtmer.yisanbang.service.ColorSizeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "颜色尺寸管理接口")
@RestController
@RequestMapping("/colorSize")
public class ColorSizeController {
    @Autowired
    private ColorSizeService colorSizeService;

    @GetMapping("/selectAllColorSizeByGoodsId")
    @ApiOperation(value = "根据商品id查找该商品的所有颜色尺寸")
    // 查找所有颜色尺寸通过商品id
    public ResponseMessage selectAllColorSizeByGoodsId(@ApiParam(name = "goodsId", value = "商品Id", required = true)
                                                       @RequestParam(value = "goodsId", defaultValue = "5") Integer goodsId,
                                                       @ApiParam("查询页数(第几页)")
                                                       @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                       @ApiParam("单页数量")
                                                       @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ColorSizeDTO> colorSizeDtos = colorSizeService.selectAllByGoodsId(goodsId);
        if (colorSizeDtos != null && !colorSizeDtos.isEmpty())
            return ResponseMessage.newSuccessInstance(PageResponseMessage.restPage(colorSizeDtos), "查找成功");
        else
            return ResponseMessage.newErrorInstance("无商品颜色尺寸信息，查找失败");
    }

    @PostMapping("/addColorSize")
    @ApiOperation(value = "添加商品颜色尺寸")
    // 添加商品颜色尺寸
    public ResponseMessage addColorSize(@ApiParam(name = "颜色尺寸Dto实体类", value = "传入Json格式", required = true)
                                        @RequestBody
                                        @Validated ColorSizeDTO colorSizeDto) {
        ColorSizeDTO colorSize = colorSizeService.selectColorSizeById(colorSizeDto.getId());
        if (colorSize != null) {
            return ResponseMessage.newErrorInstance("该商品颜色尺寸id已经存在");
        }
        boolean judgeFlag = colorSizeService.judgeColorSize(colorSizeDto);
        if (judgeFlag) return ResponseMessage.newErrorInstance("该商品颜色尺寸已经存在");
        boolean addFlag = colorSizeService.addColorSize(colorSizeDto);
        if (addFlag) return ResponseMessage.newSuccessInstance("添加成功");
        else return ResponseMessage.newErrorInstance("添加失败");
    }

    @PutMapping("/updateColorSize")
    @ApiOperation(value = "更新商品颜色尺寸")
    // 更新商品颜色尺寸
    public ResponseMessage updateColorSize(@ApiParam(name = "颜色尺寸Dto实体类", value = "传入Json格式", required = true)
                                           @RequestBody
                                           @Validated(Update.class) ColorSizeDTO colorSizeDto) {
        ColorSizeDTO colorSize = colorSizeService.selectColorSizeById(colorSizeDto.getId());
        if (colorSize != null) {
            boolean updateFlag = colorSizeService.updateColorSize(colorSizeDto);
            if (updateFlag) return ResponseMessage.newSuccessInstance("更新成功");
            else return ResponseMessage.newErrorInstance("更新失败");
        } else return ResponseMessage.newErrorInstance("该商品颜色尺寸id不存在");
    }

    @DeleteMapping("/deleteColorSize")
    @ApiOperation(value = "删除商品颜色尺寸")
    // 删除商品颜色尺寸
    public ResponseMessage deleteColorSize(@ApiParam(name = "颜色尺寸Dto实体类", value = "传入Json格式", required = true)
                                           @RequestBody
                                           @Validated(Delete.class) ColorSizeDTO colorSizeDto) {
        ColorSizeDTO colorSize = colorSizeService.selectColorSizeById(colorSizeDto.getId());
        if (colorSize != null) {
            boolean deleteFlag = colorSizeService.deleteColorSize(colorSizeDto.getId());
            if (deleteFlag) return ResponseMessage.newSuccessInstance("删除成功");
            else return ResponseMessage.newErrorInstance("删除失败");
        } else return ResponseMessage.newErrorInstance("该商品颜色尺寸不存在");
    }

    @GetMapping("/selectAllColorById")
    @ApiOperation(value = "根据商品id查找该商品的所有颜色")
    // 查找所有颜色
    public ResponseMessage selectAllColorById(@ApiParam(name = "goodsId", value = "商品Id", required = true)
                                              @RequestParam(value = "goodsId", defaultValue = "5") Integer goodsId,
                                              @ApiParam("查询页数(第几页)")
                                              @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                              @ApiParam("单页数量")
                                              @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ColorSizeDTO> colorSizeDtos = colorSizeService.selectAllByGoodsId(goodsId);
        if (colorSizeDtos != null) {
            List<String> list = colorSizeService.selectAllColorById(goodsId);
            if (list != null && !list.isEmpty()) {
                List uniqueList = list.stream().distinct().collect(Collectors.toList());
                return ResponseMessage.newSuccessInstance(PageResponseMessage.restPage(uniqueList), "查找成功");
            } else {
                return ResponseMessage.newErrorInstance("查找失败");
            }
        } else {
            return ResponseMessage.newErrorInstance("该商品id错误，查找无结果");
        }
    }

    @GetMapping("/selectAllSizeById")
    @ApiOperation(value = "根据商品id查找该商品的所有尺寸")
    // 查找所有尺寸
    public ResponseMessage selectAllSizeById(@ApiParam(name = "goodsId", value = "商品Id", required = true)
                                             @RequestParam(value = "goodsId", defaultValue = "5") Integer goodsId,
                                             @ApiParam("查询页数(第几页)")
                                             @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                             @ApiParam("单页数量")
                                             @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ColorSizeDTO> colorSizeDtos = colorSizeService.selectAllByGoodsId(goodsId);
        if (colorSizeDtos != null) {
            List<String> list = colorSizeService.selectAllSizeById(goodsId);
            if (list != null && !list.isEmpty()) {
                List uniqueList = list.stream().distinct().collect(Collectors.toList());
                return ResponseMessage.newSuccessInstance(PageResponseMessage.restPage(uniqueList), "查找成功");
            } else {
                return ResponseMessage.newErrorInstance("查找失败");
            }
        } else {
            return ResponseMessage.newErrorInstance("该商品id错误，查找无结果");
        }
    }

    @GetMapping("/selectInventoryByColorSize/{id}/{color}/{size}")
    @ApiOperation(value = "根据颜色尺寸查找显示库存数量")
    // 根据颜色尺寸查找显示库存
    public ResponseMessage selectInventoryByColorSize(@ApiParam(name = "goodsId", value = "商品Id", required = true) @PathVariable("id") Integer goodsId,
                                                      @ApiParam(name = "color", value = "颜色", required = true) @PathVariable("color") String color,
                                                      @ApiParam(name = "size", value = "大小", required = true) @PathVariable("size") String size) {
        System.out.println(goodsId);
        System.out.println(color);
        System.out.println(size);
        ColorSizeDTO colorSizeDto = colorSizeService.selectColorSizeById(goodsId);
        if (colorSizeDto != null) {
            Integer inventory = colorSizeService.selectInventoryByColorSize(goodsId, color, size);
            if (inventory != null)
                return ResponseMessage.newSuccessInstance(inventory, "查找成功");
            else return ResponseMessage.newErrorInstance("查找失败");
        } else {
            return ResponseMessage.newErrorInstance("查找失败，该商品id错误");
        }
    }
}
