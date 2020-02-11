package com.vtmer.yisanbang.controller;

import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.domain.Postage;
import com.vtmer.yisanbang.service.PostageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "邮费设置接口")
@RestController
@RequestMapping("/postage")
public class PostageController {

    @Autowired
    private PostageService postageService;

    @ApiOperation("获取邮费设置信息")
    @GetMapping("/get")
    public ResponseMessage<Postage> get() {
        Postage postage = postageService.get();
        if (postage!=null) {
            return ResponseMessage.newSuccessInstance(postage,"获取邮费设置成功");
        } else {
            return ResponseMessage.newSuccessInstance("目前暂无邮费设置");
        }
    }

    @ApiOperation(value = "添加邮费设置",notes = "同时只能存在一条邮费设置信息，不可重复添加，添加后可删除或更新" +
            "若不存在邮费设置，则默认满0元包邮")
    @PostMapping("/insert")
    public ResponseMessage insert(@RequestBody @Validated Postage postage) {
        int res = postageService.insert(postage);
        if (res == -1) {
            return ResponseMessage.newErrorInstance("已经设置了邮费计算规则");
        } else if (res == 1) {
            return ResponseMessage.newSuccessInstance("存入邮费计算规则成功");
        } else {
            return ResponseMessage.newErrorInstance("存入邮费计算规则失败");
        }
    }

    @ApiOperation(value = "删除邮费设置")
    @DeleteMapping("/delete")
    public ResponseMessage delete() {
        int res = postageService.delete();
        if (res == -1) {
            return ResponseMessage.newErrorInstance("目前暂无邮费计算规则可删除");
        } else if (res == 1) {
            return ResponseMessage.newSuccessInstance("删除当前邮费计算规则成功");
        } else {
            return ResponseMessage.newErrorInstance("删除邮费计算规则失败");
        }
    }

    @ApiOperation("更新邮费设置")
    @PutMapping("/update")
    public ResponseMessage update(@RequestBody @Validated Postage postage) {
        int res = postageService.update(postage);
        if (res == -1) {
            return ResponseMessage.newErrorInstance("目前暂无邮费计算规则可更新");
        } else if (res == 1) {
            return ResponseMessage.newSuccessInstance("更新邮费计算规则成功");
        } else {
            return ResponseMessage.newErrorInstance("更新邮费计算规则失败");
        }
    }
}
