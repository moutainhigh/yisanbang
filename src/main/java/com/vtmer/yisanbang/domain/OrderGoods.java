package com.vtmer.yisanbang.domain;

import lombok.Data;

import java.util.Date;

@Data
public class OrderGoods {
    private Integer id;

    private Integer orderId;

    private Integer sizeId;

    private Boolean whetherGoods;

    private Integer amount;

    private Double totalPrice;

    private Date createTime;

    private Date updateTime;

}