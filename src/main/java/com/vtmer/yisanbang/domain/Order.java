package com.vtmer.yisanbang.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode
@Data
public class Order {

    private Integer id;

    private String orderNumber;

    private Double totalPrice;

    private String addressName;

    private Integer status;

    private String courierNumber;

    private String message;

    private Date createTime;

    private Date updateTime;

    private Integer userId;

    private String userName;

    private String phoneNumber;

    private double postage;

    private Boolean whetherDelete;

    private Boolean whetherRemind;


}