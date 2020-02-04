package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.Cart;
import com.vtmer.yisanbang.vo.CartVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    Cart selectByPrimaryKey(Integer id);

    List<Cart> selectAll();

    int updateByPrimaryKey(Cart record);

    Integer selectCartIdByUserId(Integer userId);

    CartVo selectCartDtoByUserId(Integer userId);

    boolean updateTotalPrice(double totalPrice,Integer cartId);
}