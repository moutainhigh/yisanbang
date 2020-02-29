package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.RefundExpress;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface RefundExpressMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RefundExpress record);

    RefundExpress selectByPrimaryKey(Integer id);

    List<RefundExpress> selectAll();

    int updateByPrimaryKey(RefundExpress record);

    RefundExpress selectByRefundId(Integer refundId);
}