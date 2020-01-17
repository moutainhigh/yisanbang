package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.Refund;
import java.util.List;

public interface RefundMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Refund record);

    Refund selectByPrimaryKey(Integer id);

    List<Refund> selectAll();

    int updateByPrimaryKey(Refund record);
}