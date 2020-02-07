package com.vtmer.yisanbang.service.impl;

import com.vtmer.yisanbang.common.Md5Util;
import com.vtmer.yisanbang.dto.SuitDetailDto;
import com.vtmer.yisanbang.mapper.SuitDetailMapper;
import com.vtmer.yisanbang.service.SuitDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class SuitDetailServiceImpl implements SuitDetailService {
    @Autowired
    private SuitDetailMapper suitDetailMapper;

    @Override
    // 添加套装详细信息
    public boolean addSuitDetail(SuitDetailDto suitDetailDto) {
        int addFlag = suitDetailMapper.insertDto(suitDetailDto);
        if (addFlag > 0) return true;
        return false;
    }

    @Override
    // 删除套装详细信息
    public boolean deleteSuitDetail(Integer suitDetailId) {
        int deleteFlag = suitDetailMapper.deleteByPrimaryKey(suitDetailId);
        if (deleteFlag > 0) return true;
        return false;
    }

    @Override
    // 更新套装详细信息
    public boolean updateSuitDetail(SuitDetailDto suitDetailDto) {
        int updateFlag = suitDetailMapper.updateDtoByPrimaryKey(suitDetailDto);
        if (updateFlag > 0) return true;
        return false;
    }

    @Override
    // 查找所有套装详细信息
    public List<SuitDetailDto> selectAllDto() {
        List<SuitDetailDto> suitDetailDtoList = suitDetailMapper.selectAllDto();
        if (suitDetailDtoList != null && !suitDetailDtoList.isEmpty())
            return suitDetailDtoList;
        return null;
    }

    @Override
    // 根据套装id查找套装的所有套装详细信息
    public List<SuitDetailDto> selectAllDtoBySuitId(Integer suitId) {
        List<SuitDetailDto> suitDetailDtoList = suitDetailMapper.selectAllDtoBySuitId(suitId);
        if (suitDetailDtoList != null && !suitDetailDtoList.isEmpty())
            return suitDetailDtoList;
        return null;
    }

    @Override
    // 根据套装详细id查找套装详细信息
    public SuitDetailDto selectSuitDetailByID(Integer suitDetailId) {
        SuitDetailDto suitDetailDto = suitDetailMapper.selectDtoByPrimaryKey(suitDetailId);
        if (suitDetailDto != null) return suitDetailDto;
        return null;
    }

    @Override
    // 查看套装详细信息是否相同
    public boolean judgeSuitDetail(SuitDetailDto suitDetailDto, List<SuitDetailDto> suitDetailDtoList) {
        File file1 = new File(suitDetailDto.getPirtucePath());
        String img1 = Md5Util.getFileMD5(file1);
        for (SuitDetailDto suitDetail : suitDetailDtoList) {
            File file2 = new File(suitDetail.getPirtucePath());
            String img2 = Md5Util.getFileMD5(file2);
            if (img1.equals(img2))
                return true;
        }
        return false;
    }
}
