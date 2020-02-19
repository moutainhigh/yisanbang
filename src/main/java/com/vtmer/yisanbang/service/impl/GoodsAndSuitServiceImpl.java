package com.vtmer.yisanbang.service.impl;

import com.vtmer.yisanbang.dto.GoodsDTO;
import com.vtmer.yisanbang.dto.SuitDTO;
import com.vtmer.yisanbang.mapper.GoodsMapper;
import com.vtmer.yisanbang.mapper.SuitMapper;
import com.vtmer.yisanbang.service.GoodsAndSuitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GoodsAndSuitServiceImpl implements GoodsAndSuitService {
    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private SuitMapper suitMapper;

    @Override
    public List<Object> selectGoodsAndSuit(List<GoodsDTO> goodsDTOList, List<SuitDTO> suitDTOList) {
        List<Object> objectList = new ArrayList<>();
        if (goodsDTOList != null && !goodsDTOList.isEmpty() && suitDTOList != null && !suitDTOList.isEmpty()) {
            for (GoodsDTO goodsDTO : goodsDTOList) {
                for (SuitDTO suitDTO : suitDTOList) {
                    if (goodsDTO.getId() <= suitDTO.getId()){
                        objectList.add(goodsDTO);
                        goodsDTOList.remove(goodsDTO);
                    }else {
                        objectList.add(suitDTO);
                        suitDTOList.remove(suitDTO);
                    }
                }
            }
        }
        return objectList;
    }

    @Override
    public List<Object> selectGoodsAndSuitByPriceAsc(List<GoodsDTO> goodsDTOList, List<SuitDTO> suitDTOList) {
        List<Object> objectList = new ArrayList<>();
        if (goodsDTOList != null && !goodsDTOList.isEmpty() && suitDTOList != null && !suitDTOList.isEmpty()) {
            for (GoodsDTO goodsDTO : goodsDTOList) {
                for (SuitDTO suitDTO : suitDTOList) {
                    if (goodsDTO.getPrice() <= suitDTO.getLowestPrice()){
                        objectList.add(goodsDTO);
                        goodsDTOList.remove(goodsDTO);
                    }else {
                        objectList.add(suitDTO);
                        suitDTOList.remove(suitDTO);
                    }
                }
            }
        }
        return objectList;
    }

    @Override
    public List<Object> selectGoodsAndSuitByPriceDec(List<GoodsDTO> goodsDTOList, List<SuitDTO> suitDTOList) {
        List<Object> objectList = new ArrayList<>();
        if (goodsDTOList != null && !goodsDTOList.isEmpty() && suitDTOList != null && !suitDTOList.isEmpty()) {
            for (GoodsDTO goodsDTO : goodsDTOList) {
                for (SuitDTO suitDTO : suitDTOList) {
                    if (goodsDTO.getPrice() >= suitDTO.getLowestPrice()){
                        objectList.add(goodsDTO);
                        goodsDTOList.remove(goodsDTO);
                    }else {
                        objectList.add(suitDTO);
                        suitDTOList.remove(suitDTO);
                    }
                }
            }
        }
        return objectList;
    }

    @Override
    public List<Object> selectGoodsAndSuitByTimeAsc(List<GoodsDTO> goodsDTOList, List<SuitDTO> suitDTOList) {
        List<Object> objectList = new ArrayList<>();
        if (goodsDTOList != null && !goodsDTOList.isEmpty() && suitDTOList != null && !suitDTOList.isEmpty()) {
            for (GoodsDTO goodsDTO : goodsDTOList) {
                for (SuitDTO suitDTO : suitDTOList) {
                    if (suitDTO.getUpdateTime().before(goodsDTO.getUpdateTime())){
                        objectList.add(goodsDTO);
                        goodsDTOList.remove(goodsDTO);
                    }else {
                        objectList.add(suitDTO);
                        suitDTOList.remove(suitDTO);
                    }
                }
            }
        }
        return objectList;
    }

    @Override
    public List<Object> selectGoodsAndSuitByTimeDec(List<GoodsDTO> goodsDTOList, List<SuitDTO> suitDTOList) {
        List<Object> objectList = new ArrayList<>();
        if (goodsDTOList != null && !goodsDTOList.isEmpty() && suitDTOList != null && !suitDTOList.isEmpty()) {
            for (GoodsDTO goodsDTO : goodsDTOList) {
                for (SuitDTO suitDTO : suitDTOList) {
                    if (suitDTO.getUpdateTime().after(goodsDTO.getUpdateTime())){
                        objectList.add(goodsDTO);
                        goodsDTOList.remove(goodsDTO);
                    }else {
                        objectList.add(suitDTO);
                        suitDTOList.remove(suitDTO);
                    }
                }
            }
        }
        return objectList;
    }
}
