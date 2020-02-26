package com.vtmer.yisanbang.common.util.comparator;

import com.vtmer.yisanbang.dto.GoodsDTO;
import com.vtmer.yisanbang.dto.SuitDTO;

import java.util.Comparator;

public class ComparatorGoodsSuitByTime implements Comparator {
    @Override
    public int compare(Object arg0, Object arg1) {
        GoodsDTO goodsDTO, goodsDTO1;
        SuitDTO suitDTO, suitDTO1;
        if (arg0 instanceof GoodsDTO) {
            goodsDTO = (GoodsDTO) arg0;
            if (arg1 instanceof SuitDTO) {
                suitDTO = (SuitDTO) arg1;
                return goodsDTO.getUpdateTime().compareTo(suitDTO.getUpdateTime());
            } else {
                goodsDTO1 = (GoodsDTO) arg1;
                return goodsDTO.getUpdateTime().compareTo(goodsDTO1.getUpdateTime());
            }
        } else {
            suitDTO1 = (SuitDTO) arg0;
            if (arg1 instanceof SuitDTO) {
                suitDTO = (SuitDTO) arg1;
                return suitDTO1.getUpdateTime().compareTo(suitDTO.getUpdateTime());
            } else {
                goodsDTO = (GoodsDTO) arg1;
                return suitDTO1.getUpdateTime().compareTo(goodsDTO.getUpdateTime());
            }
        }
    }
}
