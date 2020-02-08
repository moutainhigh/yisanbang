package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.Income;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface IncomeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Income record);

    Income selectByPrimaryKey(Integer id);

    List<Income> selectAll();

    int updateByPrimaryKey(Income record);
}