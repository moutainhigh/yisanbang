package com.vtmer.yisanbang.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "userAddress对象", description = "用户地址Dto对象")
public class UserAddressDto {
    @ApiModelProperty(value = "用户地址id", example = "1")
    private Integer id;

    @ApiModelProperty(value = "用户id", example = "1")
    private Integer userId;

    @ApiModelProperty(value = "地址名称", example = "**省**市**镇**村***号")
    private String addressName;

    @ApiModelProperty(value = "联系方式", example = "130*****8888")
    private String phoneNumber;

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

    public UserAddressDto(Integer id, Integer userId, String addressName, String phoneNumber, Boolean isDefault) {
        this.id = id;
        this.userId = userId;
        this.addressName = addressName;
        this.phoneNumber = phoneNumber;
        this.isDefault = isDefault;
    }

    @Override
    public String toString() {
        return "UserAddressDto{" +
                "id=" + id +
                ", userId=" + userId +
                ", addressName='" + addressName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", isDefault=" + isDefault +
                '}';
    }
}
