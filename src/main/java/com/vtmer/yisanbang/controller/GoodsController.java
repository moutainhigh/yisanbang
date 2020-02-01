package com.vtmer.yisanbang.controller;

import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.dto.GoodsDto;
import com.vtmer.yisanbang.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    @GetMapping("/selectAllGoods")
    // 查找所有商品
    public ResponseMessage selectAllGoods() {
        List<GoodsDto> goodsDtos = goodsService.selectAllDto();
        if (goodsDtos != null && !goodsDtos.isEmpty())
            return ResponseMessage.newSuccessInstance(goodsDtos, "查找成功");
        else
            return ResponseMessage.newErrorInstance("无商品信息，查找失败");
    }

    @GetMapping("/selectGoodsById/{id}")
    // 根据商品id查找商品
    public ResponseMessage selectGoodsById(@PathVariable("id") Integer goodsId){
        GoodsDto goodsDto = goodsService.selectDtoByPrimaryKey(goodsId);
        if (goodsDto != null)
            return ResponseMessage.newSuccessInstance(goodsDto,"查找成功");
        else return ResponseMessage.newErrorInstance("该商品id错误");
    }

    @GetMapping("/selectGoodsByName/{name}")
    // 根据商品名称查找商品
    public ResponseMessage selectGoodsByName(@PathVariable("name") String goodsName){
        GoodsDto goodsDto = goodsService.selectDtoByGoodsName(goodsName);
        if (goodsDto != null) return ResponseMessage.newSuccessInstance(goodsDto,"查找成功");
        else return ResponseMessage.newErrorInstance("该商品名称不存在");
    }

    @PostMapping("/addGoods")
    // 添加商品
    public ResponseMessage addGoods(@RequestBody GoodsDto goodsDto) {
        List<GoodsDto> goodsDtos = goodsService.selectAllDto();
        boolean judgeFlag = goodsService.judgeGoods(goodsDto, goodsDtos);
        if (judgeFlag) return ResponseMessage.newErrorInstance("该商品名称已经存在");
        boolean addFlag = goodsService.addGoods(goodsDto);
        if (addFlag) return ResponseMessage.newSuccessInstance("添加成功");
        else return ResponseMessage.newErrorInstance("添加失败");
    }

    @DeleteMapping("/deleteGoods")
    // 删除商品
    public ResponseMessage deleteGoods(@RequestBody GoodsDto goodsDto) {
        GoodsDto goods = goodsService.selectDtoByPrimaryKey(goodsDto.getId());
        if (goods!= null) {
            boolean deleteFlag = goodsService.deleteGoodsById(goodsDto.getId());
            if (deleteFlag) return ResponseMessage.newSuccessInstance("删除成功");
            else return ResponseMessage.newErrorInstance("删除失败");
        }else return ResponseMessage.newSuccessInstance("该商品不存在");
    }

    @PutMapping("/updateGoods")
    // 更新商品
    public ResponseMessage updateGoods(@RequestBody GoodsDto goodsDto) {
        GoodsDto goods = goodsService.selectDtoByPrimaryKey(goodsDto.getId());
        if (goods!= null) {
            boolean updateFlag = goodsService.updateGoods(goodsDto);
            if (updateFlag) return ResponseMessage.newSuccessInstance("更新成功");
            else return ResponseMessage.newErrorInstance("更新失败");
        }else return ResponseMessage.newSuccessInstance("该商品不存在");
    }

    @PutMapping("/hideGoods")
    // 隐藏商品
    public ResponseMessage hideGoods(@RequestBody GoodsDto goodsDto) {
        GoodsDto goods = goodsService.selectDtoByPrimaryKey(goodsDto.getId());
        if (goods != null) {
            boolean hideFlag = goodsService.hideGoods(goodsDto);
            if (hideFlag) return ResponseMessage.newSuccessInstance("隐藏成功");
            else return ResponseMessage.newErrorInstance("隐藏失败");
        }else return ResponseMessage.newErrorInstance("该商品不存在");
    }
}
