package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.PartSize;
import com.vtmer.yisanbang.dto.PartSizeDTO;
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

    int updateInventoryByPrimaryKey(Map<String,Integer> inventoryMap);

    // Dto
    int insertDto(PartSizeDTO record);

    // Dto
    PartSizeDTO selectDtoByPrimaryKey(Integer id);

    // Dto
    List<PartSizeDTO> selectAllDto();

    // Dto
    int updateDtoByPrimaryKey(PartSizeDTO record);

    // Dto
    List<PartSizeDTO> selectAllBySuitId(Integer suitId);
}