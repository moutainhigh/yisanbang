package com.vtmer.yisanbang.service;

import java.util.List;

public interface AdminRoleService {
    // 根据管理员名称查找管理员角色id
    List<Integer> selectRoleIdByName(String name);
}
