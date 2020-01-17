package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.UniformSort;
import java.util.List;

public interface UniformSortMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UniformSort record);

    UniformSort selectByPrimaryKey(Integer id);

    List<UniformSort> selectAll();

    int updateByPrimaryKey(UniformSort record);
}