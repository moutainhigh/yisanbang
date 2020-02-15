package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.CartGoods;
import com.vtmer.yisanbang.dto.CartGoodsDTO;
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
    List<CartGoodsDTO> selectCartGoodsByCartId(Integer cartId);

    // 根据购物车id查询购物车中普通商品信息
    List<CartGoodsDTO> selectCartGoodsByCartId1(Integer cartId);

    boolean checkGoodsExist(CartGoodsDTO cartGoodsDto);

    boolean addOrSubtractAmount(CartGoodsDTO cartGoodsDto);

    boolean insertCartGoods(CartGoodsDTO cartGoodsDto);

    boolean updateAmount(CartGoodsDTO cartGoodsDto);

    Integer selectChosen(CartGoodsDTO cartGoodsDto);

    Boolean updateChosen(@Param("addGoodsDto") CartGoodsDTO cartGoodsDto, @Param("isChosen") Integer isChosen);

    Boolean deleteCartGoods(CartGoodsDTO cartGoodsDto);

    void deleteCartGoodsByIsChosen(Integer cartId);

}