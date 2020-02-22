package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.Goods;
import com.vtmer.yisanbang.dto.GoodsDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface GoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Goods record);

    Goods selectByPrimaryKey(Integer id);

    List<Goods> selectAll();

    int updateByPrimaryKey(Goods record);

    // Dto
    int insertDto(GoodsDTO record);

    // Dto
    GoodsDTO selectDtoByPrimaryKey(Integer goodsId);

    // Dto
    GoodsDTO selectDtoByGoodsName(String goodsName);

    // Dto
    List<GoodsDTO> selectAllDto();

    // Dto
    int updateDtoByPrimaryKey(GoodsDTO record);

    // Dto
    List<GoodsDTO> selectAllDtoBySort(Integer sortId);

    // Dto
    List<GoodsDTO> selectAllDtoBySortOrderByPrice(Integer sortId);

    // Dto
    List<GoodsDTO> selectAllDtoBySortOrderByTime(Integer sortId);

    // Dto
    List<GoodsDTO> selectAllDtoOrderByPrice();

    // Dto
    List<GoodsDTO> selectAllDtoOrderByTime();

    int hideGoods(Integer goodsId);

    int showGoods(Integer goodsId);
}