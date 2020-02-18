package com.vtmer.yisanbang.service.impl;

import com.vtmer.yisanbang.domain.*;
import com.vtmer.yisanbang.dto.ColorSizeDTO;
import com.vtmer.yisanbang.dto.PartSizeDTO;
import com.vtmer.yisanbang.mapper.*;
import com.vtmer.yisanbang.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class InventoryServiceImpl implements InventoryService {
    @Autowired
    private ColorSizeMapper colorSizeMapper;

    @Autowired
    private PartSizeMapper partSizeMapper;

    @Autowired
    private RefundMapper refundMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderGoodsMapper orderGoodsMapper;

    @Override
    // 判断库存是否足够
    public boolean JudgeInventory(OrderGoods orderGoods) {
        Boolean isGoods = orderGoods.getWhetherGoods();
        if (isGoods) {
            ColorSizeDTO colorSizeDto = colorSizeMapper.selectDtoByPrimaryKey(orderGoods.getSizeId());
            if (colorSizeDto.getInventory() < orderGoods.getAmount())
                return false;
            else
                return true;
        } else {
            PartSizeDTO partSizeDto = partSizeMapper.selectDtoByPrimaryKey(orderGoods.getSizeId());
            if (partSizeDto.getInventory() < orderGoods.getAmount())
                return false;
            else
                return true;
        }
    }

    @Override
    // 交易成功后减去库存
    public boolean minusInventory(OrderGoods orderGoods) {
        Boolean isGoods = orderGoods.getWhetherGoods();
        if (isGoods) {
            ColorSizeDTO colorSizeDto = colorSizeMapper.selectDtoByPrimaryKey(orderGoods.getSizeId());
            Integer amount = colorSizeDto.getInventory() - orderGoods.getAmount();
            colorSizeDto.setInventory(amount);
            int updateFlag = colorSizeMapper.updateDtoByPrimaryKey(colorSizeDto);
            if (updateFlag > 0) return true;
            else return false;
        } else {
            PartSizeDTO partSizeDto = partSizeMapper.selectDtoByPrimaryKey(orderGoods.getSizeId());
            Integer amount = partSizeDto.getInventory() - orderGoods.getAmount();
            partSizeDto.setInventory(amount);
            int updateFlag = partSizeMapper.updateDtoByPrimaryKey(partSizeDto);
            if (updateFlag > 0) return true;
            else return false;
        }
    }

    @Override
    // 取消订单后增加库存
    public boolean addInventory(RefundGoods refundGoods) {
        Boolean isGoods = refundGoods.getIsGoods();
        Integer refundId = refundGoods.getRefundId();
        Refund refund = refundMapper.selectByPrimaryKey(refundId);
        Integer orderId = refund.getOrderId();
        List<OrderGoods> orderGoodsList = orderGoodsMapper.selectByOrderId(orderId);
        if (isGoods) {
            ColorSizeDTO colorSizeDto = colorSizeMapper.selectDtoByPrimaryKey(refundGoods.getSizeId());
            for (OrderGoods orderGoods1 : orderGoodsList) {
                if (colorSizeDto.getId() == orderGoods1.getSizeId()) {
                    Integer amount = colorSizeDto.getInventory() + orderGoods1.getAmount();
                    colorSizeDto.setInventory(amount);
                    int updateFlag = colorSizeMapper.updateDtoByPrimaryKey(colorSizeDto);
                    if (updateFlag > 0) return true;
                    else return false;
                }
            }
        } else {
            PartSizeDTO partSizeDto = partSizeMapper.selectDtoByPrimaryKey(refundGoods.getSizeId());
            for (OrderGoods orderGoods1 : orderGoodsList) {
                if (partSizeDto.getId() == orderGoods1.getSizeId()) {
                    Integer amount = partSizeDto.getInventory() + orderGoods1.getAmount();
                    partSizeDto.setInventory(amount);
                    int updateFlag = partSizeMapper.updateDtoByPrimaryKey(partSizeDto);
                    if (updateFlag > 0) return true;
                    else return false;
                }
            }
        }
        return false;
    }


}
