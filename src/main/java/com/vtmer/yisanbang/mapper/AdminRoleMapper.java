package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.AdminRole;

import java.util.List;

public interface AdminRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AdminRole record);

    AdminRole selectByPrimaryKey(Integer id);

    List<AdminRole> selectAll();

    int updateByPrimaryKey(AdminRole record);

    // 根据管理员id查找角色id
    List<Integer> selectRoleIdByAdminId(Integer adminId);
}