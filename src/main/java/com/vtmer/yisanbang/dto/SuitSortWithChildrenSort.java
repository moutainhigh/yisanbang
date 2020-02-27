package com.vtmer.yisanbang.dto;

import com.vtmer.yisanbang.domain.Sort;

import java.util.List;

public class SuitSortWithChildrenSort extends Sort {
    
    private List<Sort> children;

    public List<Sort> getChildren() {
        return children;
    }

    public void setChildren(List<Sort> children) {
        this.children = children;
    }
}
