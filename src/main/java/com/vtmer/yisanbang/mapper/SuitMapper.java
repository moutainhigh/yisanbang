package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.Suit;
import java.util.List;

public interface SuitMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Suit record);

    Suit selectByPrimaryKey(Integer id);

    List<Suit> selectAll();

    int updateByPrimaryKey(Suit record);
}