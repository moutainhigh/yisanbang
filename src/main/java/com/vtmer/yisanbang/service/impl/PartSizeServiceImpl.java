package com.vtmer.yisanbang.service.impl;

import com.vtmer.yisanbang.dto.CartGoodsDTO;
import com.vtmer.yisanbang.dto.PartSizeDTO;
import com.vtmer.yisanbang.dto.SuitDTO;
import com.vtmer.yisanbang.mapper.PartSizeMapper;
import com.vtmer.yisanbang.mapper.SuitMapper;
import com.vtmer.yisanbang.service.PartSizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PartSizeServiceImpl implements PartSizeService {
    @Autowired
    private PartSizeMapper partSizeMapper;

    @Autowired
    private SuitMapper suitMapper;

    public CartGoodsDTO setSkuById(CartGoodsDTO cartGoodsDto) {
        PartSizeDTO suitSku = partSizeMapper.selectDtoByPrimaryKey(cartGoodsDto.getColorSizeId());
        // 套装尺寸
        cartGoodsDto.setSize(suitSku.getSize());
        // 套装部件
        cartGoodsDto.setPartOrColor(suitSku.getPart());
        // 查询商品信息
        SuitDTO suitDto = suitMapper.selectDtoByPrimaryKey(suitSku.getSuitId());
        // 套装标题
        cartGoodsDto.setTitle(suitDto.getName());
        // 套装价格（最低价）
        cartGoodsDto.setPrice(suitDto.getLowestPrice());
        // 设置套装id
        cartGoodsDto.setId(suitDto.getId());
        // 套装图片
        cartGoodsDto.setPicture(suitDto.getPicture());
        return cartGoodsDto;
    }

    @Override
    // 查找所有部件尺寸
    public List<PartSizeDTO> selectAll() {
        List<PartSizeDTO> partSizeDtos = partSizeMapper.selectAllDto();
        if (partSizeDtos != null && !partSizeDtos.isEmpty()) {
            return partSizeDtos;
        }
        return null;
    }

    @Override
    // 添加部件尺寸
    public boolean addPartSize(PartSizeDTO partSizeDto) {
        int addFlag = partSizeMapper.insertDto(partSizeDto);
        if (addFlag > 0) {
            return true;
        }
        return false;
    }

    @Override
    // 删除部件尺寸
    public boolean deletePartSize(Integer partSizeId) {
        int deleteFlag = partSizeMapper.deleteByPrimaryKey(partSizeId);
        if (deleteFlag > 0) {
            return true;
        }
        return false;
    }

    @Override
    // 更新部件尺寸
    public boolean updatePartSize(PartSizeDTO partSizeDto) {
        int updateFlag = partSizeMapper.updateDtoByPrimaryKey(partSizeDto);
        if (updateFlag > 0) {
            return true;
        }
        return false;
    }

    @Override
    // 根据部件尺寸id查找部件尺寸
    public PartSizeDTO selectPartSizeById(Integer partSizeId) {
        PartSizeDTO partSizeDto = partSizeMapper.selectDtoByPrimaryKey(partSizeId);
        if (partSizeDto != null) {
            return partSizeDto;
        }
        return null;
    }

    @Override
    // 根据套装id查找所有该套装的部件尺寸
    public List<PartSizeDTO> selectAllBySuitId(Integer suitId) {
        List<PartSizeDTO> partSizeDtos = partSizeMapper.selectAllBySuitId(suitId);
        if (partSizeDtos != null && !partSizeDtos.isEmpty()) {
            return partSizeDtos;
        }
        return null;
    }

    @Override
    // 判断部件尺寸是否存在
    public boolean judgePartSize(PartSizeDTO partSizeDto) {
        List<PartSizeDTO> partSizeDtoList = partSizeMapper.selectAllDto();
        for (PartSizeDTO partSize : partSizeDtoList) {
            if (partSize.getSuitId().equals(partSizeDto.getSuitId())) {
                if (partSize.getPart().equals(partSizeDto.getPart())) {
                    if (partSize.getSize().equals(partSizeDto.getSize())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    // 查找所有部件
    public List<String> selectAllPartById(Integer suitId) {
        List<PartSizeDTO> partSizeDtos = partSizeMapper.selectAllBySuitId(suitId);
        List<String> partList = new ArrayList<>();
        if (partSizeDtos != null && !partSizeDtos.isEmpty()) {
            for (PartSizeDTO partSize : partSizeDtos) {
                String part = partSize.getPart();
                partList.add(part);
            }
            return partList;
        }
        return null;
    }

    @Override
    // 查找所有尺寸
    public List<String> selectAllSizeById(Integer suitId) {
        List<PartSizeDTO> partSizeDtos = partSizeMapper.selectAllBySuitId(suitId);
        List<String> sizeList = new ArrayList<>();
        if (partSizeDtos != null && !partSizeDtos.isEmpty()) {
            for (PartSizeDTO partSize : partSizeDtos) {
                String size = partSize.getSize();
                sizeList.add(size);
            }
            return sizeList;
        }
        return null;
    }

    @Override
    // 根据部件尺寸查找显示库存
    public Integer selectInventoryByPartSize(Integer suitId, String part, String size) {
        List<PartSizeDTO> partSizeDtos = partSizeMapper.selectAllBySuitId(suitId);
        for (PartSizeDTO partSize : partSizeDtos) {
            if (partSize.getPart().equals(part)) {
                if (partSize.getSize().equals(size)) {
                    return partSize.getInventory();
                }
            }
        }
        return null;
    }

    @Override
    // 根据部件尺寸返回价格
    public Double selectPriceByPartSize(Integer suitId, String part, String size) {
        List<PartSizeDTO> partSizeDtos = partSizeMapper.selectAllBySuitId(suitId);
        for (PartSizeDTO partSize : partSizeDtos) {
            if (partSize.getPart().equals(part)) {
                if (partSize.getSize().equals(size)) {
                    return partSize.getPrice();
                }
            }
        }
        return null;
    }

    @Override
    // 查找最低价
    public Double selectLowPriceBySuitId(Integer suitId) {
        List<PartSizeDTO> partSizeDtos = partSizeMapper.selectAllBySuitId(suitId);
        Double lowPrice = partSizeDtos.get(0).getPrice();
        for (int i = 0; i < partSizeDtos.size(); i++) {
            if (lowPrice > partSizeDtos.get(i).getPrice()) {
                lowPrice = partSizeDtos.get(i).getPrice();
            }
        }
        return lowPrice;
    }

    @Override
    // 查找最高价
    public Double selecgHighPriceBySuitId(Integer suitId) {
        List<PartSizeDTO> partSizeDtos = partSizeMapper.selectAllBySuitId(suitId);
        Double highPrice = partSizeDtos.get(0).getPrice();
        for (int i = 0; i < partSizeDtos.size(); i++) {
            if (highPrice < partSizeDtos.get(i).getPrice()) {
                highPrice = partSizeDtos.get(i).getPrice();
            }
        }
        return highPrice;
    }
}
