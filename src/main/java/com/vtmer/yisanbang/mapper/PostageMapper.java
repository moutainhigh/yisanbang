package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.Postage;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface PostageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Postage record);

    Postage selectByPrimaryKey(Integer id);

    List<Postage> selectAll();

    int updateByPrimaryKey(Postage record);

    Postage select();

    int update(Postage postage);

    int delete();
}