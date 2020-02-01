package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.Goods;
import com.vtmer.yisanbang.dto.GoodsDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface GoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Goods record);

    Goods selectByPrimaryKey(Integer goodsId);

    List<Goods> selectAll();

    int updateByPrimaryKey(Goods record);

    // Dto
    int insertDto(GoodsDto record);

    // Dto
    GoodsDto selectDtoByPrimaryKey(Integer goodsId);

    // Dto
    GoodsDto selectDtoByGoodsName(String goodsName);

    // Dto
    List<GoodsDto> selectAllDto();

    // Dto
    int updateDtoByPrimaryKey(GoodsDto record);
}