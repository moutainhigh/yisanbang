package com.vtmer.yisanbang.service.impl;

import com.vtmer.yisanbang.common.util.comparator.ComparatorGoodsSuit;
import com.vtmer.yisanbang.common.util.comparator.ComparatorGoodsSuitByPrice;
import com.vtmer.yisanbang.common.util.comparator.ComparatorGoodsSuitByTime;
import com.vtmer.yisanbang.dto.SuitDTO;
import com.vtmer.yisanbang.dto.SuitDetailDTO;
import com.vtmer.yisanbang.mapper.PartSizeMapper;
import com.vtmer.yisanbang.mapper.SuitDetailMapper;
import com.vtmer.yisanbang.mapper.SuitMapper;
import com.vtmer.yisanbang.service.SuitDetailService;
import com.vtmer.yisanbang.service.SuitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SuitServiceImpl implements SuitService {
    @Autowired
    private SuitMapper suitMapper;

    @Autowired
    private SuitDetailMapper suitDetailMapper;

    @Autowired
    private PartSizeMapper partSizeMapper;

    @Override
    // 查找所有套装
    public List<SuitDTO> selectAll() {
        List<SuitDTO> suitList = suitMapper.selectAllDto();
        if (suitList != null && !suitList.isEmpty()) {
            return suitList;
        }
        return null;
    }

    @Override
    // 根据套装id删除套装
    public boolean deleteSuitById(Integer suitId) {
        SuitDTO suitDTO = suitMapper.selectDtoByPrimaryKey(suitId);
        suitDTO.setWhetherDelete(true);
        int deleteFlag = suitMapper.updateDtoByPrimaryKey(suitDTO);
        if (deleteFlag > 0) {
            return true;
        }
        return false;
    }

    @Override
    // 根据套装id更新套装
    public boolean updateSuitById(SuitDTO suitDto) {
        int updateFlag = suitMapper.updateDtoByPrimaryKey(suitDto);
        if (updateFlag > 0) {
            return true;
        }
        return false;
    }

    @Override
    // 添加套装
    public boolean addSuit(SuitDTO suitDto) {
        int insertFlag = suitMapper.insertDto(suitDto);
        if (insertFlag > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean judgeSuit(SuitDTO suitDto, List<SuitDTO> suitDtoList) {
        for (SuitDTO suit : suitDtoList) {
            if (suit.getName().equals(suitDto.getName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    // 根据套装id查找套装
    public SuitDTO selectSuitById(Integer suitId) {
        SuitDTO suitDto = suitMapper.selectDtoByPrimaryKey(suitId);
        if (suitDto != null) {
            return suitDto;
        }
        return null;
    }

    @Override
    // 根据套装名字与简介查找套装
    public List<SuitDTO> selectSuitByContent(String content) {
        List<SuitDTO> suitDtoList = suitMapper.selectDtoByContent(content);
        if (suitDtoList != null && !suitDtoList.isEmpty()) {
            return suitDtoList;
        }
        return null;
    }

    @Override
    // 根据套装的最低价格排序进行显示
    public List<SuitDTO> selectSuitOrderByPrice() {
        List<SuitDTO> suitDtoList = suitMapper.selectAllDtoOrderByPrice();
        if (suitDtoList != null && !suitDtoList.isEmpty()) {
            ComparatorGoodsSuitByPrice comparatorGoodsSuitByPrice = new ComparatorGoodsSuitByPrice();
            Collections.sort(suitDtoList, comparatorGoodsSuitByPrice);
            return suitDtoList;
        }
        return null;
    }

    @Override
    // 根据套装的时间排序进行显示
    public List<SuitDTO> selectSuitOrderByTime() {
        List<SuitDTO> suitDtoList = suitMapper.selectAllDtoOrderByTime();
        if (suitDtoList != null && !suitDtoList.isEmpty()) {
            ComparatorGoodsSuitByTime comparatorGoodsSuitByTime = new ComparatorGoodsSuitByTime();
            Collections.sort(suitDtoList, comparatorGoodsSuitByTime);
            return suitDtoList;
        }
        return null;
    }

    @Override
    // 根据分类id显示套装
    public List<SuitDTO> selectSuitBySort(Integer sortId) {
        List<SuitDTO> suitDtoList = suitMapper.selectAllDtoBySortId(sortId);
        if (suitDtoList != null && !suitDtoList.isEmpty()) {
            ComparatorGoodsSuit comparatorGoodsSuit = new ComparatorGoodsSuit();
            Collections.sort(suitDtoList, comparatorGoodsSuit);
            return suitDtoList;
        }
        return null;
    }

    @Override
    // 根据分类id与套装的最低价格排序进行显示
    public List<SuitDTO> selectSuitBySortIdOrderByPrice(Integer sortId) {
        List<SuitDTO> suitDTOList = suitMapper.selectSuitBySortIdOrderByPrice(sortId);
        if (suitDTOList != null && !suitDTOList.isEmpty()) {
            ComparatorGoodsSuitByPrice comparatorGoodsSuitByPrice = new ComparatorGoodsSuitByPrice();
            Collections.sort(suitDTOList, comparatorGoodsSuitByPrice);
            return suitDTOList;
        }
        return null;
    }

    @Override
    // 根据分类id与套装的时间排序进行显示
    public List<SuitDTO> selectSuitBySortIdOrderByTime(Integer sortId) {
        List<SuitDTO> suitDTOList = suitMapper.selectSuitBySortIdOrderByTime(sortId);
        if (suitDTOList != null && !suitDTOList.isEmpty()) {
            ComparatorGoodsSuitByTime comparatorGoodsSuitByTime = new ComparatorGoodsSuitByTime();
            Collections.sort(suitDTOList, comparatorGoodsSuitByTime);
            return suitDTOList;
        }
        return null;
    }

    @Override
    // 隐藏套装
    public boolean hideSuit(SuitDTO suitDto) {
        if (suitDto.getIsShow()) {
            int hideFlag = suitMapper.hideSuit(suitDto.getId());
            if (hideFlag > 0) {
                return true;
            }
        } else {
            int showFlag = suitMapper.showSuit(suitDto.getId());
            if (showFlag > 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<SuitDTO> selectAllShow() {
        List<SuitDTO> suitList = suitMapper.selectAllShowDto();
        if (suitList != null && !suitList.isEmpty()) {
            for (SuitDTO suitDTO : suitList) {
                List<SuitDetailDTO> suitDetailDTOS = suitDetailMapper.selectAllDtoBySuitId(suitDTO.getId());
                if (!(suitDetailDTOS != null && !suitDetailDTOS.isEmpty())){
                    suitDTO.setIsShow(false);
                    suitMapper.updateDtoByPrimaryKey(suitDTO);
                }
                partSizeMapper.selectAllBySuitId(suitDTO.getId());
                if (!(suitDetailDTOS != null && !suitDetailDTOS.isEmpty())){
                    suitDTO.setIsShow(false);
                    suitMapper.updateDtoByPrimaryKey(suitDTO);
                }
            }
            return suitList;
        }
        return null;
    }

    @Override
    public boolean setSuitAddress(String address) {
        List<SuitDTO> suitDTOS = suitMapper.selectAllDto();
        if (suitDTOS != null && !suitDTOS.isEmpty()) {
            for (SuitDTO suitDTO : suitDTOS) {
                suitDTO.setAddress(address);
                suitMapper.updateDtoByPrimaryKey(suitDTO);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean setSuitNoDelete() {
        List<SuitDTO> suitDTOS = suitMapper.selectAllDtoToSetDelete();
        if (suitDTOS != null && !suitDTOS.isEmpty()) {
            for (SuitDTO suitDTO : suitDTOS) {
                suitDTO.setWhetherDelete(false);
                suitMapper.updateDtoByPrimaryKey(suitDTO);
            }
            return true;
        }
        return false;
    }
}
