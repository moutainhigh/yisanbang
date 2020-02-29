package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.domain.Admin;
import com.vtmer.yisanbang.domain.Role;

import java.util.List;

public interface AdminService {

    // 新增管理员
    boolean addAdmin(Admin admin);

    // 检测管理员名称是否已经存在
    boolean isAdminNameExist(String name);

    // 删除普通管理员
    boolean deleteAdmin(Integer adminId);

    // 修改密码时比较管理员输入的密码是否与正确的密码一致
    boolean isPasswordCorrect(Integer adminId, String password);

    // 修改密码
    boolean updatePassword(Integer adminId, String password);

    // 查找所有普通管理员
    List<Admin> listAllGeneralAdmin();

    List<Role> getRoleListByName(String name);

    int getAdminIdByName(String name);

}
