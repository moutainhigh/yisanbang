package com.vtmer.yisanbang.domain;

import lombok.Data;

import java.util.Date;

@Data
public class RefundGoods {
    private Integer id;

    private Integer refundId;

    private Integer sizeId;

    private Boolean whetherGoods;

    private Date createTime;

    private Date updateTime;


}