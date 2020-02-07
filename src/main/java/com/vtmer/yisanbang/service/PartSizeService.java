package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.dto.ColorSizeDto;
import com.vtmer.yisanbang.dto.PartSizeDto;

import java.util.List;

public interface PartSizeService {
    // 查找所有部件尺寸
    public List<PartSizeDto> selectAll();

    // 添加部件尺寸
    public boolean addPartSize(PartSizeDto partSizeDto);

    // 删除部件尺寸
    public boolean deletePartSize(Integer partSizeId);

    // 更新部件尺寸
    public boolean updatePartSize(PartSizeDto partSizeDto);

    // 根据部件尺寸id查找部件尺寸
    public PartSizeDto selectPartSizeById(Integer partSizeId);

    // 根据套装id查找所有该套装的部件尺寸
    public List<PartSizeDto> selectAllBySuitId(Integer suitId);

    // 判断部件尺寸是否存在
    public boolean judgePartSize(PartSizeDto partSizeDto);
}
