package com.vtmer.yisanbang.service;

import java.util.List;

public interface AdminRoleService {

    // 标识新增管理员为普通管理员
    boolean setGeneralAdmin(Integer adminId);

    // 根据管理员名称查找管理员角色id
    List<Integer> selectRoleIdByName(String name);

}
