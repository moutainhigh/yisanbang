package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.Cart;
import com.vtmer.yisanbang.dto.CartDto;
import com.vtmer.yisanbang.dto.CartGoodsDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CartMapper {
    int deleteByPrimmaryKey(Integer id);

    int insert(Cart record);

    Cart selectByPriaryKey(Integer id);

    List<Cart> selectAll();

    int updateByPrimaryKey(Cart record);

    Integer selectCartIdByUserId(Integer userId);

    CartDto selectCartDtoByUserId(Integer userId);
}