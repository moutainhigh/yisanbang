package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.GoodsDetail;
import com.vtmer.yisanbang.dto.GoodsDetailDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
@Mapper
@Repository
public interface GoodsDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsDetail record);

    GoodsDetail selectByPrimaryKey(Integer id);

    List<GoodsDetail> selectAll();

    int updateByPrimaryKey(GoodsDetail record);

    // Dto
    int insertDto(GoodsDetailDto record);

    // Dto
    GoodsDetailDto selectDtoByPrimaryKey(Integer id);

    // Dto
    List<GoodsDetailDto> selectAllDto();

    // Dto
    List<GoodsDetailDto> selectAllDtoByGoodsId(Integer goodsId);

    // Dto
    int updateDtoByPrimaryKey(GoodsDetailDto record);

}