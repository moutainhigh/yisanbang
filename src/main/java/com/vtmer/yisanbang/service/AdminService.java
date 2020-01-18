package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.domain.Admin;

public interface AdminService {

    // 新增管理员
    boolean addAdmin(Admin admin);

    // 检测管理员名称是否已经存在
    boolean isAdminNameExist(String name);
}
