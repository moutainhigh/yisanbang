package com.vtmer.yisanbang.controller;

import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.dto.ColorSizeDto;
import com.vtmer.yisanbang.dto.GoodsDto;
import com.vtmer.yisanbang.service.ColorSizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/colorSize")
public class ColorSizeController {
    @Autowired
    private ColorSizeService colorSizeService;

    @GetMapping("/selectAllColorSizeByGoodsId/{id}")
    // 查找所有颜色尺寸通过商品id
    public ResponseMessage selectAllColorSizeByGoodsId(@PathVariable("id") Integer goodsId) {
        List<ColorSizeDto> colorSizeDtos = colorSizeService.selectAllByGoodsId(goodsId);
        if (colorSizeDtos != null && !colorSizeDtos.isEmpty())
            return ResponseMessage.newSuccessInstance(colorSizeDtos, "查找成功");
        else
            return ResponseMessage.newErrorInstance("无商品颜色尺寸信息，查找失败");
    }

    @PostMapping("/addColorSize")
    // 添加商品颜色尺寸
    public ResponseMessage addColorSize(@RequestBody ColorSizeDto colorSizeDto) {
        ColorSizeDto colorSize = colorSizeService.selectColorSizeById(colorSizeDto.getId());
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
    // 更新商品颜色尺寸
    public ResponseMessage updateColorSize(@RequestBody ColorSizeDto colorSizeDto) {
        ColorSizeDto colorSize = colorSizeService.selectColorSizeById(colorSizeDto.getId());
        if (colorSize != null) {
            boolean updateFlag = colorSizeService.updateColorSize(colorSizeDto);
            if (updateFlag) return ResponseMessage.newSuccessInstance("更新成功");
            else return ResponseMessage.newErrorInstance("更新失败");
        } else return ResponseMessage.newErrorInstance("该商品颜色尺寸id不存在");
    }

    @DeleteMapping("/deleteColorSize")
    // 删除商品颜色尺寸
    public ResponseMessage deleteColorSize(@RequestBody ColorSizeDto colorSizeDto) {
        ColorSizeDto colorSize = colorSizeService.selectColorSizeById(colorSizeDto.getId());
        if (colorSize != null) {
            boolean deleteFlag = colorSizeService.deleteColorSize(colorSizeDto.getId());
            if (deleteFlag) return ResponseMessage.newSuccessInstance("删除成功");
            else return ResponseMessage.newErrorInstance("删除失败");
        } else return ResponseMessage.newErrorInstance("该商品颜色尺寸不存在");
    }

    @GetMapping("/selectAllColorById/{id}")
    // 查找所有颜色
    public ResponseMessage selectAllColorById(@PathVariable("id") Integer goodsId) {
        List<ColorSizeDto> colorSizeDtos = colorSizeService.selectAllByGoodsId(goodsId);
        if (colorSizeDtos != null) {
            List<String> list = colorSizeService.selectAllColorById(goodsId);
            if (list != null && !list.isEmpty()) {
                return ResponseMessage.newSuccessInstance(list, "查找成功");
            } else {
                return ResponseMessage.newErrorInstance("查找失败");
            }
        } else {
            return ResponseMessage.newErrorInstance("该商品id错误，查找无结果");
        }
    }

    @GetMapping("/selectAllSizeById/{id}")
    // 查找所有尺寸
    public ResponseMessage selectAllSizeById(@PathVariable("id") Integer goodsId) {
        List<ColorSizeDto> colorSizeDtos = colorSizeService.selectAllByGoodsId(goodsId);
        if (colorSizeDtos != null) {
            List<String> list = colorSizeService.selectAllSizeById(goodsId);
            if (list != null && !list.isEmpty()) {
                return ResponseMessage.newSuccessInstance(list, "查找成功");
            } else {
                return ResponseMessage.newErrorInstance("查找失败");
            }
        } else {
            return ResponseMessage.newErrorInstance("该商品id错误，查找无结果");
        }
    }

    @GetMapping("/selectInventoryByColorSize/{id}/{color}/{size}")
    // 根据颜色尺寸查找显示库存
    public ResponseMessage selectInventoryByColorSize(@PathVariable("id") Integer goodsId, @PathVariable("color") String color, @PathVariable("size") String size) {
        ColorSizeDto colorSizeDto = colorSizeService.selectColorSizeById(goodsId);
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
