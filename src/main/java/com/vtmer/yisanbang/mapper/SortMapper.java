package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.Sort;
import com.vtmer.yisanbang.dto.SortDto;
import com.vtmer.yisanbang.dto.SuitSortWithChildrenSort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SortMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Sort record);

    Sort selectByPrimaryKey(Integer id);

    List<Sort> selectAll();

    int updateByPrimaryKey(Sort record);

    // 新增分类信息
    int insertSort(SortDto sortDto);

    // 查询所有校服分类
    List<Sort> selectAllUniformSort();

    // 查询职业装对应级别分类信息
    List<Sort> selectSuitSort(Integer parentId);

    // 查询所有职业装一级分类及其所包含二级分类
    List<SuitSortWithChildrenSort> selectAllSuitSortWithChildren();

    // 修改分类为显示状态
    int updateSort2Show(Integer sortId);

    // 修改分类为不显示状态
    int updateSort2UnShow(Integer sortId);

    // 修改分类信息
    int updateSortInfoSelective(SortDto sortDto);

    // 查询已存在的显示排序
    List<Integer> selectExitedOrder(Integer isSuit, Integer parentId);

}