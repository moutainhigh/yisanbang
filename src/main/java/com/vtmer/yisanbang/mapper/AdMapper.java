package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.Ad;
import java.util.List;

public interface AdMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Ad record);

    Ad selectByPrimaryKey(Integer id);

    List<Ad> selectAll();

    int updateByPrimaryKey(Ad record);
}