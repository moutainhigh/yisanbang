package com.vtmer.yisanbang.dto;

import org.springframework.data.domain.Sort;

import java.util.List;

public class SuitSortWithChildrenSort extends Sort {
    
    private List<Sort> children;

    protected SuitSortWithChildrenSort(List<Order> orders) {
        super(orders);
    }

    public List<Sort> getChildren() {
        return children;
    }

    public void setChildren(List<Sort> children) {
        this.children = children;
    }
}
