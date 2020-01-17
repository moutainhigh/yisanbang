package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.SuitFirstSort;
import java.util.List;

public interface SuitFirstSortMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SuitFirstSort record);

    SuitFirstSort selectByPrimaryKey(Integer id);

    List<SuitFirstSort> selectAll();

    int updateByPrimaryKey(SuitFirstSort record);
}