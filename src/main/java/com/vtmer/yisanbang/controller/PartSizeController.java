package com.vtmer.yisanbang.controller;

import com.github.pagehelper.PageHelper;
import com.vtmer.yisanbang.common.PageResponseMessage;
import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.common.valid.group.Delete;
import com.vtmer.yisanbang.common.valid.group.Update;
import com.vtmer.yisanbang.dto.PartSizeDTO;
import com.vtmer.yisanbang.service.PartSizeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "部件尺寸管理接口")
@RestController
@RequestMapping("/partSize")
public class PartSizeController {
    @Autowired
    private PartSizeService partSizeService;

    @GetMapping("/selectAllPartSize")
    @ApiOperation(value = "查找所有部件尺寸")
    // 查找所有部件尺寸
    public ResponseMessage selectAll(@ApiParam("查询页数(第几页)")
                                     @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                     @ApiParam("单页数量")
                                     @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<PartSizeDTO> partSizeDtos = partSizeService.selectAll();
        if (partSizeDtos != null && !partSizeDtos.isEmpty())
            return ResponseMessage.newSuccessInstance(PageResponseMessage.restPage(partSizeDtos), "查找成功");
        else return ResponseMessage.newErrorInstance("查找失败");
    }

    @GetMapping("/selectPartBySuitId")
    @ApiOperation(value = "根据套装id查找所有该套装的部件尺寸")
    // 根据套装id查找所有该套装的部件尺寸
    public ResponseMessage selectPartBySuitId(@ApiParam(name = "suitId", value = "套装Id", required = true)
                                              @RequestParam(value = "suitId", defaultValue = "5") Integer suitId,
                                              @ApiParam("查询页数(第几页)")
                                              @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                              @ApiParam("单页数量")
                                              @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<PartSizeDTO> partSizeDtos = partSizeService.selectAllBySuitId(suitId);
        if (partSizeDtos != null && !partSizeDtos.isEmpty())
            return ResponseMessage.newSuccessInstance(PageResponseMessage.restPage(partSizeDtos), "查找成功");
        else return ResponseMessage.newErrorInstance("查找失败");
    }

    @GetMapping("/selectPartById/{id}")
    @ApiOperation(value = "根据部件尺寸id查找部件尺寸")
    // 根据部件尺寸id查找部件尺寸
    public ResponseMessage selectPartById(@ApiParam(name = "partSizeId", value = "部件尺寸Id", required = true)
                                          @PathVariable("id") Integer partSizeId) {
        PartSizeDTO partSizeDto = partSizeService.selectPartSizeById(partSizeId);
        if (partSizeDto != null)
            return ResponseMessage.newSuccessInstance(partSizeDto, "查找成功");
        else return ResponseMessage.newErrorInstance("查找失败，该套装id不存在");
    }

    @PostMapping("/addPartSize")
    @ApiOperation(value = "添加套装部件尺寸")
    // 添加套装部件尺寸
    public ResponseMessage addPartSize(@ApiParam(name = "部件尺寸Dto实体类", value = "传入Json格式", required = true)
                                       @RequestBody
                                       @Validated PartSizeDTO partSizeDto) {
        PartSizeDTO partSize = partSizeService.selectPartSizeById(partSizeDto.getId());
        if (partSize != null) {
            return ResponseMessage.newErrorInstance("该套装部件尺寸id已经存在");
        }
        boolean judgeFlag = partSizeService.judgePartSize(partSizeDto);
        if (judgeFlag) return ResponseMessage.newErrorInstance("该套装部件尺寸已经存在");
        boolean addFlag = partSizeService.addPartSize(partSizeDto);
        if (addFlag) return ResponseMessage.newSuccessInstance("添加成功");
        else return ResponseMessage.newErrorInstance("添加失败");
    }

