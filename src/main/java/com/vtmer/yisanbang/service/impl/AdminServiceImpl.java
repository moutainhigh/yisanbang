package com.vtmer.yisanbang.service.impl;

import com.vtmer.yisanbang.common.util.EncryptUtils;
import com.vtmer.yisanbang.domain.Admin;
import com.vtmer.yisanbang.domain.Role;
import com.vtmer.yisanbang.mapper.AdminMapper;
import com.vtmer.yisanbang.mapper.AdminRoleMapper;
import com.vtmer.yisanbang.mapper.RoleMapper;
import com.vtmer.yisanbang.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AdminRoleMapper adminRoleMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public boolean addAdmin(Admin admin) {
        admin.setPassword(EncryptUtils.encrypt(admin.getName(), admin.getPassword()).toString());
        if (adminMapper.insertAdmin(admin) != 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isAdminNameExist(String name) {
        if (adminMapper.selectAdminIdByName(name) != null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteAdmin(Integer adminId) {
        if (adminMapper.deleteByPrimaryKey(adminId) != 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isPasswordCorrect(Integer adminId, String password) {
        String adminPwd = adminMapper.selectByPrimaryKey(adminId).getPassword();
        String adminName = adminMapper.selectByPrimaryKey(adminId).getName();
        if (adminPwd.equals(EncryptUtils.encrypt(adminName, password).toString())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean updatePassword(Integer adminId, String password) {
        String adminName = adminMapper.selectByPrimaryKey(adminId).getName();
        if (adminMapper.updatePasswordByAdminId(adminId, EncryptUtils.encrypt(adminName, password).toString()) != 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<Admin> listAllGeneralAdmin() {
        return adminMapper.selectGeneralAdmin();
    }

    @Override
    public List<Role> getRoleListByName(String name) {
        ArrayList<Role> roleArrayList = new ArrayList<>();
        Integer adminId = adminMapper.selectAdminIdByName(name);
        List<Integer> RoleIdList = adminRoleMapper.selectRoleIdByAdminId(adminId);
        for (Integer roleId : RoleIdList) {
            Role role = roleMapper.selectByPrimaryKey(roleId);
            roleArrayList.add(role);
        }
        return roleArrayList;
    }

}
