package com.vtmer.yisanbang.service.impl;

import com.vtmer.yisanbang.domain.Sort;
import com.vtmer.yisanbang.dto.SortDto;
import com.vtmer.yisanbang.dto.SuitSortWithChildrenSort;
import com.vtmer.yisanbang.mapper.SortMapper;
import com.vtmer.yisanbang.service.SortService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SortServiceImpl implements SortService {

    @Autowired
    private SortMapper sortMapper;

    @Override
    public int addSortInfo(SortDto sortDto) {
        setSortLevel(sortDto);
        return sortMapper.insertSort(sortDto);
    }

    @Override
    public Sort listSortInfoById(Integer sortId) {
        return sortMapper.selectByPrimaryKey(sortId);
    }

    @Override
    public List<Sort> listAllUniformSort() {
        return sortMapper.selectAllUniformSort();
    }

    @Override
    public List<Sort> listSuitSort(Integer parentId) {
        return sortMapper.selectSuitSort(parentId);
    }

    @Override
    public List<SuitSortWithChildrenSort> listAllSuitSortWithChildren() {
        return null;
    }

    @Override
    public int updateSortInfo(Integer sortId, SortDto sortDto) {
        sortDto.setId(sortId);
        setSortLevel(sortDto);
        return sortMapper.updateSortInfoSelective(sortDto);
    }

    @Override
    public boolean updateIsShowed(Integer sortId) {
        int flag = 0;
        Sort sort = sortMapper.selectByPrimaryKey(sortId);
        if (sort != null) {
            if (sort.getIsShow()) {
                flag = sortMapper.updateSort2UnShow(sortId);
            } else {
                flag = sortMapper.updateSort2Show(sortId);
            }
        }
        if (flag > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isShowOrderExisted(Integer order, Boolean isSuit, Integer parentId) {
        Integer isSuitFlag;
        if (isSuit) {
            isSuitFlag = 1;
        } else {
            isSuitFlag = 0;
        }
        List<Integer> orders = sortMapper.selectExitedOrder(isSuitFlag, parentId);
        return orders.contains(order);
    }

    @Override
    public int deleteSort(Integer sortId) {
        return sortMapper.deleteByPrimaryKey(sortId);
    }

    /**
     * 根据分类的parentId设置分类的level
     */
    private void setSortLevel(SortDto sortDto) {
        // 没有父分类时为一级分类
        if (sortDto.getParentId() == 0) {
            sortDto.setLevel(0);
        } else {
            // 有父分类时选择根据父分类level设置
            Sort parentSort = sortMapper.selectByPrimaryKey(sortDto.getParentId());
            if (parentSort != null) {
                sortDto.setLevel(parentSort.getLevel() + 1);
            } else {
                sortDto.setLevel(0);
            }
        }
    }
}
