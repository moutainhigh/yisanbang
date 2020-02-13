package com.vtmer.yisanbang.common;

import com.vtmer.yisanbang.dto.CartGoodsDto;

import java.util.Comparator;
import java.util.List;

/*
    list排序方法
 */
public class ListSort {

    public static void listTimeSort(List<CartGoodsDto> list) {
        list.sort(new Comparator<CartGoodsDto>() {
            @Override
            public int compare(CartGoodsDto o1, CartGoodsDto o2) {
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
