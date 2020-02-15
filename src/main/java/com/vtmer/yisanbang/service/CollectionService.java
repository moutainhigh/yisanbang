package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.domain.Collection;
import com.vtmer.yisanbang.vo.CollectionVo;

import java.util.List;

public interface CollectionService {

    void insertOne(Collection collection);

    void delete(List<Integer> collectionIdList);

    List<CollectionVo> selectAllByUserId();
}
