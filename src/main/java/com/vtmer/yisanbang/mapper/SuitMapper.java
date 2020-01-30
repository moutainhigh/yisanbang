package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.Suit;
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

}