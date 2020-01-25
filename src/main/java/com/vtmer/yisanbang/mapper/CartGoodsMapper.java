package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.CartGoods;
import com.vtmer.yisanbang.dto.AddGoodsDto;
import com.vtmer.yisanbang.dto.CartGoodsDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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

    // 根据购物车id查询购物车中套装散件信息
    List<CartGoodsDto> selectCartGoodsByCartId(Integer cartId);

    // 根据购物车id查询购物车中普通商品信息
    List<CartGoodsDto> selectCartGoodsByCartId1(Integer cartId);

    boolean checkGoodsExist(AddGoodsDto addGoodsDto);

    boolean updateAmount(AddGoodsDto addGoodsDto);

    boolean insertCartGoods(AddGoodsDto addGoodsDto);

    Integer selectChosen(AddGoodsDto addGoodsDto);

    Boolean updateChosen(@Param("addGoodsDto") AddGoodsDto addGoodsDto,@Param("isChosen") Integer isChosen);
}