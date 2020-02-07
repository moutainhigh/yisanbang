package com.vtmer.yisanbang.service.impl;

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
    public int insert(Discount discount) {
        Boolean res = discountMapper.checkExist();
        if (!res) {
            return discountMapper.insert(discount);
        } else {  // 如果已经有打折数据了，返回-1
            return -1;
        }

    }

    @Override
    public int update(Discount discount) {
        return discountMapper.update(discount);
    }

    @Override
    public Discount selectDiscount() {
        return discountMapper.select();
    }

    @Override
    public int deleteDiscount() {
        return discountMapper.delete();
    }
}
