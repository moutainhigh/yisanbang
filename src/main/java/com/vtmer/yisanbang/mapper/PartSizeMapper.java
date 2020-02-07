package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.PartSize;
import com.vtmer.yisanbang.dto.PartSizeDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface PartSizeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PartSize record);

    PartSize selectByPrimaryKey(Integer id);

    List<PartSize> selectAll();

    int updateByPrimaryKey(PartSize record);

    void updateInventoryByPrimaryKey(Map<String,Integer> inventoryMap);

    // Dto
    int insertDto(PartSizeDto record);

    // Dto
    PartSizeDto selectDtoByPrimaryKey(Integer id);

    // Dto
    List<PartSizeDto> selectAllDto();

    // Dto
    int updateDtoByPrimaryKey(PartSizeDto record);

    // Dto
    List<PartSizeDto> selectAllBySuitId(Integer suitId);
}