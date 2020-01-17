package com.vtmer.yisanbang.domain;

import java.util.Date;

public class Sort {
    private Integer id;

    private String name;

    private Boolean isSuit;

    private Integer firstSortid;

    private Integer secondSortid;

    private Date createTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Boolean getIsSuit() {
        return isSuit;
    }

    public void setIsSuit(Boolean isSuit) {
        this.isSuit = isSuit;
    }

    public Integer getFirstSortid() {
        return firstSortid;
    }

    public void setFirstSortid(Integer firstSortid) {
        this.firstSortid = firstSortid;
    }

    public Integer getSecondSortid() {
        return secondSortid;
    }

    public void setSecondSortid(Integer secondSortid) {
        this.secondSortid = secondSortid;
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