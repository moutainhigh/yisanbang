package com.vtmer.yisanbang.controller;

import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.common.annotation.RequestLog;
import com.vtmer.yisanbang.domain.Discount;
import com.vtmer.yisanbang.service.DiscountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "优惠规则设置接口")
@RestController
@RequestMapping("/discount")
public class DiscountController {



    @Autowired
    private DiscountService discountService;

    /**
     * 插入打折信息
     * @param discount：amount、discountRate
     * @return
     */
    @RequestLog(module = "优惠规则设置",operationDesc = "添加打折优惠信息设置")
    @ApiOperation(value = "添加打折优惠信息设置",
            notes = "满amount件打discountRate*10折，discountRate范围在0-1之间\n" +
            "同时只能存在一种优惠信息设置，重复添加无效，添加后可选择更新或删除")
    @PostMapping("/insert")
    public ResponseMessage insert(@RequestBody @Validated Discount discount) {
        int res = discountService.insert(discount);
        if (res == 1) {
            return ResponseMessage.newSuccessInstance("插入打折数据成功");
        } else if (res == -1){
            return ResponseMessage.newErrorInstance("优惠信息已设置，请勿重复插入打折数据");
        } else {
            return ResponseMessage.newErrorInstance("插入打折数据异常");
        }
    }

    /**
     * 修改打折信息
     * @param discount
     * @return
     */
    @RequestLog(module = "优惠规则设置",operationDesc = "修改打折优惠信息设置")
    @ApiOperation("修改打折优惠信息设置")
    @PutMapping("/update")
    public ResponseMessage update(@RequestBody @Validated Discount discount) {
        int res = discountService.update(discount);
        if (res == 1) {
            return ResponseMessage.newSuccessInstance("修改打折数据成功");
        } else {
            return ResponseMessage.newErrorInstance("修改打折数据失败");
        }
    }

    @RequestLog(module = "优惠规则设置",operationDesc = "获取打折优惠信息设置")
    @ApiOperation("获取打折优惠信息设置")
    @GetMapping("/get")
    public ResponseMessage<Discount> get() {
        Discount discount = discountService.selectDiscount();
        if (discount!=null) {
            return ResponseMessage.newSuccessInstance(discount,"获取打折信息成功");
        } else {
            return ResponseMessage.newErrorInstance("暂无打折数据");
        }
    }

    @RequestLog(module = "优惠规则设置",operationDesc = "删除打折优惠信息设置")
    @ApiOperation("删除打折优惠信息设置")
    @DeleteMapping("/delete")
    public ResponseMessage delete() {
        int res = discountService.deleteDiscount();
        if (res == 1) {
            return ResponseMessage.newSuccessInstance("删除打折设置成功");
        } else {
            return ResponseMessage.newErrorInstance("删除打折设置失败");
        }
    }
}
