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
        oneCycle:
        for (int i = 0; i <= goodsDTOList.size(); i++) {
            for (int j = 0; j < suitDTOList.size(); j++) {
                if (goodsDTOList.get(i).getId() < suitDTOList.get(j).getId()) {
                    objectList.add(goodsDTOList.get(i));
                    break oneCycle;
                } else {
                    objectList.add(suitDTOList.get(j));
                }
            }
        }
        return objectList;
    }

    @Override
    public List<Object> selectGoodsAndSuitByPriceAsc(List<GoodsDTO> goodsDTOList, List<SuitDTO> suitDTOList) {
        List<Object> objectList = new ArrayList<>();
        oneCycle:
        for (int i = 0; i <= goodsDTOList.size(); i++) {
            for (int j = 0; j < suitDTOList.size(); j++) {
                if (goodsDTOList.get(i).getPrice() < suitDTOList.get(j).getLowestPrice()) {
                    objectList.add(goodsDTOList.get(i));
                    break oneCycle;
                } else {
                    objectList.add(suitDTOList.get(j));
                }
            }
        }
        return objectList;
    }

    @Override
    public List<Object> selectGoodsAndSuitByPriceDec(List<GoodsDTO> goodsDTOList, List<SuitDTO> suitDTOList) {
        List<Object> objectList = new ArrayList<>();
        oneCycle:
        for (int i = 0; i <= goodsDTOList.size(); i++) {
            for (int j = 0; j < suitDTOList.size(); j++) {
                if (goodsDTOList.get(i).getPrice() > suitDTOList.get(j).getLowestPrice()) {
                    objectList.add(goodsDTOList.get(i));
                    break oneCycle;
                } else {
                    objectList.add(suitDTOList.get(j));
                }
            }
        }
        return objectList;
    }

    @Override
    public List<Object> selectGoodsAndSuitByTimeAsc(List<GoodsDTO> goodsDTOList, List<SuitDTO> suitDTOList) {
        List<Object> objectList = new ArrayList<>();
        oneCycle:
        for (int i = 0; i <= goodsDTOList.size(); i++) {
            for (int j = 0; j < suitDTOList.size(); j++) {
                if (goodsDTOList.get(i).getUpdateTime().before(suitDTOList.get(j).getUpdateTime())) {
                    objectList.add(goodsDTOList.get(i));
                    break oneCycle;
                } else {
                    objectList.add(suitDTOList.get(j));
                }
            }
        }
        return objectList;
    }

    @Override
    public List<Object> selectGoodsAndSuitByTimeDec(List<GoodsDTO> goodsDTOList, List<SuitDTO> suitDTOList) {
        List<Object> objectList = new ArrayList<>();
        oneCycle:
        for (int i = 0; i <= goodsDTOList.size(); i++) {
            for (int j = 0; j < suitDTOList.size(); j++) {
                if (goodsDTOList.get(i).getUpdateTime().after(suitDTOList.get(j).getUpdateTime())) {
                    objectList.add(goodsDTOList.get(i));
                    break oneCycle;
                } else {
                    objectList.add(suitDTOList.get(j));
                }
            }
        }
        return objectList;
    }
}
