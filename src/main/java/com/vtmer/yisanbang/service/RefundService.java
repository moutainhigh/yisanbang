package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.domain.Refund;
import com.vtmer.yisanbang.dto.AgreeRefundDto;
import com.vtmer.yisanbang.vo.RefundVo;

import java.util.List;
import java.util.Map;

public interface RefundService {

    /**
     * 申请退款
     * @param refund：orderId，reason （订单id、退款原因）
     * @return
     */
    int applyForRefund(Refund refund);

    /**
     * 获取退款详情
     * @param orderId：订单id
     * @return
     */
    RefundVo getRefundVoByOrderId(Integer orderId);

    List<RefundVo> getRefundVoListByStatus(Integer status);

    /**
     * 由退款编号获取退款所需的参数
     * @param refundNumber
     * @return
     */
    AgreeRefundDto getAgreeRefundDtoByRefundNumber(String refundNumber);

    /**
     *
     * @param refundMap:orderId、status
     * @return
     */
    int updateRefundStatus(Map<String,Integer> refundMap);

    Refund selectByRefundNumber(String refundNumber);

    int deleteByRefundNumber(String refundNumber);
}
