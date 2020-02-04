package com.vtmer.yisanbang.dto;

public class SortDto {

    private Integer id;

    private String name;

    private Boolean isSuit;

    private Integer parentId;

    private Integer level;

    private Integer showOrder;

    private Boolean isShow;

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
        this.name = name;
    }

    public Boolean getIsSuit() {
        return isSuit;
    }

    public void setIsSuit(Boolean suit) {
        isSuit = suit;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }

    public Boolean getIsShow() {
        return isShow;
    }

    public void setIsShow(Boolean show) {
        isShow = show;
    }

}
