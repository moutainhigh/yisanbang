package com.vtmer.yisanbang.controller;

import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.domain.Admin;
import com.vtmer.yisanbang.dto.AddAdminDto;
import com.vtmer.yisanbang.dto.LoginDto;
import com.vtmer.yisanbang.service.AdminService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @PostMapping("/addAdmin")
    public ResponseMessage addAdmin(@RequestBody AddAdminDto addAdminDto) {
        if (adminService.isAdminNameExist(addAdminDto.getName())) {
            return ResponseMessage.newErrorInstance("新增管理员失败，管理员名称已存在");
        }
        if (!addAdminDto.getPassword().equals(addAdminDto.getPasswordConfirm())){
            return ResponseMessage.newErrorInstance("新增管理员失败，两次密码输入不一致");
        }
        Admin admin = new Admin(addAdminDto.getName(), addAdminDto.getPassword());
        if (adminService.addAdmin(admin)) {
            return ResponseMessage.newSuccessInstance("新建管理员成功");
        }
        return ResponseMessage.newErrorInstance("新增管理员失败，发生系统错误");
    }

    @PostMapping("/login")
    public ResponseMessage login(@RequestBody LoginDto loginDto) {
        /**
         * 使用Shiro编写认证操作
         */
        // 1.获取Subject
        Subject subject = SecurityUtils.getSubject();
        // 2.封装用户数据
        UsernamePasswordToken token = new UsernamePasswordToken(loginDto.getAdminName(), loginDto.getPassword());
        // 3.执行登陆方法
        try {
            subject.login(token);
            // System.out.println("登陆");
            // 登陆成功
            return ResponseMessage.newSuccessInstance("登陆成功");
        } catch (UnknownAccountException e) {
            e.printStackTrace();
            return ResponseMessage.newErrorInstance("登陆失败，用户名不存在");
        } catch (IncorrectCredentialsException e) {
            e.printStackTrace();
            return ResponseMessage.newErrorInstance("登陆失败，密码错误");
        }
    }

}
