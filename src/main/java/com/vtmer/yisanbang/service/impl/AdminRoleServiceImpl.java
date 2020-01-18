package com.vtmer.yisanbang.service.impl;

import com.vtmer.yisanbang.mapper.AdminMapper;
import com.vtmer.yisanbang.mapper.AdminRoleMapper;
import com.vtmer.yisanbang.service.AdminRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminRoleServiceImpl implements AdminRoleService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AdminRoleMapper adminRoleMapper;

    @Override
    public List<Integer> selectRoleIdByName(String name) {
        return adminRoleMapper.selectRoleIdByAdminId(adminMapper.selectAdminIdByName(name));
    }

}
