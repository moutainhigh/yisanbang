package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.domain.Sort;
import com.vtmer.yisanbang.dto.SortDto;
import com.vtmer.yisanbang.dto.SuitSortWithChildrenSort;

import java.util.List;

public interface SortService {

    // 新建分类信息
    int addSortInfo(SortDto sortDto);

    // 查询指定分类信息
    Sort listSortInfoById(Integer sortId);

    // 查询所有校服分类信息
    List<Sort> listAllUniformSort();

    // 查询职业装对应级别分类信息
    List<Sort> listSuitSort(Integer parentId);

    // 查询所有职业装一级分类及其所包含二级分类
    List<SuitSortWithChildrenSort> listAllSuitSortWithChildren();

    // 修改指定分类信息
    int updateSortInfo(Integer sortId, SortDto sortDto);

    // 修改分类是否显示
    boolean updateIsShowed(Integer sortId);

    // 判断分类显示顺序是否已经存在
    boolean isShowOrderExisted(Integer order, Boolean isSuit, Integer parentId);

    // 删除指定分类信息
    int deleteSort(Integer sortId);

}
