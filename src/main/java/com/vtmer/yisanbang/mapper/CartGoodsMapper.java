package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.CartGoods;
import com.vtmer.yisanbang.dto.CartGoodsDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CartGoodsMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(CartGoods record);

    CartGoods selectByPrimaryKey(Integer id);

    List<CartGoods> selectAll();

    int updateByPrimaryKey(CartGoods record);

    List<CartGoodsDto> selectCartGoodsByCartId(Integer cartId);
}