package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.GoodsDetail;
import com.vtmer.yisanbang.dto.GoodsDetailDTO;
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
    int insertDto(GoodsDetailDTO record);

    // Dto
    GoodsDetailDTO selectDtoByPrimaryKey(Integer id);

    // Dto
    List<GoodsDetailDTO> selectAllDto();

    // Dto
    List<GoodsDetailDTO> selectAllDtoByGoodsId(Integer goodsId);

    // Dto
    int updateDtoByPrimaryKey(GoodsDetailDTO record);

}