package com.vtmer.yisanbang.controller;

import com.github.pagehelper.PageHelper;
import com.vtmer.yisanbang.common.PageResponseMessage;
import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.common.annotation.RequestLog;
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

    @RequestLog(module = "部件尺寸", operationDesc = "根据套装id查找所有该套装的部件尺寸")
    @GetMapping("/get/selectPartBySuitId")
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
        if (partSizeDtos != null && !partSizeDtos.isEmpty()) {
            return ResponseMessage.newSuccessInstance(PageResponseMessage.restPage(partSizeDtos), "查找成功");
        } else {
            return ResponseMessage.newErrorInstance("查找失败");
        }
    }

    @RequestLog(module = "部件尺寸", operationDesc = "根据部件尺寸id查找部件尺寸")
    @GetMapping("/get/selectPartById")
    @ApiOperation(value = "根据部件尺寸id查找部件尺寸")
    // 根据部件尺寸id查找部件尺寸
    public ResponseMessage selectPartById(@ApiParam(name = "partSizeId", value = "部件尺寸Id", required = true)
                                          @RequestParam(value = "partSizeId", defaultValue = "5") Integer partSizeId) {
        PartSizeDTO partSizeDto = partSizeService.selectPartSizeById(partSizeId);
        if (partSizeDto != null) {
            return ResponseMessage.newSuccessInstance(partSizeDto, "查找成功");
        } else {
            return ResponseMessage.newErrorInstance("查找失败，该套装id不存在");
        }
    }

    @RequestLog(module = "部件尺寸", operationDesc = "添加套装部件尺寸")
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
        if (judgeFlag) {
            return ResponseMessage.newErrorInstance("该套装部件尺寸已经存在");
        }
        boolean addFlag = partSizeService.addPartSize(partSizeDto);
        if (addFlag) {
            return ResponseMessage.newSuccessInstance("添加成功");
        } else {
            return ResponseMessage.newErrorInstance("添加失败");
        }
    }

    @RequestLog(module = "部件尺寸", operationDesc = "删除部件尺寸")
    @DeleteMapping("/deletePartSize")
    @ApiOperation(value = "删除部件尺寸")
    // 删除部件尺寸
    public ResponseMessage deletePartSize(@ApiParam(name = "部件尺寸Dto实体类", value = "传入Json格式", required = true)
                                          @RequestBody
                                          @Validated(Delete.class) PartSizeDTO partSizeDto) {
        PartSizeDTO partSize = partSizeService.selectPartSizeById(partSizeDto.getId());
        if (partSize != null) {
            boolean deleteFlag = partSizeService.deletePartSize(partSizeDto.getId());
            if (deleteFlag) {
                return ResponseMessage.newSuccessInstance("删除成功");
            } else {
                return ResponseMessage.newErrorInstance("删除失败");
            }
        } else {
            return ResponseMessage.newErrorInstance("该部件尺寸id错误");
        }
    }

    @RequestLog(module = "部件尺寸", operationDesc = "更新部件尺寸")
    @PutMapping("/updatePartSize")
    @ApiOperation(value = "更新部件尺寸")
    // 更新部件尺寸
    public ResponseMessage updatePartSize(@ApiParam(name = "部件尺寸Dto实体类", value = "传入Json格式", required = true)
                                          @RequestBody
                                          @Validated(Update.class) PartSizeDTO partSizeDto) {
        PartSizeDTO partSize = partSizeService.selectPartSizeById(partSizeDto.getId());
        if (partSize != null) {
            boolean updateFlag = partSizeService.updatePartSize(partSizeDto);
            if (updateFlag) {
                return ResponseMessage.newSuccessInstance("更新成功");
            } else {
                return ResponseMessage.newErrorInstance("更新失败");
            }
        } else {
            return ResponseMessage.newErrorInstance("该部件尺寸id错误");
        }
    }

    @RequestLog(module = "部件尺寸", operationDesc = "根据套装id查找该套装的所有部件")
    @GetMapping("/get/selectAllPartById")
    @ApiOperation(value = "根据套装id查找该套装的所有部件")
    // 根据套装id查找该套装的所有部件
    public ResponseMessage selectAllPartById(@ApiParam(name = "suitId", value = "套装Id", required = true)
                                             @RequestParam(value = "suitId", defaultValue = "5") Integer suitId) {
        List<PartSizeDTO> partSizeDtos = partSizeService.selectAllBySuitId(suitId);
        if (partSizeDtos != null) {
            List<String> list = partSizeService.selectAllPartById(suitId);
            if (list != null && !list.isEmpty()) {
                List uniqueList = list.stream().distinct().collect(Collectors.toList());
                return ResponseMessage.newSuccessInstance(uniqueList, "查找成功");
            } else {
                return ResponseMessage.newErrorInstance("查找失败");
            }
        } else {
            return ResponseMessage.newErrorInstance("该套装id错误，查找无结果");
        }
    }

    @RequestLog(module = "部件尺寸", operationDesc = "根据套装id查找该套装的所有尺寸")
    @GetMapping("/get/selectAllSizeById")
    @ApiOperation(value = "根据套装id查找该套装的所有尺寸")
    // 根据套装id查找该套装的所有尺寸
    public ResponseMessage selectAllSizeById(@ApiParam(name = "suitId", value = "套装Id", required = true)
                                             @RequestParam(value = "suitId", defaultValue = "5") Integer suitId) {
        List<PartSizeDTO> partSizeDtos = partSizeService.selectAllBySuitId(suitId);
        if (partSizeDtos != null) {
            List<String> list = partSizeService.selectAllSizeById(suitId);
            if (list != null && !list.isEmpty()) {
                List uniqueList = list.stream().distinct().collect(Collectors.toList());
                return ResponseMessage.newSuccessInstance(uniqueList, "查找成功");
            } else {
                return ResponseMessage.newErrorInstance("查找失败");
            }
        } else {
            return ResponseMessage.newErrorInstance("该套装id错误，查找无结果");
        }
    }

    @RequestLog(module = "部件尺寸", operationDesc = "根据部件尺寸返回该部件尺寸对应的库存")
    @GetMapping("/get/selectInventoryByPartSize")
    @ApiOperation(value = "根据部件尺寸返回该部件尺寸对应的库存")
    // 根据部件尺寸返回该部件尺寸对应的库存
    public ResponseMessage selectInventoryByPartSize(@ApiParam(name = "suitId", value = "套装Id", required = true)
                                                     @RequestParam(value = "suitId", defaultValue = "5") Integer suitId,
                                                     @ApiParam(name = "part", value = "套装部件", required = true)
                                                     @RequestParam(value = "part", defaultValue = "裤子") String part,
                                                     @ApiParam(name = "size", value = "套装大小", required = true)
                                                     @RequestParam(value = "size", defaultValue = "S") String size) {
        List<PartSizeDTO> partSizeDtos = partSizeService.selectAllBySuitId(suitId);
        if (partSizeDtos != null && !partSizeDtos.isEmpty()) {
            Integer inventory = partSizeService.selectInventoryByPartSize(suitId, part, size);
            if (inventory != null) {
                return ResponseMessage.newSuccessInstance(inventory, "查找成功");
            } else {
                return ResponseMessage.newErrorInstance("查找失败");
            }
        } else {
            return ResponseMessage.newErrorInstance("该套装id错误，查找无结果");
        }
    }

    @RequestLog(module = "部件尺寸", operationDesc = "根据部件尺寸返回该部件尺寸对应的价格")
    @GetMapping("/get/selectPriceByPartSize")
    @ApiOperation(value = "根据部件尺寸返回该部件尺寸对应的价格")
    // 根据部件尺寸返回该部件尺寸对应的价格
    public ResponseMessage selectPriceByPartSize(@ApiParam(name = "suitId", value = "套装Id", required = true)
                                                 @RequestParam(value = "suitId", defaultValue = "5") Integer suitId,
                                                 @ApiParam(name = "part", value = "套装部件", required = true)
                                                 @RequestParam(value = "part", defaultValue = "裤子") String part,
                                                 @ApiParam(name = "size", value = "套装大小", required = true)
                                                 @RequestParam(value = "size", defaultValue = "S") String size) {
        List<PartSizeDTO> partSizeDtos = partSizeService.selectAllBySuitId(suitId);
        if (partSizeDtos != null && !partSizeDtos.isEmpty()) {
            Double price = partSizeService.selectPriceByPartSize(suitId, part, size);
            if (price != null) {
                return ResponseMessage.newSuccessInstance(price, "查找成功");
            } else {
                return ResponseMessage.newErrorInstance("查找失败");
            }
        } else {
            return ResponseMessage.newErrorInstance("该套装id错误，查找无结果");
        }
    }

    @RequestLog(module = "部件尺寸", operationDesc = "返回套装内部件的最低价")
    @GetMapping("/get/selectLowPriceById")
    @ApiOperation(value = "返回套装内部件的最低价")
    // 返回套装内部件的最低价
    public ResponseMessage selectLowPriceById(@ApiParam(name = "suitId", value = "套装Id", required = true)
                                              @RequestParam(value = "suitId", defaultValue = "5") Integer suitId) {
        List<PartSizeDTO> partSizeDtos = partSizeService.selectAllBySuitId(suitId);
        if (partSizeDtos != null && !partSizeDtos.isEmpty()) {
            Double lowPrice = partSizeService.selectLowPriceBySuitId(suitId);
            if (lowPrice != null) {
                return ResponseMessage.newSuccessInstance(lowPrice, "查找成功");
            } else {
                return ResponseMessage.newErrorInstance("查找失败");
            }
        } else {
            return ResponseMessage.newErrorInstance("该套装id错误，查找无结果");
        }
    }

    @RequestLog(module = "部件尺寸", operationDesc = "返回套装内部件的最高价")
    @GetMapping("/get/selectHighPriceById")
    @ApiOperation(value = "返回套装内部件的最高价")
    // 返回套装内部件的最高价
    public ResponseMessage selectHighPriceById(@ApiParam(name = "suitId", value = "套装Id", required = true)
                                               @RequestParam(value = "suitId", defaultValue = "5") Integer suitId) {
        List<PartSizeDTO> partSizeDtos = partSizeService.selectAllBySuitId(suitId);
        if (partSizeDtos != null && !partSizeDtos.isEmpty()) {
            Double highPrice = partSizeService.selecgHighPriceBySuitId(suitId);
            if (highPrice != null) {
                return ResponseMessage.newSuccessInstance(highPrice, "查找成功");
            } else {
                return ResponseMessage.newErrorInstance("查找失败");
            }
        } else {
            return ResponseMessage.newErrorInstance("该套装id错误，查找无结果");
        }
    }

}
