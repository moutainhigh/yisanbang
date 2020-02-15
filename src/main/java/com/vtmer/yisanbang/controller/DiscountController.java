package com.vtmer.yisanbang.controller;

import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.common.annotation.RequestLog;
import com.vtmer.yisanbang.common.exception.api.ApiException;
import com.vtmer.yisanbang.common.exception.api.discount.ApiDiscountExistException;
import com.vtmer.yisanbang.common.exception.api.discount.ApiDiscountNotFoundException;
import com.vtmer.yisanbang.common.exception.service.discount.DiscountExistException;
import com.vtmer.yisanbang.common.exception.service.discount.DiscountNotFoundException;
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
        try {
            discountService.insert(discount);
        } catch (DiscountExistException e) {
            throw new ApiDiscountExistException(e.getMessage());
        } catch (Exception e) {
            throw new ApiException(e);
        }
        return ResponseMessage.newSuccessInstance("插入打折数据成功");
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
        try {
            discountService.update(discount);
        } catch (DiscountNotFoundException e) {
            throw new ApiDiscountNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new ApiException(e);
        }
        return ResponseMessage.newSuccessInstance("修改打折优惠信息设置成功");
    }

    @RequestLog(module = "优惠规则设置",operationDesc = "获取打折优惠信息设置")
    @ApiOperation("获取打折优惠信息设置")
    @GetMapping("/get")
    public ResponseMessage<Discount> get() {
        Discount discount = discountService.selectDiscount();
        if (discount!=null) {
            return ResponseMessage.newSuccessInstance(discount,"获取打折信息成功");
        } else {
            return ResponseMessage.newSuccessInstance("暂无打折数据");
        }
    }

    @RequestLog(module = "优惠规则设置",operationDesc = "删除打折优惠信息设置")
    @ApiOperation("删除打折优惠信息设置")
    @DeleteMapping("/delete")
    public ResponseMessage delete() {
        try {
            discountService.deleteDiscount();
        } catch (DiscountNotFoundException e) {
            throw new ApiDiscountNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new ApiException(e);
        }
        return ResponseMessage.newSuccessInstance("删除打折设置成功");
    }
}
