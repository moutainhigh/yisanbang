package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.RefundExpress;
import java.util.List;

public interface RefundExpressMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RefundExpress record);

    RefundExpress selectByPrimaryKey(Integer id);

    List<RefundExpress> selectAll();

    int updateByPrimaryKey(RefundExpress record);
}