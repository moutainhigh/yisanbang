package com.vtmer.yisanbang.service.impl;

import com.vtmer.yisanbang.common.util.Comparator.ComparatorGoodsSuit;
import com.vtmer.yisanbang.common.util.Comparator.ComparatorGoodsSuitByPrice;
import com.vtmer.yisanbang.common.util.Comparator.ComparatorGoodsSuitByTime;
import com.vtmer.yisanbang.dto.GoodsDTO;
import com.vtmer.yisanbang.dto.SuitDTO;
import com.vtmer.yisanbang.mapper.GoodsMapper;
import com.vtmer.yisanbang.mapper.SuitMapper;
import com.vtmer.yisanbang.service.GoodsAndSuitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Service
public class GoodsAndSuitServiceImpl implements GoodsAndSuitService {
    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private SuitMapper suitMapper;

    @Override
    public List selectGoodsAndSuit(List<GoodsDTO> goodsDTOList, List<SuitDTO> suitDTOList) {
        List objectList = new ArrayList<>();
        Iterator iterator1 = goodsDTOList.iterator();
        Iterator iterator2 = suitDTOList.iterator();
        while (iterator1.hasNext()) {
            objectList.add(iterator1.next());
        }
        while (iterator2.hasNext()) {
            objectList.add(iterator2.next());
        }
        ComparatorGoodsSuit comparatorGoodsSuit = new ComparatorGoodsSuit();
        Collections.sort(objectList, comparatorGoodsSuit);
        return objectList;
    }

    @Override
    public List selectGoodsAndSuitByPriceAsc(List<GoodsDTO> goodsDTOList, List<SuitDTO> suitDTOList) {
        List objectList = new ArrayList<>();
        Iterator iterator1 = goodsDTOList.iterator();
        Iterator iterator2 = suitDTOList.iterator();
        while (iterator1.hasNext()) {
            objectList.add(iterator1.next());
        }
        while (iterator2.hasNext()) {
            objectList.add(iterator2.next());
        }
        ComparatorGoodsSuitByPrice comparatorGoodsSuit = new ComparatorGoodsSuitByPrice();
        Collections.sort(objectList, comparatorGoodsSuit);
        return objectList;
    }

    @Override
    public List selectGoodsAndSuitByPriceDec(List<GoodsDTO> goodsDTOList, List<SuitDTO> suitDTOList) {
        List objectList = new ArrayList<>();
        Iterator iterator1 = goodsDTOList.iterator();
        Iterator iterator2 = suitDTOList.iterator();
        while (iterator1.hasNext()) {
            objectList.add(iterator1.next());
        }
        while (iterator2.hasNext()) {
            objectList.add(iterator2.next());
        }
        ComparatorGoodsSuitByPrice comparatorGoodsSuit = new ComparatorGoodsSuitByPrice();
        Collections.sort(objectList, comparatorGoodsSuit);
        Collections.reverse(objectList);
        return objectList;
    }

    @Override
    public List selectGoodsAndSuitByTimeAsc(List<GoodsDTO> goodsDTOList, List<SuitDTO> suitDTOList) {
        List objectList = new ArrayList<>();
        Iterator iterator1 = goodsDTOList.iterator();
        Iterator iterator2 = suitDTOList.iterator();
        while (iterator1.hasNext()) {
            objectList.add(iterator1.next());
        }
        while (iterator2.hasNext()) {
            objectList.add(iterator2.next());
        }
        ComparatorGoodsSuitByTime comparatorGoodsSuit = new ComparatorGoodsSuitByTime();
        Collections.sort(objectList, comparatorGoodsSuit);
        return objectList;
    }

    @Override
    public List selectGoodsAndSuitByTimeDec(List<GoodsDTO> goodsDTOList, List<SuitDTO> suitDTOList) {
        List objectList = new ArrayList<>();
        Iterator iterator1 = goodsDTOList.iterator();
        Iterator iterator2 = suitDTOList.iterator();
        while (iterator1.hasNext()) {
            objectList.add(iterator1.next());
        }
        while (iterator2.hasNext()) {
            objectList.add(iterator2.next());
        }
        ComparatorGoodsSuitByTime comparatorGoodsSuit = new ComparatorGoodsSuitByTime();
        Collections.sort(objectList, comparatorGoodsSuit);
        Collections.reverse(objectList);
        return objectList;
    }
}
