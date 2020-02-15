package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.Collection;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CollectionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Collection record);

    Collection selectByPrimaryKey(Integer id);

    List<Collection> selectAll();

    int updateByPrimaryKey(Collection record);

    Boolean checkExist(Collection collection);

    Boolean deleteOne(Collection collection);

    List<Collection> selectAllByUserId(Integer userId);

}