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
     * -1:欲更新的状态与原状态相同
     * -2:退款单原状态为0，欲更新的状态为2，即待商家处理更新到待商家收货，报错
     * -3:原退款状态大于欲修改的退款状态，报错，退款状态不可回溯
     * 0:更新失败
     * 1:更新成功
     */
    int updateRefundStatus(Map<String,Integer> refundMap);

    Refund selectByRefundNumber(String refundNumber);

    int deleteByRefundNumber(String refundNumber);
}
