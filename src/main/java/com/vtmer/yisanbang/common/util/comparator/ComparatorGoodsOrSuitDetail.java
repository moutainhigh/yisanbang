package com.vtmer.yisanbang.common.util.comparator;

import com.vtmer.yisanbang.dto.GoodsDetailDTO;
import com.vtmer.yisanbang.dto.SuitDetailDTO;

import java.util.Comparator;

public class ComparatorGoodsOrSuitDetail implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        GoodsDetailDTO goodsDetailDTO,goodsDetailDTO1;
        SuitDetailDTO suitDetailDTO,suitDetailDTO1;
        if (o1 instanceof GoodsDetailDTO && o2 instanceof GoodsDetailDTO){
            goodsDetailDTO = (GoodsDetailDTO) o1;
            goodsDetailDTO1 = (GoodsDetailDTO) o2;
            return goodsDetailDTO.getShowOrder().compareTo(goodsDetailDTO1.getShowOrder());
        }else if (o1 instanceof SuitDetailDTO && o2 instanceof SuitDetailDTO){
            suitDetailDTO = (SuitDetailDTO) o1;
            suitDetailDTO1 = (SuitDetailDTO) o2;
            return suitDetailDTO.getShowOrder().compareTo(suitDetailDTO1.getShowOrder());
        }
        return 0;
    }
}
