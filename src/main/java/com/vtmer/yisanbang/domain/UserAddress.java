package com.vtmer.yisanbang.domain;

import com.vtmer.yisanbang.common.valid.group.Insert;
import com.vtmer.yisanbang.common.valid.group.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;

@ApiModel
@Data
public class UserAddress {

    @ApiModelProperty(hidden = true)
    private Integer id;

    @ApiModelProperty(name = "userId",value = "用户id",example = "1")
    private Integer userId;

    @NotBlank(groups = {Insert.class, Update.class},message = "联系人姓名为空")
    @ApiModelProperty(name = "userName",value = "联系人姓名",required = true,example = "一叄邦")
    private String userName;

    @NotBlank(groups = {Insert.class, Update.class},message = "联系地址为空")
    @ApiModelProperty(name = "addressName",value = "用户收货地址",required = true,example = "广东工业大学生活东区")
    private String addressName;

    @NotBlank(groups = {Insert.class, Update.class},message = "联系号码不能为空")
    @Pattern(groups = {Insert.class, Update.class},regexp = "^1([345789])\\d{9}$",message = "手机号码格式不正确")
    @ApiModelProperty(name = "phoneNumber",value = "用户电话号码",required = true,example = "17666289644")
    private String phoneNumber;

    @ApiModelProperty(readOnly = true,value = "是否是默认收货地址")
    private Boolean isDefault;

    @ApiModelProperty(hidden = true)
    private Date createTime;

    @ApiModelProperty(hidden = true)
    private Date updateTime;

}