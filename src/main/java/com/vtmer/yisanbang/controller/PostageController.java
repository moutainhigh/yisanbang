package com.vtmer.yisanbang.controller;

import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.domain.Postage;
import com.vtmer.yisanbang.service.PostageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/postage")
public class PostageController {

    @Autowired
    private PostageService postageService;

    @GetMapping("/get")
    public ResponseMessage get() {
        Postage postage = postageService.get();
        if (postage!=null) {
            return ResponseMessage.newSuccessInstance(postage,"获取邮费设置成功");
        } else {
            return ResponseMessage.newSuccessInstance("目前暂无邮费设置");
        }
    }

    @PostMapping("/insert")
    public ResponseMessage insert(@RequestBody Postage postage) {
        if (postage != null) {
            int res = postageService.insert(postage);
            if (res == -1) {
                return ResponseMessage.newErrorInstance("已经设置了邮费计算规则");
            } else if (res == 1) {
                return ResponseMessage.newSuccessInstance("存入邮费计算规则成功");
            } else {
                return ResponseMessage.newErrorInstance("存入邮费计算规则失败");
            }
        } else return ResponseMessage.newErrorInstance("传入参数有误");
    }

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

    @PutMapping("/update")
    public ResponseMessage update(@RequestBody Postage postage) {
        if (postage!=null) {
            int res = postageService.update(postage);
            if (res == -1) {
                return ResponseMessage.newErrorInstance("目前暂无邮费计算规则可更新");
            } else if (res == 1) {
                return ResponseMessage.newSuccessInstance("更新邮费计算规则成功");
            } else {
                return ResponseMessage.newErrorInstance("更新邮费计算规则失败");
            }
        } else return ResponseMessage.newErrorInstance("传入参数有误");
    }
}
