package com.vtmer.yisanbang.controller;

import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.common.annotation.RequestLog;
import com.vtmer.yisanbang.common.exception.api.ApiException;
import com.vtmer.yisanbang.common.exception.api.postage.ApiPostageSettingsExistException;
import com.vtmer.yisanbang.common.exception.api.postage.ApiPostageSettingsNotFoundException;
import com.vtmer.yisanbang.common.exception.service.postage.PostageSettingsExistException;
import com.vtmer.yisanbang.common.exception.service.postage.PostageSettingsNotFoundException;
import com.vtmer.yisanbang.domain.Postage;
import com.vtmer.yisanbang.service.PostageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "邮费设置接口",value = "后台管理部分")
@RestController
@RequestMapping("/postage")
public class PostageController {

    @Autowired
    private PostageService postageService;

    @RequestLog(module = "邮费设置",operationDesc = "获取邮费设置信息")
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

    @RequestLog(module = "邮费设置",operationDesc = "添加邮费设置")
    @ApiOperation(value = "添加邮费设置",notes = "同时只能存在一条邮费设置信息，不可重复添加，添加后可删除或更新" +
            "若不存在邮费设置，则默认满0元包邮")
    @PostMapping("/insert")
    public ResponseMessage insert(@RequestBody @Validated Postage postage) {
        try {
            postageService.insert(postage);
        } catch (PostageSettingsExistException e) {
            throw new ApiPostageSettingsExistException(e.getMessage());
        } catch (Exception e) {
            throw new ApiException(e);
        }
        return ResponseMessage.newSuccessInstance("存入邮费计算规则成功");
    }

    @RequestLog(module = "邮费设置",operationDesc = "删除邮费设置")
    @ApiOperation(value = "删除邮费设置")
    @DeleteMapping("/delete")
    public ResponseMessage delete() {
        try {
            postageService.delete();
        } catch (PostageSettingsNotFoundException e) {
            throw new ApiPostageSettingsNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new ApiException(e);
        }
        return ResponseMessage.newSuccessInstance("删除当前邮费计算规则成功");
    }

    @RequestLog(module = "邮费设置",operationDesc = "更新邮费设置")
    @ApiOperation("更新邮费设置")
    @PutMapping("/update")
    public ResponseMessage update(@RequestBody @Validated Postage postage) {
        try {
            postageService.update(postage);
        } catch (PostageSettingsNotFoundException e) {
            throw new ApiPostageSettingsNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new ApiException(e);
        }
        return ResponseMessage.newSuccessInstance("更新邮费计算规则成功");
    }
}
