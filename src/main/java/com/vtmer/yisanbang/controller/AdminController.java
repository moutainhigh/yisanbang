package com.vtmer.yisanbang.controller;

import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.domain.Admin;
import com.vtmer.yisanbang.dto.AddAdminDto;
import com.vtmer.yisanbang.dto.LoginDto;
import com.vtmer.yisanbang.dto.UpdatePwdDto;
import com.vtmer.yisanbang.service.AdminRoleService;
import com.vtmer.yisanbang.service.AdminService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @Autowired
    AdminRoleService adminRoleService;

    /**
     * 添加普通管理员
     *
     * @param addAdminDto
     * @return
     */
    @PostMapping("/addAdmin")
    public ResponseMessage addAdmin(@RequestBody AddAdminDto addAdminDto) {
        if (adminService.isAdminNameExist(addAdminDto.getName())) {
            return ResponseMessage.newErrorInstance("新增管理员失败，管理员名称已存在");
        }
        if (!addAdminDto.getPassword().equals(addAdminDto.getPasswordConfirm())) {
            return ResponseMessage.newErrorInstance("新增管理员失败，两次密码输入不一致");
        }
        Admin admin = new Admin(addAdminDto.getName(), addAdminDto.getPassword());
        if (adminService.addAdmin(admin)) {
            if (adminRoleService.setGeneralAdmin(admin.getId())) {
                return ResponseMessage.newSuccessInstance("新建管理员成功");
            }
        }
        return ResponseMessage.newErrorInstance("新增管理员失败，发生系统错误");
    }


    /**
     * 管理员登录
     *
     * @param loginDto
     * @return
     */
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

    /**
     * 删除普通管理员
     *
     * @param adminId
     * @return
     */
    @DeleteMapping("/deleteAdmin/{id}")
    public ResponseMessage deleteAdmin(@PathVariable("id") Integer adminId) {
        if (adminService.deleteAdmin(adminId)) {
            return ResponseMessage.newSuccessInstance("删除管理员成功");
        }
        return ResponseMessage.newErrorInstance("删除管理员失败");
    }


    /**
     * 管理员修改密码
     *
     * @param adminId
     * @param updatePwdDto
     * @return
     */
    @PutMapping("/updatePwd/{id}")
    public ResponseMessage updatePwd(@PathVariable("id") Integer adminId, @RequestBody UpdatePwdDto updatePwdDto) {
        if (!adminService.isPasswordCorrect(adminId, updatePwdDto.getOldPassword())) {
            return ResponseMessage.newErrorInstance("原始密码输入错误");
        } else {
            if (!updatePwdDto.getNewPasswordConfirm().equals(updatePwdDto.getNewPassword())) {
                return ResponseMessage.newErrorInstance("两次新密码输入不一致");
            } else {
                if (adminService.updatePassword(adminId, updatePwdDto.getNewPassword())) {
                    return ResponseMessage.newSuccessInstance("密码修改成功");
                }
            }
        }
        return ResponseMessage.newErrorInstance("密码修改失败");
    }


    /**
     * 查找所有普通管理员
     *
     * @return
     */
    @GetMapping("/generalAdmin")
    public ResponseMessage listGeneralAdmin() {
        List<Admin> generalAdmins = adminService.listAllGeneralAdmin();
        if (generalAdmins != null) {
            return ResponseMessage.newSuccessInstance(generalAdmins);
        } else {
            return ResponseMessage.newErrorInstance("无普通管理员");
        }
    }

}