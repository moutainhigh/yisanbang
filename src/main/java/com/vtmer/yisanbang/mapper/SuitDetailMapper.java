package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.SuitDetail;
import com.vtmer.yisanbang.dto.SuitDetailDTO;
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
    int insertDto(SuitDetailDTO record);

    // Dto
    SuitDetailDTO selectDtoByPrimaryKey(Integer id);

    // Dto
    List<SuitDetailDTO> selectAllDto();

    // Dto
    List<SuitDetailDTO> selectAllDtoBySuitId(Integer goodsId);

    // Dto
    int updateDtoByPrimaryKey(SuitDetailDTO record);
}