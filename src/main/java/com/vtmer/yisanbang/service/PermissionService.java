package com.vtmer.yisanbang.service;

import java.util.List;

public interface PermissionService {

    // 根据角色id查找权限URL
    public List<String> selectUrlByRoleId(Integer roleId);

}
