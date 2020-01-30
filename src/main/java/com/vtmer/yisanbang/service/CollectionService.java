package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.domain.Collection;
import com.vtmer.yisanbang.dto.CollectionDto;

import java.util.List;

public interface CollectionService {

    public Integer insertOne(Collection collection);

    public Boolean delete(List<Collection> collectionList);

    public List<CollectionDto> selectAllByUserId(Integer userId);
}
