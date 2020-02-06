package com.vtmer.yisanbang.controller;

import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.domain.BusinessAddress;
import com.vtmer.yisanbang.service.BusinessAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping("/update")
    public ResponseMessage update(@RequestBody @Validated BusinessAddress businessAddress) {
        if (businessAddress.getId() == null) {
            return ResponseMessage.newErrorInstance("收货地址id为空");
        }
        int res = businessAddressService.update(businessAddress);
        if (res == 1) {
            return ResponseMessage.newSuccessInstance("修改收货地址成功");
        } else {
            return ResponseMessage.newErrorInstance("修改收货地址失败");
        }
    }

    @PutMapping("/updateDefault/{id}")
    public ResponseMessage updateDefault(@PathVariable("id") Integer id) {
        int res = businessAddressService.updateDefault(id);
        if (res == 1) {
            return ResponseMessage.newSuccessInstance("修改默认收货地址成功");
        } else if (res == -1){
            return ResponseMessage.newErrorInstance("收货地址id不存在");
        } else {
            return ResponseMessage.newErrorInstance("修改默认收货地址失败");
        }
    }

    @GetMapping("/getAll")
    public ResponseMessage getAll() {
        List<BusinessAddress> businessAddresses = businessAddressService.selectAll();
        if (businessAddresses != null && businessAddresses.size()!=0) {
            return ResponseMessage.newSuccessInstance(businessAddresses,"获取收货地址列表成功");
        } else {
            return ResponseMessage.newSuccessInstance("目前暂无收货地址");
        }
    }

    @GetMapping("/getDefault")
    public ResponseMessage getDefault() {
        BusinessAddress businessAddress = businessAddressService.getDefault();
        if (businessAddress!=null) {
            return ResponseMessage.newSuccessInstance(businessAddress,"获取默认收货地址成功");
        } else {
            return ResponseMessage.newSuccessInstance("目前暂无默认收货地址");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseMessage delete(@PathVariable("id") Integer id) {
        int res = businessAddressService.deleteById(id);
        if (res == -1) {
            return ResponseMessage.newErrorInstance("地址id不存在");
        } else if (res == 1) {
            return ResponseMessage.newSuccessInstance("删除收货地址成功");
        } else {
            return ResponseMessage.newErrorInstance("删除收货地址失败");
        }
    }
}
