package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.Suit;
import com.vtmer.yisanbang.dto.SuitDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface SuitMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Suit record);

    Suit selectByPrimaryKey(Integer id);

    List<Suit> selectAll();

    int updateByPrimaryKey(Suit record);

    // Dto
    int insertDto(SuitDto record);

    // Dto
    SuitDto selectDtoByPrimaryKey(Integer id);

    // Dto
    List<SuitDto> selectAllDto();

    // Dto
    int updateDtoByPrimaryKey(SuitDto record);

    // Dto
    List<SuitDto> selectDtoBySuitName(String suitName);

    // Dto
    List<SuitDto> selectAllDtoOrderByPrice();

    // Dto
    List<SuitDto> selectAllDtoOrderByTime();

    // Dto
    List<SuitDto> selectAllDtoBySortId(Integer sortId);

}