package com.vtmer.yisanbang.service.impl;

import com.vtmer.yisanbang.common.util.comparator.ComparatorGoodsOrSuitDetail;
import com.vtmer.yisanbang.dto.SuitDetailDTO;
import com.vtmer.yisanbang.mapper.SuitDetailMapper;
import com.vtmer.yisanbang.service.SuitDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SuitDetailServiceImpl implements SuitDetailService {
    @Autowired
    private SuitDetailMapper suitDetailMapper;

    @Override
    // 添加套装详细信息
    public boolean addSuitDetail(SuitDetailDTO suitDetailDto) {
        int addFlag = suitDetailMapper.insertDto(suitDetailDto);
        if (addFlag > 0) {
            return true;
        }
        return false;
    }

    @Override
    // 删除套装详细信息
    public boolean deleteSuitDetail(Integer suitDetailId) {
        int deleteFlag = suitDetailMapper.deleteByPrimaryKey(suitDetailId);
        if (deleteFlag > 0) {
            return true;
        }
        return false;
    }

    @Override
    // 更新套装详细信息
    public boolean updateSuitDetail(SuitDetailDTO suitDetailDto) {
        int updateFlag = suitDetailMapper.updateDtoByPrimaryKey(suitDetailDto);
        if (updateFlag > 0) {
            return true;
        }
        return false;
    }

    @Override
    // 查找所有套装详细信息
    public List<SuitDetailDTO> selectAllDto() {
        List<SuitDetailDTO> suitDetailDtoList = suitDetailMapper.selectAllDto();
        if (suitDetailDtoList != null && !suitDetailDtoList.isEmpty()) {
            return suitDetailDtoList;
        }
        return null;
    }

    @Override
    // 根据套装id查找套装的所有套装详细信息
    public List<SuitDetailDTO> selectAllDtoBySuitId(Integer suitId) {
        List<SuitDetailDTO> suitDetailDtoList = suitDetailMapper.selectAllDtoBySuitId(suitId);
        if (suitDetailDtoList != null && !suitDetailDtoList.isEmpty()) {
            ComparatorGoodsOrSuitDetail comparatorGoodsOrSuitDetail = new ComparatorGoodsOrSuitDetail();
            Collections.sort(suitDetailDtoList,comparatorGoodsOrSuitDetail);
            return suitDetailDtoList;
        }
        return null;
    }

    @Override
    // 根据套装详细id查找套装详细信息
    public SuitDetailDTO selectSuitDetailByID(Integer suitDetailId) {
        SuitDetailDTO suitDetailDto = suitDetailMapper.selectDtoByPrimaryKey(suitDetailId);
        if (suitDetailDto != null) {
            return suitDetailDto;
        }
        return null;
    }
}
