package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.RefundGoods;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface RefundGoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RefundGoods record);

    RefundGoods selectByPrimaryKey(Integer id);

    List<RefundGoods> selectAll();

    int updateByPrimaryKey(RefundGoods record);

    void deleteByRefundId(Integer refundId);

    List<RefundGoods> selectByRefundId(Integer refundId);
}