package com.vtmer.yisanbang.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

public class LoginDto {
    @ApiModelProperty(value = "管理员用户名", required = true, example = "admin")
    @NotBlank(message = "用户名不能为空")
    private String adminName;
    @ApiModelProperty(value = "密码", required = true, example = "abc123456")
    @NotBlank(message = "密码不能为空")
    private String password;

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
