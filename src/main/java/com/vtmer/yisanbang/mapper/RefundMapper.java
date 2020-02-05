package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.Refund;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface RefundMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Refund record);

    Refund selectByPrimaryKey(Integer id);

    List<Refund> selectAll();

    int updateByPrimaryKey(Refund record);

    Refund selectByOrderId(Integer orderId);

    int updateReasonByOrderId(Refund refund);

    Refund selectByRefundNumber(String refundNumber);

    void updateStatusByOrderId(Map<String, Integer> refundMap);

    List<Refund> selectByStatus(Integer status);

    int deleteByRefundNumber(String refundNumber);

    void deleteByOrderId(Integer orderId);
}