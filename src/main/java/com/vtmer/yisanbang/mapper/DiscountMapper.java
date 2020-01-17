package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.Discount;
import java.util.List;

public interface DiscountMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Discount record);

    Discount selectByPrimaryKey(Integer id);

    List<Discount> selectAll();

    int updateByPrimaryKey(Discount record);
}