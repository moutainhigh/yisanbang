package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.RolePermi;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePermiMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RolePermi record);

    RolePermi selectByPrimaryKey(Integer id);

    List<RolePermi> selectAll();

    int updateByPrimaryKey(RolePermi record);

    // 根据角色id查找权限id
    List<Integer> selectPermiIdByRoleId(Integer roleId);
}