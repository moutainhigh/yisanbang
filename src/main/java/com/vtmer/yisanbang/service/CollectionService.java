package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.domain.Collection;
import com.vtmer.yisanbang.vo.CollectionVo;

import java.util.List;

public interface CollectionService {

    Integer insertOne(Collection collection);

    int delete(List<Integer> collectionIdList);

    List<CollectionVo> selectAllByUserId(Integer userId);
}
