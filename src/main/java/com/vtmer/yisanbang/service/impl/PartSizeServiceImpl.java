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

    @Override
    // 查找所有部件
    public List<String> selectAllPartById(Integer suitId) {
        List<PartSizeDto> partSizeDtos = partSizeMapper.selectAllBySuitId(suitId);
        List<String> partList = null;
        for (PartSizeDto partSize : partSizeDtos) {
            String part = partSize.getPart();
            partList.add(part);
        }
        if (partList != null) return partList;
        return null;
    }

    @Override
    // 查找所有尺寸
    public List<String> selectAllSizeById(Integer suitId) {
        List<PartSizeDto> partSizeDtos = partSizeMapper.selectAllBySuitId(suitId);
        List<String> sizeList = null;
        for (PartSizeDto partSize : partSizeDtos) {
            String size = partSize.getSize();
            sizeList.add(size);
        }
        if (sizeList != null) return sizeList;
        return null;
    }

    @Override
    // 根据颜色尺寸查找显示库存
    public Integer selectInventoryByPartSize(Integer suitId, String part, String size) {
        List<PartSizeDto> partSizeDtos = partSizeMapper.selectAllBySuitId(suitId);
        for (PartSizeDto partSize : partSizeDtos) {
            if (partSize.getPart().equals(part))
                if (partSize.getSize().equals(size))
                    return partSize.getInventory();
        }
        return null;
    }

    @Override
    // 根据部件尺寸返回价格
    public Double selectPriceByPartSize(Integer suitId, String part, String size) {
        List<PartSizeDto> partSizeDtos = partSizeMapper.selectAllBySuitId(suitId);
        for (PartSizeDto partSize : partSizeDtos) {
            if (partSize.getPart().equals(part))
                if (partSize.getSize().equals(size))
                    return partSize.getPrice();
        }
        return null;
    }

    @Override
    // 查找最低价
    public Double selectLowPriceBySuitId(Integer suitId) {
        List<PartSizeDto> partSizeDtos = partSizeMapper.selectAllBySuitId(suitId);
        Double lowPrice = null;
        for (PartSizeDto partSize : partSizeDtos) {
            Double price = partSize.getPrice();
            for (PartSizeDto partSizeDto : partSizeDtos) {
                if (partSizeDto.getPrice() < price)
                    lowPrice = partSizeDto.getPrice();
            }
        }
        return lowPrice;
    }

    @Override
    // 查找最高价
    public Double selecgHighPriceBySuitId(Integer suitId) {
        List<PartSizeDto> partSizeDtos = partSizeMapper.selectAllBySuitId(suitId);
        Double highPrice = null;
        for (PartSizeDto partSize : partSizeDtos) {
            Double price = partSize.getPrice();
            for (PartSizeDto partSizeDto : partSizeDtos) {
                if (partSizeDto.getPrice() > price)
                    highPrice = partSizeDto.getPrice();
            }
        }
        return highPrice;
    }
}
