package com.vtmer.yisanbang.controller;

import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.domain.User;
import com.vtmer.yisanbang.dto.UserAddressDto;
import com.vtmer.yisanbang.service.UserAddressService;
import com.vtmer.yisanbang.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("用户地址管理接口")
@RestController
@RequestMapping("/userAddress")
public class UserAddressController {

    @Autowired
    private UserAddressService userAddressService;

    @Autowired
    private UserService userService;

    /*
     * 根据用户id查看所有地址
     * */
    @GetMapping("/listUserAddress/{id}")
    @ApiOperation(value = "根据用户id查看该用户的所有地址")
    public ResponseMessage listUserAddress(@ApiParam(name = "userId", value = "用户Id", required = true) @PathVariable("id") Integer userId) {
        User user = userService.selectByPrimaryKey(userId);
        List<UserAddressDto> UserAdressDto = userAddressService.selectUserAddressByUserId(userId);
        if (user == null) {
            return ResponseMessage.newErrorInstance("该用户id错误");
        } else if (UserAdressDto != null) {
            return ResponseMessage.newSuccessInstance(UserAdressDto, "获取用户所有地址成功");
        } else {
            return ResponseMessage.newErrorInstance("该用户无地址");
        }
    }

    /*
     * 添加用户地址
     * */
    @PostMapping("/addUserAddress")
    @ApiOperation(value = "添加用户地址")
    public ResponseMessage insertUserAddress(@ApiParam(name = "用户地址Dto实体类", value = "传入Json格式", required = true)@RequestBody UserAddressDto userAddress) {
        List<UserAddressDto> userAddressDtos = userAddressService.selectUserAddressByUserId(userAddress.getUserId());
        boolean flag = userAddressService.JudegAddressContent(userAddress, userAddressDtos);
        if (flag) return ResponseMessage.newErrorInstance("该地址已存在");
        else {
            boolean insertFlag = userAddressService.InsertUserAddress(userAddress);
            if (insertFlag == true) return ResponseMessage.newSuccessInstance("添加成功");
            else return ResponseMessage.newErrorInstance("添加失败");
        }
    }

    /*
     * 根据用户地址id更新地址
     * */
    @PutMapping("/updateUserAddress")
    @ApiOperation(value = "根据用户地址id更新该地址")
    public ResponseMessage updateUserAddress(@ApiParam(name = "用户地址Dto实体类", value = "传入Json格式", required = true)@RequestBody UserAddressDto userAddress) {
        UserAddressDto address = userAddressService.selectUserAddressDtoByAddressId(userAddress.getId());
        if (address != null) {
            boolean flag = userAddressService.updateUserAddressByAddressId(userAddress);
            if (flag == true) return ResponseMessage.newSuccessInstance("更新成功");
            else return ResponseMessage.newErrorInstance("更新失败");
        } else return ResponseMessage.newErrorInstance("该地址id错误");
    }

    /*
     * 根据用户地址id删除地址
     * */
    @DeleteMapping("/deleteUserAddress")
    @ApiOperation(value = "根据用户地址id删除地址")
    public ResponseMessage deleteUserAddress(@ApiParam(name = "用户地址Dto实体类", value = "传入Json格式", required = true)@RequestBody UserAddressDto userAddress) {
        UserAddressDto address = userAddressService.selectUserAddressDtoByAddressId(userAddress.getId());
        if (address != null) {
            boolean flag = userAddressService.deleteUserAddressByAddressId(address);
            if (flag == true) return ResponseMessage.newSuccessInstance("删除成功");
            else return ResponseMessage.newErrorInstance("删除失败");
        } else return ResponseMessage.newErrorInstance("该地址id错误");
    }

    /*
     * 查找默认地址
     * */
    @GetMapping("/defaultUserAddress/{id}")
    @ApiOperation(value = "根据用户id查看该用户的默认地址")
    public ResponseMessage selectDefaultUserAddress(@ApiParam(name = "userId", value = "用户Id", required = true) @PathVariable("id") Integer userId) {
        User user = userService.selectByPrimaryKey(userId);
        UserAddressDto addressDto = userAddressService.selectDefaultUserAddress(userId);
        if (user == null) {
            return ResponseMessage.newErrorInstance("该用户id错误");
        } else if (addressDto == null) {
            return ResponseMessage.newErrorInstance("无默认地址");
        } else {
            return ResponseMessage.newSuccessInstance(addressDto, "查找成功");
        }
    }

    /*
     * 改变默认地址
     * */
    @PutMapping("/changeDefaultUserAddress")
    @ApiOperation(value = "改变用户默认地址")
    public ResponseMessage changeDefaultUserAddress(@ApiParam(name = "用户地址Dto实体类", value = "传入Json格式", required = true)@RequestBody UserAddressDto userAddress) {
        UserAddressDto addressDto = userAddressService.selectDefaultUserAddress(userAddress.getUserId());
        if (addressDto != null) {
            boolean changeFlag = userAddressService.changeDefaultUserAddress(addressDto, userAddress);
            if (changeFlag) return ResponseMessage.newSuccessInstance("改变成功");
            else return ResponseMessage.newErrorInstance("改变失败");
        } else return ResponseMessage.newErrorInstance("该地址错误");
    }
}
