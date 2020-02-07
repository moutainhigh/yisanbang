package com.vtmer.yisanbang.domain;

import com.vtmer.yisanbang.common.validGroup.Insert;
import com.vtmer.yisanbang.common.validGroup.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

public class UserAddress {

    private Integer id;

    @NotNull(groups = {Insert.class, Update.class},message = "userId为空")
    private Integer userId;

    @NotBlank(groups = {Insert.class, Update.class},message = "联系人姓名为空")
    private String userName;

    @NotBlank(groups = {Insert.class, Update.class},message = "联系地址为空")
    private String addressName;

    @NotBlank(groups = {Insert.class, Update.class},message = "联系号码不能为空")
    @Pattern(groups = {Insert.class, Update.class},regexp = "^1([34578])\\d{9}$",message = "手机号码格式不正确")
    private String phoneNumber;

    private Boolean isDefault;

    private Date createTime;

    private Date updateTime;

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}