    @DeleteMapping("/deletePartSize")
    @ApiOperation(value = "删除部件尺寸")
    // 删除部件尺寸
    public ResponseMessage deletePartSize(@ApiParam(name = "部件尺寸Dto实体类", value = "传入Json格式", required = true)
                                          @RequestBody
                                          @Validated(Delete.class) PartSizeDTO partSizeDto) {
        PartSizeDTO partSize = partSizeService.selectPartSizeById(partSizeDto.getId());
        if (partSize != null) {
            boolean deleteFlag = partSizeService.deletePartSize(partSizeDto.getId());
            if (deleteFlag) return ResponseMessage.newSuccessInstance("删除成功");
            else return ResponseMessage.newErrorInstance("删除失败");
        } else return ResponseMessage.newErrorInstance("该部件尺寸id错误");
    }

    @PutMapping("/updatePartSize")
    @ApiOperation(value = "更新部件尺寸")
    // 更新部件尺寸
    public ResponseMessage updatePartSize(@ApiParam(name = "部件尺寸Dto实体类", value = "传入Json格式", required = true)
                                          @RequestBody
                                          @Validated(Update.class) PartSizeDTO partSizeDto) {
        boolean judgeFlag = partSizeService.judgePartSize(partSizeDto);
        if (judgeFlag) return ResponseMessage.newErrorInstance("该套装部件尺寸已经存在");
        PartSizeDTO partSize = partSizeService.selectPartSizeById(partSizeDto.getId());
        if (partSize != null) {
            boolean updateFlag = partSizeService.updatePartSize(partSizeDto);
            if (updateFlag) return ResponseMessage.newSuccessInstance("更新成功");
            else return ResponseMessage.newErrorInstance("更新失败");
        } else return ResponseMessage.newErrorInstance("该部件尺寸id错误");
    }

    @GetMapping("/selectAllPartById")
    @ApiOperation(value = "根据套装id查找该套装的所有部件")
    // 根据套装id查找该套装的所有部件
    public ResponseMessage selectAllPartById(@ApiParam(name = "suitId", value = "套装Id", required = true)
                                             @RequestParam(value = "suitId", defaultValue = "5") Integer suitId,
                                             @ApiParam("查询页数(第几页)")
                                             @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                             @ApiParam("单页数量")
                                             @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<PartSizeDTO> partSizeDtos = partSizeService.selectAllBySuitId(suitId);
        if (partSizeDtos != null) {
            List<String> list = partSizeService.selectAllPartById(suitId);
            if (list != null && !list.isEmpty()) {
                List uniqueList = list.stream().distinct().collect(Collectors.toList());
                return ResponseMessage.newSuccessInstance(PageResponseMessage.restPage(uniqueList), "查找成功");
            } else {
                return ResponseMessage.newErrorInstance("查找失败");
            }
        } else {
            return ResponseMessage.newErrorInstance("该套装id错误，查找无结果");
        }
    }

    @GetMapping("/selectAllSizeById")
    @ApiOperation(value = "根据套装id查找该套装的所有尺寸")
    // 根据套装id查找该套装的所有尺寸
    public ResponseMessage selectAllSizeById(@ApiParam(name = "suitId", value = "套装Id", required = true)
                                             @RequestParam(value = "suitId", defaultValue = "5") Integer suitId,
                                             @ApiParam("查询页数(第几页)")
                                             @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                             @ApiParam("单页数量")
                                             @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<PartSizeDTO> partSizeDtos = partSizeService.selectAllBySuitId(suitId);
        if (partSizeDtos != null) {
            List<String> list = partSizeService.selectAllSizeById(suitId);
            if (list != null && !list.isEmpty()) {
                List uniqueList = list.stream().distinct().collect(Collectors.toList());
                return ResponseMessage.newSuccessInstance(PageResponseMessage.restPage(uniqueList), "查找成功");
            } else {
                return ResponseMessage.newErrorInstance("查找失败");
            }
        } else {
            return ResponseMessage.newErrorInstance("该套装id错误，查找无结果");
        }
    }

