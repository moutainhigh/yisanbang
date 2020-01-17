package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.Postage;
import java.util.List;

public interface PostageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Postage record);

    Postage selectByPrimaryKey(Integer id);

    List<Postage> selectAll();

    int updateByPrimaryKey(Postage record);
}