package com.vtmer.yisanbang.controller;

import com.github.pagehelper.PageHelper;
import com.vtmer.yisanbang.common.PageResponseMessage;
import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.common.annotation.RequestLog;
import com.vtmer.yisanbang.domain.Sort;
import com.vtmer.yisanbang.dto.SortDTO;
import com.vtmer.yisanbang.dto.SuitSortWithChildrenSort;
import com.vtmer.yisanbang.service.SortService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "分类接口")
@RestController
@RequestMapping("/sort")
public class SortController {

    @Autowired
    private SortService sortService;

    @RequestLog(module = "商品分类", operationDesc = "添加分类信息")
    @ApiOperation("添加分类信息")
    @PostMapping("/add")
    public ResponseMessage addSortInfo(@Validated @RequestBody SortDTO sortDto) {
        if (sortService.isShowOrderExisted(sortDto.getShowOrder(), sortDto.getIsSuit(), sortDto.getParentId())) {
            return ResponseMessage.newErrorInstance("显示顺序已存在，分类信息添加失败");
        }
        int count = sortService.addSortInfo(sortDto);
        if (count > 0) {
            return ResponseMessage.newSuccessInstance("分类信息添加成功");
        }
        return ResponseMessage.newErrorInstance("分类信息添加失败");
    }

    @RequestLog(module = "商品分类", operationDesc = "查询指定分类信息")
    @ApiOperation("根据id查询分类信息")
    @GetMapping("/get/{sortId}")
    public ResponseMessage getSortInfo(@PathVariable("sortId") Integer sortId) {
        Sort sort = sortService.listSortInfoById(sortId);
        if (sort != null) {
            return ResponseMessage.newSuccessInstance(sort, "查询指定分类信息成功");
        }
        return ResponseMessage.newErrorInstance("查询指定分类信息失败");
    }

    @RequestLog(module = "商品分类", operationDesc = "查询所有校服分类")
    @ApiOperation(value = "查询校服分类", notes = "(默认不分页，传入分页相关参数则返回分页信息)")
    @GetMapping("/get/uniformSort")
    public ResponseMessage getAllUniformSortInfo(@ApiParam("查询页数(第几页)") @Param(value = "pageNum") Integer pageNum,
                                                 @ApiParam("单页数量") @Param(value = "pageSize") Integer pageSize) {
        if (pageNum != null && pageSize != null && pageNum != 0 && pageSize != 0) {
            PageHelper.startPage(pageNum, pageSize);
            List<Sort> sorts = sortService.listAllUniformSort();
            if (sorts != null) {
                return ResponseMessage.newSuccessInstance(PageResponseMessage.restPage(sorts), "查询所有校服分类信息成功");
            }
            return ResponseMessage.newErrorInstance("查询校服分类信息失败");
        } else {
            List<Sort> sorts = sortService.listAllUniformSort();
            if (sorts != null) {
                return ResponseMessage.newSuccessInstance(sorts, "查询所有校服分类信息成功");
            }
            return ResponseMessage.newErrorInstance("查询校服分类信息失败");
        }
    }

    @RequestLog(module = "商品分类", operationDesc = "查询职业装分类")
    @ApiOperation(value = "查询职业装分类", notes = "按分类等级查询(默认不分页，传入分页相关参数则返回分页信息)")
    @GetMapping("/get/suitSort/{parentId}")
    public ResponseMessage getAllSuitSortInfo(@ApiParam("上级分类id(若查询一级分类则为0)") @PathVariable("parentId") Integer parentId,
                                              @ApiParam("查询页数(第几页)") @Param(value = "pageNum") Integer pageNum,
                                              @ApiParam("单页数量") @Param(value = "pageSize") Integer pageSize) {
        if (pageNum != null && pageSize != null && pageNum != 0 && pageSize != 0) {
            PageHelper.startPage(pageNum, pageSize);
            List<Sort> sorts = sortService.listSuitSort(parentId);
            if (sorts != null) {
                return ResponseMessage.newSuccessInstance(PageResponseMessage.restPage(sorts), "查询职业装分类信息成功");
            }
            return ResponseMessage.newErrorInstance("查询校服分类信息失败");
        } else {
            List<Sort> sorts = sortService.listSuitSort(parentId);
            if (sorts != null) {
                return ResponseMessage.newSuccessInstance(sorts, "查询职业装分类信息成功");
            }
            return ResponseMessage.newErrorInstance("查询校服分类信息失败");
        }

    }

    @RequestLog(module = "商品分类", operationDesc = "查询所有职业装一级分类及其二级分类")
    @ApiOperation(value = "查询所有职业装一级分类及其所包含二级分类", notes = "返回分类id，名称，显示排序，是否显示")
    @GetMapping("/get/suitSortWithChild")
    public ResponseMessage getAllSuitSortWithChildInfo() {
        List<SuitSortWithChildrenSort> sorts = sortService.listAllSuitSortWithChildren();
        if (sorts != null) {
            return ResponseMessage.newSuccessInstance(sorts, "查询所有职业装一级分类及其所包含二级分类成功");
        }
        return ResponseMessage.newErrorInstance("查询所有职业装一级分类及其所包含二级分类失败");
    }

    @RequestLog(module = "商品分类", operationDesc = "修改分类信息")
    @ApiOperation("根据id修改分类信息")
    @PutMapping("/{sortId}")
    public ResponseMessage updateSortInfo(@PathVariable("sortId") Integer sortId, @Validated @RequestBody SortDTO sortDto){
        if (!sortDto.getShowOrder().equals(sortService.listSortInfoById(sortId).getShowOrder()) && sortService.isShowOrderExisted(sortDto.getShowOrder(), sortDto.getIsSuit(), sortDto.getParentId())) {
            return ResponseMessage.newErrorInstance("显示顺序已存在，分类信息修改失败");
        }
        int count = sortService.updateSortInfo(sortId, sortDto);
        if (count > 0) {
            return ResponseMessage.newSuccessInstance("分类信息修改成功");
        }
        return ResponseMessage.newErrorInstance("分类信息修改失败");
    }

    @RequestLog(module = "商品分类", operationDesc = "修改分类显示状态")
    @ApiOperation("根据id修改分类是否显示")
    @PutMapping("/isShowed/{sortId}")
    public ResponseMessage updateSortIsShowed(@PathVariable("sortId") Integer sortId) {
        if (sortService.updateIsShowed(sortId)) {
            return ResponseMessage.newSuccessInstance("分类显示状态修改成功");
        }
        return ResponseMessage.newErrorInstance("分类显示状态修改失败");
    }

    @RequestLog(module = "商品分类", operationDesc = "删除分类信息")
    @ApiOperation("根据id删除分类")
    @DeleteMapping("/{sortId}")
    public ResponseMessage deleteSort(@PathVariable("sortId") Integer sortId) {
        int count = sortService.deleteSort(sortId);
        if (count > 0) {
            return ResponseMessage.newSuccessInstance("分类删除成功");
        }
        return ResponseMessage.newErrorInstance("分类删除失败");
    }

}

