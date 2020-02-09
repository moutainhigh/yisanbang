package com.vtmer.yisanbang.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class AddAdminDto {
    @ApiModelProperty(value = "管理员用户名", required = true, example = "abc123456")
    @Pattern(regexp = "^[a-z][a-z0-9]{5,11}$", message = "管理员用户名必须以小写字母开头，包含6-12位小写字母和数字")
    private String name;
    @ApiModelProperty(value = "登录密码", required = true, example = "Abc123456")
    @Pattern(regexp = "^(?=.*[0-9].*)(?=.*[A-Z].*)(?=.*[a-z].*).{8,20}$", message = "密码必须包含数字、小写字母、大写字母，限制长度为8-20位")
    private String password;
    @ApiModelProperty(value = "密码确认", required = true, example = "Abc123456")
    @NotBlank(message = "密码确认不能为空")
    private String passwordConfirm;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
}
