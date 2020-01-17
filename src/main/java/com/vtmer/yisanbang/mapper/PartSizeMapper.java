package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.PartSize;
import java.util.List;

public interface PartSizeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PartSize record);

    PartSize selectByPrimaryKey(Integer id);

    List<PartSize> selectAll();

    int updateByPrimaryKey(PartSize record);
}