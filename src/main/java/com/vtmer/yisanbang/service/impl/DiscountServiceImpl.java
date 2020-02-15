package com.vtmer.yisanbang.service.impl;

import com.vtmer.yisanbang.common.exception.service.discount.DiscountExistException;
import com.vtmer.yisanbang.common.exception.service.discount.DiscountNotFoundException;
import com.vtmer.yisanbang.domain.Discount;
import com.vtmer.yisanbang.mapper.DiscountMapper;
import com.vtmer.yisanbang.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiscountServiceImpl implements DiscountService {

    @Autowired
    private DiscountMapper discountMapper;

    @Override
    public void insert(Discount discount) {
        Boolean res = discountMapper.checkExist();
        if (!res) {
            // 还没有打折优惠设置
            discountMapper.insert(discount);
        } else {  // 已经有打折数据了
            throw new DiscountExistException();
        }
    }

    @Override
    public void update(Discount discount) {
        Discount checkExist = selectDiscount();
        if (checkExist == null) {
            // 如果不存在优惠信息设置
            throw new DiscountNotFoundException();
        }
        // 存在，更新
        discountMapper.update(discount);
    }

    @Override
    public Discount selectDiscount() {
        return discountMapper.select();
    }

    @Override
    public int deleteDiscount() {
        Discount checkExist = selectDiscount();
        if (checkExist == null) {
            // 如果不存在优惠信息设置
            throw new DiscountNotFoundException();
        }
        return discountMapper.delete();
    }
}
