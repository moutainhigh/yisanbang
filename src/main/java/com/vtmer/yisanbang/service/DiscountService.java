package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.domain.Discount;

public interface DiscountService {

    int insert(Discount discount);

    int update(Discount discount);

    Discount selectDiscount();

    int deleteDiscount();
}
