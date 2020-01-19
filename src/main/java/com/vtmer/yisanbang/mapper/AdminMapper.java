package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.Admin;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Admin record);

    Admin selectByPrimaryKey(Integer id);

    List<Admin> selectAll();

    int updateByPrimaryKey(Admin record);

    // 插入管理员信息
    int insertAdmin(Admin admin);

    // 根据管理员名称查找管理员(id)
    Integer selectAdminIdByName(String name);

    // 根据管理员名称查找密码
    String selectPasswordByName(String name);

    // 根据管理员id修改密码
    int updatePasswordByAdminId(Integer adminId, String password);

    // 查找所有普通管理员
    List<Admin> selectGeneralAdmin();

}