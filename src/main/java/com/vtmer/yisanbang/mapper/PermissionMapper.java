package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.Permission;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Permission record);

    Permission selectByPrimaryKey(Integer id);

    List<Permission> selectAll();

    int updateByPrimaryKey(Permission record);

    // 根据权限id返回URL
    String selectUrlByPermiId(Integer permiId);
}