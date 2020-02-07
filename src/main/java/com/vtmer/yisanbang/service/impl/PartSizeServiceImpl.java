package com.vtmer.yisanbang.service.impl;

import com.vtmer.yisanbang.dto.ColorSizeDto;
import com.vtmer.yisanbang.dto.PartSizeDto;
import com.vtmer.yisanbang.mapper.PartSizeMapper;
import com.vtmer.yisanbang.service.PartSizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartSizeServiceImpl implements PartSizeService {
    @Autowired
    private PartSizeMapper partSizeMapper;

    @Override
    // 查找所有部件尺寸
    public List<PartSizeDto> selectAll() {
        List<PartSizeDto> partSizeDtos = partSizeMapper.selectAllDto();
        if (partSizeDtos != null && !partSizeDtos.isEmpty())
            return partSizeDtos;
        return null;
    }

    @Override
    // 添加部件尺寸
    public boolean addPartSize(PartSizeDto partSizeDto) {
        int addFlag = partSizeMapper.insertDto(partSizeDto);
        if (addFlag > 0) return true;
        return false;
    }

    @Override
    // 删除部件尺寸
    public boolean deletePartSize(Integer partSizeId) {
        int deleteFlag = partSizeMapper.deleteByPrimaryKey(partSizeId);
        if (deleteFlag > 0) return true;
        return false;
    }

    @Override
    // 更新部件尺寸
    public boolean updatePartSize(PartSizeDto partSizeDto) {
        int updateFlag = partSizeMapper.updateDtoByPrimaryKey(partSizeDto);
        if (updateFlag > 0) return true;
        return false;
    }

    @Override
    // 根据部件尺寸id查找部件尺寸
    public PartSizeDto selectPartSizeById(Integer partSizeId) {
        PartSizeDto partSizeDto = partSizeMapper.selectDtoByPrimaryKey(partSizeId);
        if (partSizeDto != null) return partSizeDto;
        return null;
    }

    @Override
    // 根据套装id查找所有该套装的部件尺寸
    public List<PartSizeDto> selectAllBySuitId(Integer suitId) {
        List<PartSizeDto> partSizeDtos = partSizeMapper.selectAllBySuitId(suitId);
        if (partSizeDtos != null && !partSizeDtos.isEmpty())
            return partSizeDtos;
        return null;
    }

    @Override
    // 判断部件尺寸是否存在
    public boolean judgePartSize(PartSizeDto partSizeDto) {
        List<PartSizeDto> partSizeDtoList = partSizeMapper.selectAllDto();
        for (PartSizeDto partSize : partSizeDtoList) {
            if (partSize.getInventory() == partSizeDto.getInventory())
                if (partSize.getPrice() == partSizeDto.getPrice())
                    if (partSize.getSuitId() == partSizeDto.getSuitId())
                        if (partSize.getModel().equals(partSizeDto.getModel()))
                            if (partSize.getPart().equals(partSizeDto.getPart()))
                                if (partSize.getSize().equals(partSizeDto.getSize()))
                                    return true;
        }
        return false;
    }
}
