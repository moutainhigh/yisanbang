package com.vtmer.yisanbang.controller;

import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.common.valid.group.Delete;
import com.vtmer.yisanbang.common.valid.group.Update;
import com.vtmer.yisanbang.domain.User;
import com.vtmer.yisanbang.dto.UserAddressDTO;
import com.vtmer.yisanbang.service.UserAddressService;
import com.vtmer.yisanbang.service.UserService;
import com.vtmer.yisanbang.shiro.JwtFilter;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "用户地址管理接口")
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
    @GetMapping("/listUserAddress")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "校验token", name = "Authorization", paramType = "header", required = true)
    })
    @ApiOperation(value = "根据用户id查看该用户的所有地址")
    public ResponseMessage listUserAddress() {
        Integer userId = JwtFilter.getLoginUser().getId();
        User user = userService.selectByPrimaryKey(userId);
        List<UserAddressDTO> UserAdressDto = userAddressService.selectUserAddressByUserId(userId);
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
    @ApiImplicitParams({
            @ApiImplicitParam(value = "校验token", name = "Authorization", paramType = "header", required = true)
    })
    @ApiOperation(value = "添加用户地址")
    public ResponseMessage insertUserAddress(@ApiParam(name = "用户地址Dto实体类", value = "传入Json格式", required = true)
                                             @RequestBody
                                             @Validated UserAddressDTO userAddress) {
        List<UserAddressDTO> userAddressDtos = userAddressService.selectUserAddressByUserId(userAddress.getUserId());
        if (userAddressDtos != null && !userAddressDtos.isEmpty()) {
            boolean flag = userAddressService.JudegAddressContent(userAddress, userAddressDtos);
            if (flag) {
                return ResponseMessage.newErrorInstance("该地址已存在");
            }
        }
        boolean insertFlag = userAddressService.InsertUserAddress(userAddress);
        if (insertFlag == true) {
            return ResponseMessage.newSuccessInstance("添加成功");
        } else {
            return ResponseMessage.newErrorInstance("添加失败");
        }
    }

    /*
     * 根据用户地址id更新地址
     * */
    @PutMapping("/updateUserAddress")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "校验token", name = "Authorization", paramType = "header", required = true)
    })
    @ApiOperation(value = "根据用户地址id更新该地址")
    public ResponseMessage updateUserAddress(@ApiParam(name = "用户地址Dto实体类", value = "传入Json格式", required = true)
                                             @RequestBody
                                             @Validated(Update.class) UserAddressDTO userAddress) {
        UserAddressDTO address = userAddressService.selectUserAddressDtoByAddressId(userAddress.getId());
        if (address != null) {
            UserAddressDTO userAddressDTO = userAddressService.selectDefaultUserAddress(userAddress.getUserId());
            List<UserAddressDTO> userAddressDTOS = userAddressService.selectUserAddressByUserId(address.getUserId());
            if (userAddressDTO.getId().equals(address.getId())) {
                if (!userAddress.getIsDefault()) {
                    if (userAddressDTOS.size() > 1) {
                        Boolean flag = true;
                        for (int i = 0; i < userAddressDTOS.size(); i++) {
                            if (flag) {
                                if (!userAddressDTOS.get(i).getId().equals(address.getId())) {
                                    UserAddressDTO addressDTO = userAddressDTOS.get(i);
                                    addressDTO.setIsDefault(true);
                                    userAddressService.updateUserAddressByAddressId(addressDTO);
                                    flag = false;
                                }
                            }
                        }
                    }
                }
            }

            boolean flag = userAddressService.updateUserAddressByAddressId(userAddress);
            if (flag == true) {
                return ResponseMessage.newSuccessInstance("更新成功");
            } else {
                return ResponseMessage.newErrorInstance("更新失败");
            }
        } else {
            return ResponseMessage.newErrorInstance("该地址id错误");
        }
    }

    /*
     * 根据用户地址id删除地址
     * */
    @DeleteMapping("/deleteUserAddress")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "校验token", name = "Authorization", paramType = "header", required = true)
    })
    @ApiOperation(value = "根据用户地址id删除地址")
    public ResponseMessage deleteUserAddress(@ApiParam(name = "用户地址Dto实体类", value = "传入Json格式", required = true)
                                             @RequestBody
                                             @Validated(Delete.class) UserAddressDTO userAddress) {
        UserAddressDTO address = userAddressService.selectUserAddressDtoByAddressId(userAddress.getId());
        if (address != null) {
            UserAddressDTO userAddressDTO = userAddressService.selectDefaultUserAddress(userAddress.getUserId());
            List<UserAddressDTO> userAddressDTOS = userAddressService.selectUserAddressByUserId(address.getUserId());
            if (userAddressDTO.getId().equals(address.getId())) {
                if (userAddressDTOS.size() > 1) {
                    Boolean flag = true;
                    for (int i = 0; i < userAddressDTOS.size(); i++) {
                        if (flag) {
                            if (!userAddressDTOS.get(i).getId().equals(address.getId())) {
                                UserAddressDTO addressDTO = userAddressDTOS.get(i);
                                addressDTO.setIsDefault(true);
                                userAddressService.updateUserAddressByAddressId(addressDTO);
                                flag = false;
                            }
                        }
                    }
                }
            }
            boolean flag = userAddressService.deleteUserAddressByAddressId(address);
            if (flag == true) {
                return ResponseMessage.newSuccessInstance("删除成功");
            } else {
                return ResponseMessage.newErrorInstance("删除失败");
            }
        } else {
            return ResponseMessage.newErrorInstance("该地址id错误");
        }
    }

    /*
     * 查找默认地址
     * */
    @GetMapping("/defaultUserAddress/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "校验token", name = "Authorization", paramType = "header", required = true)
    })
    @ApiOperation(value = "根据用户id查看该用户的默认地址")
    public ResponseMessage selectDefaultUserAddress() {
        UserAddressDTO addressDto = userAddressService.selectDefaultUserAddressByToken();
        if (addressDto == null) {
            return ResponseMessage.newErrorInstance("无默认地址");
        } else {
            return ResponseMessage.newSuccessInstance(addressDto, "查找成功");
        }
    }

    /*
     * 改变默认地址
     * */
    @PutMapping("/changeDefaultUserAddress")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "校验token", name = "Authorization", paramType = "header", required = true)
    })
    @ApiOperation(value = "改变用户默认地址")
    public ResponseMessage changeDefaultUserAddress(@ApiParam(name = "用户地址Dto实体类", value = "传入Json格式", required = true)
                                                    @RequestBody
                                                    @Validated(Update.class) UserAddressDTO userAddress) {
        UserAddressDTO addressDto = userAddressService.selectDefaultUserAddress(userAddress.getUserId());
        if (addressDto != null) {
            boolean changeFlag = userAddressService.changeDefaultUserAddress(addressDto, userAddress);
            if (changeFlag) {
                return ResponseMessage.newSuccessInstance("改变成功");
            } else {
                return ResponseMessage.newErrorInstance("改变失败");
            }
        } else {
            return ResponseMessage.newErrorInstance("该地址错误");
        }
    }
}