    @GetMapping("/selectInventoryByPartSize/{id}/{part}/{size}")
    @ApiOperation(value = "根据部件尺寸返回该部件尺寸对应的库存")
    // 根据部件尺寸返回该部件尺寸对应的库存
    public ResponseMessage selectInventoryByPartSize(@ApiParam(name = "suitId", value = "套装Id", required = true)
                                                     @PathVariable("id") Integer suitId,
                                                     @ApiParam(name = "part", value = "套装部件", required = true)
                                                     @PathVariable("part") String part,
                                                     @ApiParam(name = "size", value = "套装价格", required = true)
                                                     @PathVariable("size") String size) {
        List<PartSizeDTO> partSizeDtos = partSizeService.selectAllBySuitId(suitId);
        if (partSizeDtos != null && !partSizeDtos.isEmpty()) {
            Integer inventory = partSizeService.selectInventoryByPartSize(suitId, part, size);
            if (inventory != null)
                return ResponseMessage.newSuccessInstance(inventory, "查找成功");
            else
                return ResponseMessage.newErrorInstance("查找失败");
        } else return ResponseMessage.newErrorInstance("该套装id错误，查找无结果");
    }

    @GetMapping("/selectPriceByPartSize/{id}/{part}/{size}")
    @ApiOperation(value = "根据部件尺寸返回该部件尺寸对应的价格")
    // 根据部件尺寸返回该部件尺寸对应的价格
    public ResponseMessage selectPriceByPartSize(@ApiParam(name = "suitId", value = "套装Id", required = true)
                                                 @PathVariable("id") Integer suitId,
                                                 @ApiParam(name = "part", value = "套装部件", required = true)
                                                 @PathVariable("part") String part,
                                                 @ApiParam(name = "size", value = "套装价格", required = true)
                                                 @PathVariable("size") String size) {
        List<PartSizeDTO> partSizeDtos = partSizeService.selectAllBySuitId(suitId);
        if (partSizeDtos != null && !partSizeDtos.isEmpty()) {
            Double price = partSizeService.selectPriceByPartSize(suitId, part, size);
            if (price != null)
                return ResponseMessage.newSuccessInstance(price, "查找成功");
            else
                return ResponseMessage.newErrorInstance("查找失败");
        } else return ResponseMessage.newErrorInstance("该套装id错误，查找无结果");
    }

    @GetMapping("/selectLowPriceById/{id}")
    @ApiOperation(value = "返回套装内部件的最低价")
    // 返回套装内部件的最低价
    public ResponseMessage selectLowPriceById(@ApiParam(name = "suitId", value = "套装Id", required = true)
                                              @PathVariable("id") Integer suitId) {
        List<PartSizeDTO> partSizeDtos = partSizeService.selectAllBySuitId(suitId);
        if (partSizeDtos != null && !partSizeDtos.isEmpty()) {
            Double lowPrice = partSizeService.selectLowPriceBySuitId(suitId);
            if (lowPrice != null)
                return ResponseMessage.newSuccessInstance(lowPrice, "查找成功");
            else
                return ResponseMessage.newErrorInstance("查找失败");
        } else return ResponseMessage.newErrorInstance("该套装id错误，查找无结果");
    }

    @GetMapping("/selectHighPriceById/{id}")
    @ApiOperation(value = "返回套装内部件的最高价")
    // 返回套装内部件的最高价
    public ResponseMessage selectHighPriceById(@ApiParam(name = "suitId", value = "套装Id", required = true)
                                               @PathVariable("id") Integer suitId) {
        List<PartSizeDTO> partSizeDtos = partSizeService.selectAllBySuitId(suitId);
        if (partSizeDtos != null && !partSizeDtos.isEmpty()) {
            Double highPrice = partSizeService.selecgHighPriceBySuitId(suitId);
            if (highPrice != null)
                return ResponseMessage.newSuccessInstance(highPrice, "查找成功");
            else
                return ResponseMessage.newErrorInstance("查找失败");
        } else return ResponseMessage.newErrorInstance("该套装id错误，查找无结果");
    }

}
