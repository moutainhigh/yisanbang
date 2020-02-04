package com.vtmer.yisanbang.controller;

import com.github.pagehelper.PageHelper;
import com.vtmer.yisanbang.common.PageResponseMessage;
import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.domain.Sort;
import com.vtmer.yisanbang.dto.SortDto;
import com.vtmer.yisanbang.dto.SuitSortWithChildrenSort;
import com.vtmer.yisanbang.service.SortService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sort")
public class SortController {

    @Autowired
    private SortService sortService;

    @PostMapping("/add")
    public ResponseMessage addSortInfo(@RequestBody SortDto sortDto) {
        if (sortService.isShowOrderExisted(sortDto.getShowOrder())) {
            return ResponseMessage.newErrorInstance("显示顺序已存在，分类信息添加失败");
        }
        int count = sortService.addSortInfo(sortDto);
        if (count > 0) {
            return ResponseMessage.newSuccessInstance("分类信息添加成功");
        }
        return ResponseMessage.newErrorInstance("分类信息添加失败");
    }

    @GetMapping("/{sortId}")
    public ResponseMessage getSortInfo(@PathVariable("sortId") Integer sortId) {
        Sort sort = sortService.listSortInfoById(sortId);
        if (sort != null) {
            return ResponseMessage.newSuccessInstance(sort, "查询指定分类信息成功");
        }
        return ResponseMessage.newErrorInstance("查询指定分类信息失败");
    }

    @GetMapping("/uniformSort")
    public ResponseMessage getAllUniformSortInfo(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                 @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Sort> sorts = sortService.listAllUniformSort();
        if (sorts != null) {
            return ResponseMessage.newSuccessInstance(PageResponseMessage.restPage(sorts), "查询所有校服分类信息成功");
        }
        return ResponseMessage.newErrorInstance("查询校服分类信息失败");
    }

    @GetMapping("/suitSort/{parentId}")
    public ResponseMessage getAllSuitSortInfo(@PathVariable("parentId") Integer parentId,
                                              @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                              @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Sort> sorts = sortService.listSuitSort(parentId);
        if (sorts != null) {
            return ResponseMessage.newSuccessInstance(PageResponseMessage.restPage(sorts), "查询职业装分类信息成功");
        }
        return ResponseMessage.newErrorInstance("查询职业装分类信息失败");
    }

    @GetMapping("/suitSortWithChild")
    public ResponseMessage getAllSuitSortWithChildInfo() {
        List<SuitSortWithChildrenSort> sorts = sortService.listAllSuitSortWithChildren();
        if (sorts != null) {
            return ResponseMessage.newSuccessInstance(PageResponseMessage.restPage(sorts), "查询所有职业装一级分类及其所包含二级分类成功");
        }
        return ResponseMessage.newErrorInstance("查询所有职业装一级分类及其所包含二级分类失败");
    }

    @PutMapping("/{sortId}")
    public ResponseMessage updateSortInfo(@PathVariable("sortId") Integer sortId, @RequestBody SortDto sortDto){
        if (!sortDto.getShowOrder().equals(sortService.listSortInfoById(sortId).getShowOrder()) && sortService.isShowOrderExisted(sortDto.getShowOrder())) {
            return ResponseMessage.newErrorInstance("显示顺序已存在，分类信息修改失败");
        }
        int count = sortService.updateSortInfo(sortId, sortDto);
        if (count > 0) {
            return ResponseMessage.newSuccessInstance("分类信息修改成功");
        }
        return ResponseMessage.newErrorInstance("分类信息修改失败");
    }

    @PutMapping("/isShowed/{sortId}")
    public ResponseMessage updateSortIsShowed(@PathVariable("sortId") Integer sortId) {
        if (sortService.updateIsShowed(sortId)) {
            return ResponseMessage.newSuccessInstance("分类显示状态修改成功");
        }
        return ResponseMessage.newErrorInstance("分类显示状态修改失败");
    }

    @DeleteMapping("/{sortId}")
    public ResponseMessage deleteSort(@PathVariable("sortId") Integer sortId) {
        int count = sortService.deleteSort(sortId);
        if (count > 0) {
            return ResponseMessage.newSuccessInstance("分类删除成功");
        }
        return ResponseMessage.newErrorInstance("分类删除失败");
    }

}
