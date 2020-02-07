package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.SuitDetail;
import com.vtmer.yisanbang.dto.GoodsDetailDto;
import com.vtmer.yisanbang.dto.SuitDetailDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
@Mapper
@Repository
public interface SuitDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SuitDetail record);

    SuitDetail selectByPrimaryKey(Integer id);

    List<SuitDetail> selectAll();

    int updateByPrimaryKey(SuitDetail record);

    // Dto
    int insertDto(SuitDetailDto record);

    // Dto
    SuitDetailDto selectDtoByPrimaryKey(Integer id);

    // Dto
    List<SuitDetailDto> selectAllDto();

    // Dto
    List<SuitDetailDto> selectAllDtoBySuitId(Integer goodsId);

    // Dto
    int updateDtoByPrimaryKey(SuitDetailDto record);
}