package com.vtmer.yisanbang.service.impl;

import com.vtmer.yisanbang.mapper.PermissionMapper;
import com.vtmer.yisanbang.mapper.RolePermiMapper;
import com.vtmer.yisanbang.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private RolePermiMapper rolePermiMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public List<String> selectUrlByRoleId(Integer roleId) {
        List<String> urls= new ArrayList<>();
        for (Integer permiId: rolePermiMapper.selectPermiIdByRoleId(roleId)) {
            String url = permissionMapper.selectUrlByPermiId(permiId);
            urls.add(url);
        }
        return urls;
    }

}
