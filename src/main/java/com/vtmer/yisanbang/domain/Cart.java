package com.vtmer.yisanbang.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Cart {
    private Integer id;

    private Integer userId;

    private Double totalPrice;

    private Date createTime;

    private Date updateTime;

}