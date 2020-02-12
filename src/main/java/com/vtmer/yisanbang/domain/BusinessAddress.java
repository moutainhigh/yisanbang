package com.vtmer.yisanbang.domain;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;

@ApiModel
public class BusinessAddress {

    @ApiModelProperty(value = "商家收货地址id",readOnly = true,example = "3")
    private Integer id;

    @NotBlank(message = "商家地址不能为空")
    @ApiModelProperty(value = "商家收货地址",example = "广州市白云区xxx",required = true)
    private String addressName;

    @NotBlank(message = "联系号码不能为空")
    @Pattern(regexp = "^1([34578])\\d{9}$",message = "手机号码格式不正确")
    @ApiModelProperty(value = "电话号码",example = "17666289644",required = true)
    private String phoneNumber;


    @NotBlank(message = "联系人姓名不能为空")
    @ApiModelProperty(value = "联系人姓名",example = "大老板",required = true)
    private String contactName;

    @ApiModelProperty(readOnly = true,example = "true",value = "是否是默认地址")
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

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName == null ? null : contactName.trim();
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