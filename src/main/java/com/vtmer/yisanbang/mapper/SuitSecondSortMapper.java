package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.SuitSecondSort;
import java.util.List;

public interface SuitSecondSortMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SuitSecondSort record);

    SuitSecondSort selectByPrimaryKey(Integer id);

    List<SuitSecondSort> selectAll();

    int updateByPrimaryKey(SuitSecondSort record);
}