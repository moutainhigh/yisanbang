package com.vtmer.yisanbang.dto;

import com.vtmer.yisanbang.common.validGroup.Insert;
import com.vtmer.yisanbang.common.validGroup.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.vtmer.yisanbang.common.validGroup.Delete;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@ApiModel(value = "userAddress对象", description = "用户地址Dto对象")
public class UserAddressDto {
    @NotNull(groups = {Update.class, Delete.class}, message = "收货人id不可为空")
    @ApiModelProperty(value = "用户地址id", example = "1")
    private Integer id;

    @NotNull(message = "收货人id不可为空")
    @ApiModelProperty(value = "用户id", example = "1")
    private Integer userId;

    @NotBlank(message = "收货人姓名不可为空")
    @ApiModelProperty(value = "用户姓名", example = "张三")
    private String userName;

    @NotBlank(message = "收货地址不可为空")
    @ApiModelProperty(value = "地址名称", example = "**省**市**镇**村***号")
    private String addressName;

    @NotBlank(message = "联系号码不可为空")
    //@Pattern(groups = {Insert.class, Update.class}, regexp = "^1([34578])\\d{9}$", message = "手机号码格式不正确")
    @ApiModelProperty(value = "联系方式", example = "130*****8888")
    private String phoneNumber;

    @NotNull(message = "默认标志不可为空")
    @ApiModelProperty(value = "是否为默认地址", example = "false", notes = "除修改其他皆不填")
    private Boolean isDefault;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName == null ? null : addressName.trim();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber == null ? null : phoneNumber.trim();
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UserAddressDto(Integer id, @NotBlank(groups = {Insert.class, Update.class}, message = "收货人id不可为空") Integer userId, @NotBlank(groups = {Insert.class, Update.class}, message = "收货人姓名不可为空") String userName, @NotBlank(groups = {Insert.class, Update.class}, message = "收货地址不可为空") String addressName, @NotBlank(groups = {Insert.class, Update.class}, message = "联系号码不可为空") @Pattern(groups = {Insert.class, Update.class}, regexp = "^1([34578])\\d{9}$", message = "手机号码格式不正确") String phoneNumber, Boolean isDefault) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.addressName = addressName;
        this.phoneNumber = phoneNumber;
        this.isDefault = isDefault;
    }

    @Override
    public String toString() {
        return "UserAddressDto{" +
                "id=" + id +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", addressName='" + addressName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", isDefault=" + isDefault +
                '}';
    }
}
