package com.vtmer.yisanbang.service.impl;

import com.vtmer.yisanbang.common.exception.service.order.OrderAndUserNotMatchException;
import com.vtmer.yisanbang.common.exception.service.order.OrderNotFoundException;
import com.vtmer.yisanbang.common.exception.service.refund.*;
import com.vtmer.yisanbang.common.util.OrderNumberUtil;
import com.vtmer.yisanbang.domain.*;
import com.vtmer.yisanbang.dto.AgreeRefundDTO;
import com.vtmer.yisanbang.dto.GoodsSkuDTO;
import com.vtmer.yisanbang.dto.OrderGoodsDTO;
import com.vtmer.yisanbang.dto.RefundDTO;
import com.vtmer.yisanbang.mapper.*;
import com.vtmer.yisanbang.service.OrderService;
import com.vtmer.yisanbang.service.RefundService;
import com.vtmer.yisanbang.shiro.JwtFilter;
import com.vtmer.yisanbang.vo.OrderVO;
import com.vtmer.yisanbang.vo.RefundVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class RefundServiceImpl implements RefundService {

    private final Logger logger = LoggerFactory.getLogger(RefundServiceImpl.class);

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

    @Autowired
    private RefundExpressMapper refundExpressMapper;

    /**
     * 申请退款,同时修改订单表状态，因此开启事务
     * 当该订单申请过退款，再次发起申请时，更新该订单退款信息，因此退款信息表中orderId唯一
     * @param refundDTO：orderId,reason,refundGoodsList
     * @return
     */
    @Override
    @Transactional
    public void applyForRefund(RefundDTO refundDTO) {
        Integer userId = JwtFilter.getLoginUser().getId();
        Refund refund = refundDTO.getRefund();
        Integer orderId = refund.getOrderId();
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (order == null) {  // 该订单不存在
            throw new OrderNotFoundException();
        } else if (!order.getUserId().equals(userId)) {
            throw new OrderAndUserNotMatchException("订单["+order.getOrderNumber()+"]不属于用户["+userId+"]");
        }
        // 判断退款商品是否存在
        List<GoodsSkuDTO> refundGoodsList = refundDTO.getRefundGoodsList();
        List<OrderGoodsDTO> orderGoodsDTOList = new ArrayList<>();
        for (GoodsSkuDTO goodsSkuDTO : refundGoodsList) {
            OrderGoodsDTO orderGoodsDTO = new OrderGoodsDTO();
            BeanUtils.copyProperties(goodsSkuDTO,orderGoodsDTO);
            orderGoodsDTOList.add(orderGoodsDTO);
        }
        boolean check = orderService.judgeGoodsExist(orderGoodsDTOList);
        if (!check) {
            // 如果退款商品不存在
            throw new RefundGoodsNotFoundException();
        }
        // 查询原退款基础信息
        Refund refund1 = refundMapper.selectByOrderId(orderId);
        // 如果该orderId在退款信息表中存在，说明之前申请过退款并被商家拒绝了或是重复申请
        if (refund1 != null) {
            if (refund1.getStatus() == 0) {  // 重复申请
                throw new DuplicateApplyRefundException();
            } else if (refund1.getStatus() == 4){ // 之前申请过退款并被商家拒绝了
                // 删除之前的退款信息，再插入新的退款基础信息
                refundMapper.deleteByOrderId(orderId);
                refundGoodsMapper.deleteByRefundId(refund1.getId());
            }
        }
        // 退款商品不为空
        if (refundGoodsList != null && refundGoodsList.size() != 0) {
            // 设置为已收货类退款订单
            refund.setWhetherReceived(true);
        } else {
            // 未传退款商品，说明是未收货，全退
            refund.setWhetherReceived(false);
        }
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
        if (refund.getWhetherReceived().equals(false)) {
            refund.setRefundPrice(order.getTotalPrice());
        }
        // 如果是已收货订单，部分部分退款；此时的退款金额不需要设置，前端传递
        refundMapper.insert(refund);
        // 插入退款商品数据
        if (refundGoodsList != null && refundGoodsList.size()!=0) {
            for (GoodsSkuDTO goodsSkuDto : refundGoodsList) {
                RefundGoods refundGoods = new RefundGoods();
                refundGoods.setRefundId(refund.getId());
                refundGoods.setWhetherGoods(goodsSkuDto.getWhetherGoods());
                refundGoods.setSizeId(goodsSkuDto.getColorSizeId());
                refundGoodsMapper.insert(refundGoods);
            }
        }
    }

    /**
     * 根据订单id查询退款详情
     * @param orderId:订单Id
     * @return RefundVo:退款详情
     */
    @Override
    public RefundVo getRefundVoByOrderId(Integer orderId) {
        Refund refund = refundMapper.selectByOrderId(orderId);
        if (refund == null) {
            throw new RefundNotFoundException();
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
    @Override
    public List<RefundVo> getRefundVoListByStatus(Integer status) {
        if (!(status >= 0 && status <= 5)) {
            throw new RefundStatusNotFitException("传入退款状态参数超过范围");
        }
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
        // 封装用户退款发货单信息
        RefundExpress refundExpress = refundExpressMapper.selectByRefundId(refund.getId());
        if (refundExpress != null) {
            refundVo.setRefundExpress(refundExpress);
        } else {
            refundVo.setRefundExpress(null);
        }
        // 如果该退款是未收到货类型退款(全退),则从订单表中查询退款商品详情
        if (Boolean.FALSE.equals(refund.getWhetherReceived())) {
            Integer orderId = refund.getOrderId();
            Order order = orderMapper.selectByPrimaryKey(orderId);
            // 封装退款商品详情
            OrderVO orderVO = orderService.selectOrderVOByOrderNumber(order.getOrderNumber());
            List<OrderGoodsDTO> orderGoodsDTOList = orderVO.getOrderGoodsDTOList();
            refundVo.setRefundGoodsList(orderGoodsDTOList);
        } else {
            // 如果是部分退
            List<OrderGoodsDTO> refundGoodsList1 = new ArrayList<>();
            // 查询订单商品详情
            List<OrderGoods> orderGoodsList = orderGoodsMapper.selectByOrderId(refund.getOrderId());
            // 查询退款商品详情
            List<RefundGoods> refundGoodsList = refundGoodsMapper.selectByRefundId(refund.getId());
            for (RefundGoods refundGoods : refundGoodsList) {
                OrderGoodsDTO orderGoodsDTO = new OrderGoodsDTO();
                Integer sizeId = refundGoods.getSizeId();
                Boolean isGoods = refundGoods.getWhetherGoods();
                for (OrderGoods orderGoods : orderGoodsList) {
                    // 由订单表设置退款商品的价格
                    if (isGoods.equals(orderGoods.getWhetherGoods()) && sizeId.equals(orderGoods.getSizeId())) {
                        // 商品单价
                        double price = orderGoods.getTotalPrice()/orderGoods.getAmount();
                        orderGoodsDTO.setPrice(price);
                        orderGoodsDTO.setAmount(orderGoods.getAmount());
                        orderGoodsDTO.setAfterTotalPrice(orderGoods.getTotalPrice());
                    }
                } // end for orderGoodsList
                // 设置退款商品基础信息
                orderService.setOrderGoodsDTO(orderGoodsDTO,sizeId,isGoods);
                refundGoodsList1.add(orderGoodsDTO);
            } // end for refundGoodsList
            refundVo.setRefundGoodsList(refundGoodsList1);
        }
        return refundVo;
    }

    /**
     * 根据退款编号获取申请退款所需参数
     * @param agreeRefundDTO：退款编号
     * @return
     */
    @Override
    public void setAgreeRefundDTOByRefundNumber(AgreeRefundDTO agreeRefundDTO) {
        String refundNumber = agreeRefundDTO.getRefundNumber();
        Refund refund = selectByRefundNumber(refundNumber);
        if (refund == null) {
            throw new RefundNotFoundException();
        } else if (refund.getStatus() != 2 && refund.getWhetherReceived().equals(true)) {
            throw new RefundStatusNotFitException();
        }
        //订单id
        Integer orderId = refund.getOrderId();
        Order order = orderMapper.selectByPrimaryKey(orderId);
        // 退款金额
        Double refundPrice = refund.getRefundPrice();
        agreeRefundDTO.setRefundFee(Double.toString(refundPrice));
        // 订单金额和订单编号
        agreeRefundDTO.setTotalFee(order.getTotalPrice().toString());
        agreeRefundDTO.setOrderNumber(order.getOrderNumber());
    }

    /**
     * 订单状态定义：status 订单状态 0--待付款 1--待发货 2--待收货 3--已完成（交易成功） 4--交易关闭 5--所有订单
     * 退款状态定义：status 退款状态 0--等待商家处理  1--退款中（待买家发货） 2--退款中（待商家收货） 3--退款成功 4--退款失败
     * 0 --> 1 or 3 or 4  and  1 --> 2 or 4  and 2 --> 3 or 4
     * @param refundMap: orderId、status
     * @return
     */
    @Override
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
            orderService.setOrderStatus(refundMap);
        }
        return 1;
    }

    /**
     * 根据退款编号获取退款基本信息
     * @param refundNumber:退款编号
     * @return
     */
    @Override
    public Refund selectByRefundNumber(String refundNumber) {
        return refundMapper.selectByRefundNumber(refundNumber);
    }

    /**
     * 根据退款编号删除退款信息
     * @param refundNumber
     * @return
     */
    @Override
    @Transactional
    public void deleteByRefundNumber(String refundNumber) {
        Integer userId = JwtFilter.getLoginUser().getId();
        Refund refund = refundMapper.selectByRefundNumber(refundNumber);
        if (refund == null) {
            throw new RefundNotFoundException();
        } else if (refund.getStatus()!=0 || refund.getStatus()!=1) {
            // 如果退款状态不为[待商家处理]、[待用户发货]，则不可撤销退款申请
            throw new RefundStatusNotFitException("退款状态不为[待商家处理]、[待用户发货]，不可撤销退款申请");
        } else if(!refund.getUserId().equals(userId)) {
            // 检验是否是该用户的退款单
            throw new RefundNotMatchUserException("退款单["+refundNumber+"]不属于用户["+userId+"]");
        }
        // 开始撤销退款逻辑
        if (refund.getWhetherReceived() == true) { // 如果是已收到货的部分退款,删除该退款编号下的所有退款商品
            refundGoodsMapper.deleteByRefundId(refund.getId());
        }
        refundMapper.deleteByRefundNumber(refundNumber);
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

    @Override
    public void agreeRefundApply(String refundNumber) {
        Refund refund = selectByRefundNumber(refundNumber);
        if (refund == null) {
            throw new RefundNotFoundException();
        } else if (refund.getStatus() != 0) {
            throw new RefundStatusNotFitException("该退款单的状态不为[待商家处理]，不能调用该接口");
        }
        // 更新退款状态为待用户发货
        HashMap<String, Integer> refundMap = new HashMap<>();
        refundMap.put("orderId", refund.getOrderId());
        refundMap.put("status", 1);
        updateRefundStatus(refundMap);
        logger.info("退款单[{}]状态更新：[待商家处理]-->[待用户发货]",refundNumber);
    }

    @Override
    public void refuseRefundApply(String refundNumber) {
        Refund refund = selectByRefundNumber(refundNumber);
        if (refund == null) {
            throw new RefundNotFoundException();
        } else if (refund.getStatus() != 0) {
            // 如果退款状态不为“待商家处理”
            throw new RefundStatusNotFitException("该退款单["+refundNumber+"]的退款状态不为[待商家处理]，不可进行[拒绝退款申请]操作");
        }
        // 更改退款状态
        HashMap<String, Integer> refundMap = new HashMap<>();
        refundMap.put("orderId", refund.getOrderId());
        refundMap.put("status", 4);
        updateRefundStatus(refundMap);
        logger.info("退款单[{}]状态更新：[待商家处理]-->[退款失败]",refundNumber);
    }

    @Override
    public void insertRefundExpress(RefundExpress refundExpress) {
        Integer userId = JwtFilter.getLoginUser().getId();
        Refund refund = selectByPrimaryKey(refundExpress.getRefundId());
        if (refund == null) {
            throw new RefundNotFoundException();
        } else if (refund.getStatus() != 1) {
            throw new RefundStatusNotFitException("该退款单退款状态不为[待用户发货],不可填写退货单号");
        } else if (!refund.getUserId().equals(userId)) {
            throw new RefundNotMatchUserException("退款单["+refund.getRefundNumber()+"]不属于用户["+userId+"]");
        }
        // 执行添加退款单逻辑
        // 插入用户退货单信息
        refundExpressMapper.insert(refundExpress);
        HashMap<String, Integer> refundMap = new HashMap<>();
        refundMap.put("orderId", refund.getOrderId());
        refundMap.put("status", 2);
        updateRefundStatus(refundMap);
        logger.info("退款单[{}]状态更新：[待用户发货]-->[待商家收货]",refund.getRefundNumber());
    }
}
