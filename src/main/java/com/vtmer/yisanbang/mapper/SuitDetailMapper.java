package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.SuitDetail;
import java.util.List;

public interface SuitDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SuitDetail record);

    SuitDetail selectByPrimaryKey(Integer id);

    List<SuitDetail> selectAll();

    int updateByPrimaryKey(SuitDetail record);
}