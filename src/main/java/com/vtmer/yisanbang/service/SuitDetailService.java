package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.dto.SuitDetailDto;

import java.util.List;

public interface SuitDetailService {
    // 添加套装详细信息
    public boolean addSuitDetail(SuitDetailDto suitDetailDto);

    // 删除套装详细信息
    public boolean deleteSuitDetail(Integer suitDetailId);

    // 更新套装详细信息
    public boolean updateSuitDetail(SuitDetailDto suitDetailDto);

    // 查找所有套装详细信息
    public List<SuitDetailDto> selectAllDto();

    // 根据套装id查找套装的所有套装详细信息
    public List<SuitDetailDto> selectAllDtoBySuitId(Integer suitId);

    // 根据套装详细id查找套装详细信息
    public SuitDetailDto selectSuitDetailByID(Integer suitDetailId);

    // 查看套装详细信息是否相同
    public boolean judgeSuitDetail(SuitDetailDto suitDetailDto, List<SuitDetailDto> suitDetailDtoList);
}
