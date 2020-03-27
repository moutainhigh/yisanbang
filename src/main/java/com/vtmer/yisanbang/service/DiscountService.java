package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.domain.Discount;

public interface DiscountService {

    void insert(Discount discount);

    void update(Discount discount);

    Discount selectDiscount();

    int deleteDiscount();
}
