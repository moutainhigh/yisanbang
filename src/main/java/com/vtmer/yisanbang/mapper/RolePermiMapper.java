package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.RolePermi;
import java.util.List;

public interface RolePermiMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RolePermi record);

    RolePermi selectByPrimaryKey(Integer id);

    List<RolePermi> selectAll();

    int updateByPrimaryKey(RolePermi record);
}