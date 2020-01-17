package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.Income;
import java.util.List;

public interface IncomeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Income record);

    Income selectByPrimaryKey(Integer id);

    List<Income> selectAll();

    int updateByPrimaryKey(Income record);
}