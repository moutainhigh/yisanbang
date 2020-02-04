package com.vtmer.yisanbang.service.impl;

import com.vtmer.yisanbang.common.OrderNumberUtil;
import com.vtmer.yisanbang.domain.Order;
import com.vtmer.yisanbang.domain.Refund;
import com.vtmer.yisanbang.dto.AgreeRefundDto;
import com.vtmer.yisanbang.mapper.OrderMapper;
import com.vtmer.yisanbang.mapper.RefundMapper;
import com.vtmer.yisanbang.service.OrderService;
import com.vtmer.yisanbang.service.RefundService;
import com.vtmer.yisanbang.vo.CartVo;
import com.vtmer.yisanbang.vo.OrderVo;
import com.vtmer.yisanbang.vo.RefundVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class RefundServiceImpl implements RefundService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RefundMapper refundMapper;

    @Autowired
    private OrderService orderService;

    /**
     * 申请退款,同时修改订单表状态，因此开启事务
     * 当该订单申请过退款，再次发起申请时，更新该订单退款信息，因此退款信息表中orderId唯一
     * @param refund：orderId，reason
     * @return
     */
    @Transactional
    public int applyForRefund(Refund refund) {
        Integer orderId = refund.getOrderId();
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (order == null) {  // 该订单不存在
            return -1;
        }
        Refund refund1 = refundMapper.selectByOrderId(orderId);
        HashMap<String, Integer> orderMap = new HashMap<>();
        if (refund1 != null) { // 如果该orderId在退款信息表中存在，说明之前申请过退款，只需更新其状态即可
            orderMap.put("orderId",orderId);
            orderMap.put("status",4);
            // 更新订单表状态为 "4"——申请退款
            orderService.setOrderStatus(orderMap);
            // 更新申请时间、申请原因和退款编号
            Refund refund2 = new Refund();
            refund2.setReason(refund.getReason());
            refund2.setCreateTime(new Date());
            Integer userId = order.getUserId();
            refund2.setRefundNumber(OrderNumberUtil.getRefundNumber(userId));
            return refundMapper.updateReasonByOrderId(refund2);
        } else { // 如果该订单之前未退过款
            Integer userId = order.getUserId();
            refund.setUserId(userId);
            String refundNumber = OrderNumberUtil.getRefundNumber(userId);
            // 设置退款编号
            refund.setRefundNumber(refundNumber);
            // 设置退款金额
            refund.setRefundPrice(order.getTotalPrice());
            // 更新订单表状态为 "4"——申请退款
            orderMap.put("orderId",orderId);
            orderMap.put("status",4);
            orderService.setOrderStatus(orderMap);
            // 插入退款信息
            return refundMapper.insert(refund);
        }
    }

    /**
     * @param orderId:订单Id
     * @return RefundVo:退款详情
     */
    public RefundVo getRefundVoByOrderId(Integer orderId) {

        Refund refund = refundMapper.selectByOrderId(orderId);
        if (refund == null) {
            return null;
        }
        return getRefundVoByRefund(refund);
    }

    /**
     * 获取相应状态的退款订单
     * 退款状态status 0--等待商家处理  1--退款中（待商家收货） 2--退款成功 3--退款失败
     * 4 -- 获取所有退款订单
     * @param status: 退款状态
     * @return List<RefundVo>
     */
    public List<RefundVo> getRefundVoListByStatus(Integer status) {
        List<Refund> refundList = refundMapper.selectByStatus(status);
        if (refundList == null) {
            return null;
        }
        ArrayList<RefundVo> refundVoList = new ArrayList<>();
        for (Refund refund : refundList) {
            RefundVo refundVo = getRefundVoByRefund(refund);
            refundVoList.add(refundVo);
        }

        return refundVoList;
    }

    /**
     * 由退款基础信息封装退款详情信息
     * @param refund：退款基础信息
     * @return RefundVo：退款详情信息
     */
    private RefundVo getRefundVoByRefund(Refund refund) {
        RefundVo refundVo = new RefundVo();
        Integer orderId = refund.getOrderId();
        Order order = orderMapper.selectByPrimaryKey(orderId);

        // 封装退款相关信息
        refundVo.setRefund(refund);

        // 封装退款商品详情
        OrderVo orderVo = orderService.selectOrderVoByOrderNumber(order.getOrderNumber());
        CartVo orderGoodsList = orderVo.getOrderGoodsList();
        refundVo.setOrderGoodsList(orderGoodsList);

        return refundVo;
    }

    /**
     * 根据退款编号获取申请退款所需参数
     * @param refundNumber：退款编号
     * @return
     */
    public AgreeRefundDto getAgreeRefundDtoByRefundNumber(String refundNumber) {
        AgreeRefundDto agreeRefundDto = new AgreeRefundDto();
        Refund refund = refundMapper.selectByRefundNumber(refundNumber);
        if (refund == null) {
            return null;
        }
        //订单id
        Integer orderId = refund.getOrderId();
        // 退款金额
        Double refundPrice = refund.getRefundPrice();
        agreeRefundDto.setRefundFee(Double.toString(refundPrice));
        Order order = orderMapper.selectByPrimaryKey(orderId);
        // 订单金额和订单编号
        agreeRefundDto.setTotalFee(order.getTotalPrice().toString());
        agreeRefundDto.setOrderNumber(order.getOrderNumber());

        return agreeRefundDto;
    }

    /**
     * status 订单状态 0--待付款 1--待发货 2--待收货 3--已完成 4--申请退款(待商家处理) 5--退款中(待商家收货) 6--退款成功 7--退款失败 8--交易关闭 9--所有订单
     * 退款状态status 0--等待商家处理  1--退款中（待商家收货） 2--退款成功 3--退款失败
     * @param refundMap:orderId、status
     * @return
     */
    @Transactional
    public int updateRefundStatus(Map<String, Integer> refundMap) {
        Refund refund = refundMapper.selectByOrderId(refundMap.get("orderId"));
        Integer status = refundMap.get("status");
        // 如果欲更新的状态与原状态相同
        if (refund.getStatus().equals(status)) {
            return -1;
        // 如果退款单原状态为0，欲更新的状态为2，即待商家处理更新到退款成功，报错
        } else if (refund.getStatus()==0 && status==2) {
            return -2;
        }
        // 更新退款表状态
        refundMapper.updateStatusByOrderId(refundMap);
        int orderStatus = 3; // 默认为已完成
        // 更新订单表状态
        if (status == 2) { // 如果更新为退款成功
            orderStatus = 6;
        } else if (status == 1) { // 如果更新为退款中
            orderStatus = 5;
        } else if (status == 3) { // 如果更新为退款失败
            orderStatus = 7;
        }
        refundMap.put("status",orderStatus);
        // 更新订单表的状态
        return orderService.setOrderStatus(refundMap);
    }

    /**
     * 根据退款编号获取退款基本信息
     * @param refundNumber:退款编号
     * @return
     */
    public Refund selectByRefundNumber(String refundNumber) {
        return refundMapper.selectByRefundNumber(refundNumber);
    }

    @Override
    public int deleteByRefundNumber(String refundNumber) {
        Refund refund = refundMapper.selectByRefundNumber(refundNumber);
        if (refund == null) {
            return -1;
        }
        return refundMapper.deleteByRefundNumber(refundNumber);
    }
}
