package com.vtmer.yisanbang.common;

import java.util.List;

public class GoodsPageResponseMessage<T> {

    private Integer pageNum;
    private Integer pageSize;
    private Integer totalPage;
    private Integer total;
    private List<T> list;

    public static <T> GoodsPageResponseMessage<T> restPage(Integer pageNum, Integer pageSize, List<T> list) {
        GoodsPageResponseMessage<T> result = new GoodsPageResponseMessage<>();
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        result.setTotal(list.size());
        result.setTotalPage(list.size() % pageSize);
        result.setList(list);
        return result;
    }
    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
