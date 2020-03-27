package com.vtmer.yisanbang.controller;

import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.common.annotation.RequestLog;
import com.vtmer.yisanbang.common.exception.api.ApiException;
import com.vtmer.yisanbang.common.exception.api.businessaddress.ApiDefaultAddressYetException;
import com.vtmer.yisanbang.common.exception.api.businessaddress.ApiNotFindBusinessAddressException;
import com.vtmer.yisanbang.common.exception.service.businessaddress.DefaultAddressYetException;
import com.vtmer.yisanbang.common.exception.service.businessaddress.NotFindBusinessAddressException;
import com.vtmer.yisanbang.domain.BusinessAddress;
import com.vtmer.yisanbang.dto.insert.InsertBusinessAddressDTO;
import com.vtmer.yisanbang.dto.update.UpdateBusinessAddressDTO;
import com.vtmer.yisanbang.service.BusinessAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "商家收货地址接口",value = "后台管理部分")
@Validated
@RestController
@RequestMapping("/businessAddress")
public class BusinessAddressController {

    @Autowired
    private BusinessAddressService businessAddressService;

    @RequestLog(module = "商家收货地址",operationDesc = "添加新的商家收货地址")
    @ApiOperation(value = "添加新的收货地址")
    @PostMapping("/insert")
    public ResponseMessage insert(@RequestBody @Validated InsertBusinessAddressDTO insertBusinessAddressDTO) {
        BusinessAddress businessAddress = new BusinessAddress();
        BeanUtils.copyProperties(insertBusinessAddressDTO,businessAddress);
        businessAddressService.insert(businessAddress);
        return ResponseMessage.newSuccessInstance("添加收货地址成功");
    }

    @RequestLog(module = "商家收货地址",operationDesc = "更新商家收货地址")
    @ApiOperation(value = "更新收货地址")
    @PutMapping("/update")
    public ResponseMessage update(@RequestBody @Validated UpdateBusinessAddressDTO updateBusinessAddressDTO) {
        BusinessAddress businessAddress = new BusinessAddress();
        BeanUtils.copyProperties(updateBusinessAddressDTO,businessAddress);
        try {
            businessAddressService.update(businessAddress);
        } catch (NotFindBusinessAddressException e) {
            throw new ApiNotFindBusinessAddressException("找不到该收货地址");
        } catch (Exception e) { // 未知错误
            throw new ApiException(e);
        }
        return ResponseMessage.newSuccessInstance("修改收货地址成功");
    }

    @RequestLog(module = "商家收货地址",operationDesc = "更新某一地址为默认收货地址")
    @ApiOperation("更新某一地址为默认收货地址")
    @PutMapping("/updateDefault/{id}")
    public ResponseMessage updateDefault(@ApiParam(name = "id",value = "商家收货地址信息id",example = "1",type = "int")
                                             @PathVariable Integer id) {
        try {
            businessAddressService.updateDefault(id);
        } catch (NotFindBusinessAddressException e) {
            throw new ApiNotFindBusinessAddressException("找不到该收货地址");
        } catch (DefaultAddressYetException e) {
            throw new ApiDefaultAddressYetException("该收货地址已经是默认收货地址");
        } catch (Exception e) { // 未知错误
            throw new ApiException(e);
        }
        return ResponseMessage.newSuccessInstance("修改默认收货地址成功");
    }

    @RequestLog(module = "商家收货地址",operationDesc = "获取商家的所有收货地址")
    @ApiOperation(value = "获取商家的所有收货地址")
    @GetMapping("/getAll")
    public ResponseMessage<List<BusinessAddress>> getAll() {
        List<BusinessAddress> businessAddresses = businessAddressService.selectAll();
        if (businessAddresses != null && businessAddresses.size()!=0) {
            return ResponseMessage.newSuccessInstance(businessAddresses,"获取收货地址列表成功");
        } else {
            return ResponseMessage.newSuccessInstance("目前暂无收货地址");
        }
    }

    @RequestLog(module = "商家收货地址",operationDesc = "获取商家的默认收货地址")
    @ApiOperation(value = "获取商家的默认收货地址",
            notes = "获取商家的默认收货地址,可在用户填写退款单时调用显示")
    @GetMapping("/getDefault")
    public ResponseMessage<BusinessAddress> getDefault() {
        BusinessAddress businessAddress = businessAddressService.getDefault();
        if (businessAddress!=null) {
            return ResponseMessage.newSuccessInstance(businessAddress,"获取默认收货地址成功");
        } else {
            return ResponseMessage.newSuccessInstance("目前暂无默认收货地址");
        }
    }

    @RequestLog(module = "商家收货地址",operationDesc = "删除商家收货地址信息")
    @ApiOperation(value = "删除商家收货地址信息")
    @DeleteMapping("/delete/{id}")
    public ResponseMessage delete(@ApiParam(name = "id",value = "商家收货地址id",example = "1",type = "int")
                                      @PathVariable Integer id) {
        try {
            businessAddressService.deleteById(id);
        } catch (NotFindBusinessAddressException e) {
            throw new ApiNotFindBusinessAddressException("找不到该收货地址");
        } catch (Exception e) { // 未知错误
            throw new ApiException(e);
        }
        return ResponseMessage.newSuccessInstance("删除收货地址成功");
    }
}
