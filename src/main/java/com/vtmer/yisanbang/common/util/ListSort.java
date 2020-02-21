package com.vtmer.yisanbang.common.util;

import com.vtmer.yisanbang.domain.Goods;
import com.vtmer.yisanbang.dto.CartGoodsDTO;
import com.vtmer.yisanbang.dto.GoodsDTO;
import com.vtmer.yisanbang.dto.SuitDTO;

import java.util.Comparator;
import java.util.List;

/*
    list排序方法
 */
public class ListSort {

    public static void listTimeSort(List<CartGoodsDTO> list) {
        list.sort(new Comparator<CartGoodsDTO>() {
            @Override
            public int compare(CartGoodsDTO o1, CartGoodsDTO o2) {
                try {
                    Long dt1 = o1.getUpdateTime();
                    Long dt2 = o2.getUpdateTime();
                    if (dt1 > dt2) {
                        return -1;
                    } else if (dt1 < dt2) {
                        return 1;
                    } else return 0;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
    }
}
