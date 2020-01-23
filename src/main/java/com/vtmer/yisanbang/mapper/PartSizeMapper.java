package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.PartSize;
import com.vtmer.yisanbang.dto.SuitDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface PartSizeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PartSize record);

    PartSize selectByPrimaryKey(Integer id);

    List<PartSize> selectAll();

    int updateByPrimaryKey(PartSize record);

    SuitDto selectSuitDtoByPartSizeId(Integer partSizeId);
}