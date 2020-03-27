package com.vtmer.yisanbang.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class UpdatePwdDTO {
    // 旧密码
    @ApiModelProperty(value = "旧密码", required = true, example = "a123456")
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;
    // 新密码
    @ApiModelProperty(value = "新密码(密码必须包含数字、小写字母、大写字母，限制长度为8-20位)", required = true, example = "Abc123456")
    @Pattern(regexp = "^(?=.*[0-9].*)(?=.*[A-Z].*)(?=.*[a-z].*).{8,20}$", message = "密码必须包含数字、小写字母、大写字母，限制长度为8-20位")
    private String newPassword;
    // 二次输入新密码
    @ApiModelProperty(value = "新密码确认", required = true, example = "Abc123456")
    @NotBlank(message = "新密码确认不能为空")
    private String newPasswordConfirm;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPasswordConfirm() {
        return newPasswordConfirm;
    }

    public void setNewPasswordConfirm(String newPasswordConfirm) {
        this.newPasswordConfirm = newPasswordConfirm;
    }
}
