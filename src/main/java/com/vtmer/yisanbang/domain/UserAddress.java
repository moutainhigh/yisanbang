package com.vtmer.yisanbang.domain;

import com.vtmer.yisanbang.common.validGroup.Insert;
import com.vtmer.yisanbang.common.validGroup.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

@ApiModel
public class UserAddress {

    @ApiModelProperty(hidden = true)
    private Integer id;

    @NotNull(groups = {Insert.class, Update.class},message = "userId为空")
    @ApiModelProperty(name = "userId",value = "用户id", required = true,example = "1")
    private Integer userId;

    @NotBlank(groups = {Insert.class, Update.class},message = "联系人姓名为空")
    @ApiModelProperty(name = "userName",value = "联系人姓名",required = true,example = "一叄邦")
    private String userName;

    @NotBlank(groups = {Insert.class, Update.class},message = "联系地址为空")
    @ApiModelProperty(name = "addressName",value = "用户收货地址",required = true,example = "广东工业大学生活东区")
    private String addressName;

    @NotBlank(groups = {Insert.class, Update.class},message = "联系号码不能为空")
    @Pattern(groups = {Insert.class, Update.class},regexp = "^1([34578])\\d{9}$",message = "手机号码格式不正确")
    @ApiModelProperty(name = "phoneNumber",value = "用户电话号码",required = true,example = "17666289644")
    private String phoneNumber;

    @ApiModelProperty(readOnly = true)
    private Boolean isDefault;

    @ApiModelProperty(hidden = true)
    private Date createTime;

    @ApiModelProperty(hidden = true)
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