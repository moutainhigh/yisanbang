package com.vtmer.yisanbang.domain;

import lombok.Data;

import java.util.Date;


@Data
public class Collection {

    private Integer id;

    private Integer userId;

    private Integer goodsId;

    private Boolean whetherGoods;

    private Date createTime;

    private Date updateTime;
}