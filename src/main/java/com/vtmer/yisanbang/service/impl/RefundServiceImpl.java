package com.vtmer.yisanbang.service.impl;

import com.vtmer.yisanbang.common.OrderNumberUtil;
import com.vtmer.yisanbang.domain.Order;
import com.vtmer.yisanbang.domain.OrderGoods;
import com.vtmer.yisanbang.domain.Refund;
import com.vtmer.yisanbang.domain.RefundGoods;
import com.vtmer.yisanbang.dto.AgreeRefundDto;
import com.vtmer.yisanbang.dto.CartGoodsDto;
import com.vtmer.yisanbang.mapper.OrderGoodsMapper;
import com.vtmer.yisanbang.mapper.OrderMapper;
import com.vtmer.yisanbang.mapper.RefundGoodsMapper;
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

    @Autowired
    private RefundGoodsMapper refundGoodsMapper;

    @Autowired
    private OrderGoodsMapper orderGoodsMapper;

    /**
     * 申请退款,同时修改订单表状态，因此开启事务
     * 当该订单申请过退款，再次发起申请时，更新该订单退款信息，因此退款信息表中orderId唯一
     * @param refundVo：orderId,reason,refundGoodsList
     * @return
     */
    @Transactional
    public int applyForRefund(RefundVo refundVo) {
        Refund refund = refundVo.getRefund();
        Integer orderId = refund.getOrderId();
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (order == null) {  // 该订单不存在
            return -1;
        }
        // 查询退款基础信息
        Refund refund1 = refundMapper.selectByOrderId(orderId);
        HashMap<String, Integer> orderMap = new HashMap<>();
        // 如果该orderId在退款信息表中存在，说明之前申请过退款并被商家拒绝了或是重复申请
        if (refund1 != null) {
            if (refund1.getStatus() == 0) {  // 重复申请
                return -2;
            } else if (refund1.getStatus() == 4){ // 之前申请过退款并被商家拒绝了
                // 删除之前的退款信息，再插入新的退款基础信息
                refundMapper.deleteByOrderId(orderId);
                refundGoodsMapper.deleteByRefundId(refund1.getId());
            }
        }
        Integer userId = order.getUserId();
        refund.setUserId(userId);
        String refundNumber;
        Refund checkRefundNumberExist;
        // 保证退款编号唯一
        do {
            refundNumber = OrderNumberUtil.getRefundNumber(userId);
            // checkRefundNumberExist!=null说明退款编号存在,再次生成退款编号
            checkRefundNumberExist = refundMapper.selectByRefundNumber(refundNumber);
        } while (checkRefundNumberExist != null);
        // 设置退款编号
        refund.setRefundNumber(refundNumber);
        // 设置退款金额
        // 如果是未收货订单，直接设置退款金额为订单金额
        if (refund.getIsReceived() == 0) {
            refund.setRefundPrice(order.getTotalPrice());
        }
        // 如果是已收货订单，部分部分退款；此时的退款金额不需要设置，前端传递
        refundMapper.insert(refund);
        // 插入退款商品数据
        List<CartGoodsDto> refundGoodsList = refundVo.getRefundGoodsList();
        if (refundGoodsList != null && refundGoodsList.size()!=0) {
            for (CartGoodsDto cartGoodsDto : refundGoodsList) {
                RefundGoods refundGoods = new RefundGoods();
                refundGoods.setRefundId(refund.getId());
                refundGoods.setIsGoods(cartGoodsDto.getIsGoods());
                refundGoods.setSizeId(cartGoodsDto.getColorSizeId());
                refundGoodsMapper.insert(refundGoods);
            }
        }
        return 1;
    }

    /**
     * 根据订单id查询退款详情
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
     * 订单状态定义：status 订单状态 0--待付款 1--待发货 2--待收货 3--已完成 4--交易关闭 5--所有订单
     * 退款状态定义：status 退款状态 0--等待商家处理  1--退款中（待买家发货） 2--退款中（待商家收货） 3--退款成功 4--退款失败
     * 5 -- 获取所有退款订单
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
        // 封装退款相关信息
        refundVo.setRefund(refund);
        // 如果该退款是未收到货类型退款(全退),则从订单表中查询退款商品详情
        if (refund.getIsReceived() == 0) {
            Integer orderId = refund.getOrderId();
            Order order = orderMapper.selectByPrimaryKey(orderId);
            // 封装退款商品详情
            OrderVo orderVo = orderService.selectOrderVoByOrderNumber(order.getOrderNumber());
            CartVo cartVo = orderVo.getOrderGoodsList();
            refundVo.setRefundGoodsList(cartVo.getCartGoodsList());

        } else { // 如果是部分退
            List<CartGoodsDto> refundGoodsList1 = new ArrayList<>();
            // 查询退款订单的商品详情
            List<OrderGoods> orderGoodsList = orderGoodsMapper.selectByOrderId(refund.getOrderId());
            // 查询退款商品详情
            List<RefundGoods> refundGoodsList = refundGoodsMapper.selectByRefundId(refund.getId());
            for (RefundGoods refundGoods : refundGoodsList) {
                CartGoodsDto cartGoodsDto = new CartGoodsDto();
                Integer sizeId = refundGoods.getSizeId();
                Boolean isGoods = refundGoods.getIsGoods();
                for (OrderGoods orderGoods : orderGoodsList) {
                    // 由订单表设置退款商品的价格
                    if (isGoods == orderGoods.getIsGoods() && sizeId.equals(orderGoods.getSizeId())) {
                        // 商品单价
                        double price = orderGoods.getTotalPrice()/orderGoods.getAmount();
                        cartGoodsDto.setPrice(price);
                        cartGoodsDto.setAmount(orderGoods.getAmount());
                        cartGoodsDto.setAfterTotalPrice(orderGoods.getTotalPrice());
                    }
                } // end for orderGoodsList
                // 设置退款商品基础信息
                orderService.setCartGoodsDto(cartGoodsDto,sizeId,isGoods);
                refundGoodsList1.add(cartGoodsDto);
            } // end for refundGoodsList
            refundVo.setRefundGoodsList(refundGoodsList1);
        }
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
     * 订单状态定义：status 订单状态 0--待付款 1--待发货 2--待收货 3--已完成（交易成功） 4--交易关闭 5--所有订单
     * 退款状态定义：status 退款状态 0--等待商家处理  1--退款中（待买家发货） 2--退款中（待商家收货） 3--退款成功 4--退款失败
     * 0 --> 1 or 3 or 4  and  1 --> 2 or 4  and 2 --> 3 or 4
     * @param refundMap: orderId、status
     * @return
     */
    @Transactional
    public int updateRefundStatus(Map<String, Integer> refundMap) {
        Refund refund = refundMapper.selectByOrderId(refundMap.get("orderId"));
        Integer status = refundMap.get("status");
        // 如果欲更新的状态与原状态相同
        if (refund.getStatus().equals(status)) {
            return -1;
        // 如果退款单原状态为0，欲更新的状态为2，即待商家处理更新到待商家收货，报错
        } else if (refund.getStatus()==0 && status==2) {
            return -2;
        // 如果原退款状态大于欲修改的退款状态，报错，退款状态不可回溯
        } else if (refund.getStatus() > status) {
            return -3;
        }
        // 更新退款表状态
        refundMapper.updateStatusByOrderId(refundMap);
        // 更新订单表状态 —— 只有更新为退款成功，才需要更新订单表状态为已完成
        if (status == 3) {
            refundMap.put("status",3);
            return orderService.setOrderStatus(refundMap);
        } else {
            return 1;
        }
    }

    /**
     * 根据退款编号获取退款基本信息
     * @param refundNumber:退款编号
     * @return
     */
    public Refund selectByRefundNumber(String refundNumber) {
        return refundMapper.selectByRefundNumber(refundNumber);
    }

    /**
     * 根据退款编号删除退款信息
     * @param refundNumber
     * @return
     */
    @Transactional
    public int deleteByRefundNumber(String refundNumber) {
        Refund refund = refundMapper.selectByRefundNumber(refundNumber);
        if (refund == null) {
            return -1;
        } else if (refund.getIsReceived() == 1) { // 如果是已收到货的部分退款,删除该退款编号下的所有退款商品
            refundGoodsMapper.deleteByRefundId(refund.getId());
        }
        return refundMapper.deleteByRefundNumber(refundNumber);
    }

    @Override
    public Refund selectByPrimaryKey(Integer refundId) {
        return refundMapper.selectByPrimaryKey(refundId);
    }

    @Override
    public List<Order> getUnRefundOrder(List<Order> orderList) {
        Iterator<Order> iterator = orderList.iterator();
        // 如果是退款成功的订单，从orderList中剔除
        while (iterator.hasNext()) {
            Order order = iterator.next();
            Refund refund = refundMapper.selectByOrderId(order.getId());
            if (refund!=null && refund.getStatus() == 3) {
                iterator.remove();
            }
        }
        return orderList;
    }
}
