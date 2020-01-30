package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.Discount;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface DiscountMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Discount record);

    Discount selectByPrimaryKey(Integer id);

    List<Discount> selectAll();

    int updateByPrimaryKey(Discount record);

    Discount select();

    int delete();

    Boolean checkExist();
}