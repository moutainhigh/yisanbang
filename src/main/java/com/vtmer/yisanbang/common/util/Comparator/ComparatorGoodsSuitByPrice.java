package com.vtmer.yisanbang.common.util.Comparator;

import com.vtmer.yisanbang.dto.GoodsDTO;
import com.vtmer.yisanbang.dto.SuitDTO;

import java.util.Comparator;

public class ComparatorGoodsSuitByPrice implements Comparator {
    @Override
    public int compare(Object arg0, Object arg1) {
        GoodsDTO goodsDTO,goodsDTO1;
        SuitDTO suitDTO,suitDTO1;
        if (arg0 instanceof GoodsDTO) {
            goodsDTO = (GoodsDTO) arg0;
            if (arg1 instanceof SuitDTO) {
                suitDTO = (SuitDTO) arg1;
                return goodsDTO.getPrice().compareTo(suitDTO.getLowestPrice());
            } else {
                goodsDTO1 = (GoodsDTO) arg1;
                return goodsDTO.getPrice().compareTo(goodsDTO1.getPrice());
            }
        } else {
            suitDTO1 = (SuitDTO) arg0;
            if (arg1 instanceof SuitDTO) {
                suitDTO = (SuitDTO) arg1;
                return suitDTO1.getLowestPrice().compareTo(suitDTO.getLowestPrice());
            } else {
                goodsDTO = (GoodsDTO) arg1;
                return suitDTO1.getLowestPrice().compareTo(goodsDTO.getPrice());
            }
        }
    }
}
