package com.vtmer.yisanbang.controller;

import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.domain.BusinessAddress;
import com.vtmer.yisanbang.service.BusinessAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/businessAddress")
public class BusinessAddressController {

    @Autowired
    private BusinessAddressService businessAddressService;

    @PostMapping("/insert")
    public ResponseMessage insert(@RequestBody @Validated BusinessAddress businessAddress) {
        int res = businessAddressService.insert(businessAddress);
        if (res == 1) {
            return ResponseMessage.newSuccessInstance("添加收货地址成功");
        } else {
            return ResponseMessage.newErrorInstance("添加收货地址失败");
        }
    }
}
