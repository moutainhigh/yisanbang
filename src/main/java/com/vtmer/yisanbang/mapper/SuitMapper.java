package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.Suit;
import com.vtmer.yisanbang.dto.SuitDTO;
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
    int insertDto(SuitDTO record);

    // Dto
    SuitDTO selectDtoByPrimaryKey(Integer id);

    // Dto
    List<SuitDTO> selectAllDto();

    // Dto
    int updateDtoByPrimaryKey(SuitDTO record);

    // Dto
    List<SuitDTO> selectDtoByContent(String content);

    // Dto
    List<SuitDTO> selectAllDtoOrderByPrice();

    // Dto
    List<SuitDTO> selectAllDtoOrderByTime();

    // Dto
    List<SuitDTO> selectAllDtoBySortId(Integer sortId);

    // Dto
    List<SuitDTO> selectSuitBySortIdOrderByPrice(Integer sortId);

    // Dto
    List<SuitDTO> selectSuitBySortIdOrderByTime(Integer sortId);

    int hideSuit(Integer suitId);

    int showSuit(Integer suitId);

    List<SuitDTO> selectAllShowDto();

    List<SuitDTO> selectAllDtoToSetDelete();
}