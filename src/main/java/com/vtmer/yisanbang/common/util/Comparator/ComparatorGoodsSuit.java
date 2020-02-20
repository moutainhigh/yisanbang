package com.vtmer.yisanbang.common.util.Comparator;

import com.vtmer.yisanbang.dto.GoodsDTO;
import com.vtmer.yisanbang.dto.SuitDTO;

import java.util.Comparator;

public class ComparatorGoodsSuit implements Comparator {
    @Override
    public int compare(Object arg0, Object arg1) {
        GoodsDTO goodsDTO,goodsDTO1;
        SuitDTO suitDTO,suitDTO1;
        if (arg0 instanceof GoodsDTO) {
            goodsDTO = (GoodsDTO) arg0;
            if (arg1 instanceof SuitDTO) {
                suitDTO = (SuitDTO) arg1;
                return goodsDTO.getId().compareTo(suitDTO.getId());
            } else {
                goodsDTO1 = (GoodsDTO) arg1;
                return goodsDTO.getId().compareTo(goodsDTO1.getId());
            }
        } else {
            suitDTO1 = (SuitDTO) arg0;
            if (arg1 instanceof SuitDTO) {
                suitDTO = (SuitDTO) arg1;
                return suitDTO1.getId().compareTo(suitDTO.getId());
            } else {
                goodsDTO = (GoodsDTO) arg1;
                return suitDTO1.getId().compareTo(goodsDTO.getId());
            }
        }
    }
}
