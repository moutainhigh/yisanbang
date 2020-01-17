package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.Sort;
import java.util.List;

public interface SortMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Sort record);

    Sort selectByPrimaryKey(Integer id);

    List<Sort> selectAll();

    int updateByPrimaryKey(Sort record);
}