package com.vtmer.yisanbang.domain;


import lombok.Data;

import java.util.Date;

@Data
public class BusinessAddress {

    private Integer id;

    private String addressName;

    private String phoneNumber;

    private String contactName;

    private Boolean whetherDefault;

    private Date createTime;

    private Date updateTime;

}