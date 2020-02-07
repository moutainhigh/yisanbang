package com.vtmer.yisanbang.service.impl;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.vtmer.yisanbang.domain.Suit;
import com.vtmer.yisanbang.dto.SuitDto;
import com.vtmer.yisanbang.mapper.SuitMapper;
import com.vtmer.yisanbang.service.SuitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SuitServiceImpl implements SuitService {
    @Autowired
    private SuitMapper suitMapper;

    @Override
    // 查找所有套装
    public List<SuitDto> selectAll() {
        List<SuitDto> suitList = suitMapper.selectAllDto();
        if (suitList != null && !suitList.isEmpty()) return suitList;
        return null;
    }

    @Override
    // 根据套装id删除套装
    public boolean deleteSuitById(Integer suitId) {
        int deleteFlaf = suitMapper.deleteByPrimaryKey(suitId);
        if (deleteFlaf > 0) return true;
        return false;
    }

    @Override
    // 根据套装id更新套装
    public boolean updateSuitById(SuitDto suitDto) {
        int updateFlag = suitMapper.updateDtoByPrimaryKey(suitDto);
        if (updateFlag > 0) return true;
        return false;
    }

    @Override
    // 添加套装
    public boolean addSuit(SuitDto suitDto) {
        int insertFlag = suitMapper.insertDto(suitDto);
        if (insertFlag > 0) return true;
        return false;
    }

    @Override
    public boolean judgeSuit(SuitDto suitDto, List<SuitDto> suitDtoList) {
        for (SuitDto suit : suitDtoList) {
            if (suit.getName().equals(suitDto.getName()))
                return true;
        }
        return false;
    }

    @Override
    // 根据套装id查找套装
    public SuitDto selectSuitById(Integer suitId) {
        SuitDto suitDto = suitMapper.selectDtoByPrimaryKey(suitId);
        if (suitDto != null) return suitDto;
        return null;
    }

    @Override
    // 根据套装名字查找套装
    public SuitDto selectSuitByName(String suitName) {
        SuitDto suitDto = suitMapper.selectDtoBySuitName(suitName);
        if (suitDto != null) return suitDto;
        return null;
    }

    @Override
    // 根据套装的最低价格排序进行显示
    public List<SuitDto> selectSuitOrderByPrice() {
        List<SuitDto> suitDtoList = suitMapper.selectAllDtoOrderByPrice();
        if (suitDtoList != null && !suitDtoList.isEmpty()) return suitDtoList;
        return null;
    }

    @Override
    // 根据套装的时间排序进行显示
    public List<SuitDto> selectSuitOrderByTime() {
        List<SuitDto> suitDtoList = suitMapper.selectAllDtoOrderByTime();
        if (suitDtoList != null && !suitDtoList.isEmpty()) return suitDtoList;
        return null;
    }

    @Override
    // 根据分类id显示套装
    public List<SuitDto> selectSuitBySort(Integer sortId) {
        List<SuitDto> suitDtoList = suitMapper.selectAllDtoBySortId(sortId);
        if (suitDtoList != null && !suitDtoList.isEmpty()) return suitDtoList;
        return null;
    }

    @Override
    // 隐藏套装
    public boolean hideSuit(SuitDto suitDto) {
        suitDto.setShow(false);
        int updateFlag = suitMapper.updateDtoByPrimaryKey(suitDto);
        if (updateFlag > 0) return true;
        return false;
    }
}
