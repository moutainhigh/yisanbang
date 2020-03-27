package com.vtmer.yisanbang.domain;

import lombok.Data;

import java.util.Date;
@Data
public class CartGoods {
    private Integer id;

    private Integer cartId;

    private Integer colorSizeId;

    private Integer amount;

    private Boolean whetherGoods;

    private Boolean whetherChosen;

    private Date createTime;

    private Date updateTime;

}