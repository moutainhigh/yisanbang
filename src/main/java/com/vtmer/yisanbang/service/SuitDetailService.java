package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.dto.SuitDetailDTO;

import java.util.List;

public interface SuitDetailService {
    // 添加套装详细信息
    public boolean addSuitDetail(SuitDetailDTO suitDetailDto);

    // 删除套装详细信息
    public boolean deleteSuitDetail(Integer suitDetailId);

    // 更新套装详细信息
    public boolean updateSuitDetail(SuitDetailDTO suitDetailDto);

    // 查找所有套装详细信息
    public List<SuitDetailDTO> selectAllDto();

    // 根据套装id查找套装的所有套装详细信息
    public List<SuitDetailDTO> selectAllDtoBySuitId(Integer suitId);

    // 根据套装详细id查找套装详细信息
    public SuitDetailDTO selectSuitDetailByID(Integer suitDetailId);

    // 删除套装的所有套装详情信息
    public boolean deleteAllSuitDetailBySuitId(Integer suitId);

}
