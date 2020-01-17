package com.vtmer.yisanbang.domain;

import java.util.Date;

public class SuitSecondSort {
    private Integer id;

    private Integer firstSortid;

    private Date createTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFirstSortid() {
        return firstSortid;
    }

    public void setFirstSortid(Integer firstSortid) {
        this.firstSortid = firstSortid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}