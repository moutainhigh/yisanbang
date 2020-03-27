package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.CartGoods;
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

    // 根据购物车id查询购物车中商品信息
    List<CartGoods> selectCartGoodsByCartId(Integer cartId);

}