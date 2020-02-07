package com.vtmer.yisanbang.dto;

public class SuitDetailDto {
    private Integer id;

    private Integer suitId;

    private String pirtucePath;

    private Integer showOrder;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSuitId() {
        return suitId;
    }

    public void setSuitId(Integer suitId) {
        this.suitId = suitId;
    }

    public String getPirtucePath() {
        return pirtucePath;
    }

    public void setPirtucePath(String pirtucePath) {
        this.pirtucePath = pirtucePath;
    }

    public Integer getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }

    @Override
    public String toString() {
        return "SuitDetailDto{" +
                "id=" + id +
                ", suitId=" + suitId +
                ", pirtucePath='" + pirtucePath + '\'' +
                ", showOrder=" + showOrder +
                '}';
    }

    public SuitDetailDto(Integer id, Integer suitId, String pirtucePath, Integer showOrder) {
        this.id = id;
        this.suitId = suitId;
        this.pirtucePath = pirtucePath;
        this.showOrder = showOrder;
    }
}
