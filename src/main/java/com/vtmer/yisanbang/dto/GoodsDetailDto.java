package com.vtmer.yisanbang.dto;

public class GoodsDetailDto {
    private Integer id;

    private Integer goodsId;

    private String pirtucePath;

    private Integer showOrder;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getPirtucePath() {
        return pirtucePath;
    }

    public void setPirtucePath(String pirtucePath) {
        this.pirtucePath = pirtucePath == null ? null : pirtucePath.trim();
    }

    public Integer getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }

    public GoodsDetailDto(Integer id, Integer goodsId, String pirtucePath, Integer showOrder) {
        this.id = id;
        this.goodsId = goodsId;
        this.pirtucePath = pirtucePath;
        this.showOrder = showOrder;
    }

    @Override
    public String toString() {
        return "GoodsDetailDto{" +
                "id=" + id +
                ", goodsId=" + goodsId +
                ", pirtucePath='" + pirtucePath + '\'' +
                ", showOrder=" + showOrder +
                '}';
    }
}
