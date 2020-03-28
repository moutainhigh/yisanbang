package com.vtmer.yisanbang.dto;

import com.vtmer.yisanbang.common.valid.group.Delete;
import com.vtmer.yisanbang.common.valid.group.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
@ApiModel(value = "suit对象", description = "套装对象suit")
public class SuitDTO {
    @NotNull(groups = {Update.class, Delete.class}, message = "套装id不可为空")
    @ApiModelProperty(value = "套装id", example = "1")
    private Integer id;

    @NotNull(message = "分类id不可为空")
    @ApiModelProperty(value = "分类id", example = "2")
    private Integer sortId;

    @NotBlank(message = "套装简介不可为空")
    @ApiModelProperty(value = "套装简介", example = "服务承诺 正品保证 极速退款 七天无理由退换")
    private String introduce;

    @NotNull(message = "最低价不可为空")
    @ApiModelProperty(value = "该套装内部件的最低价", example = "699")
    private Double lowestPrice;

    @NotNull(message = "最高价不可为空")
    @ApiModelProperty(value = "该套装内部件的最高价", example = "999")
    private Double highestPrice;

    @NotNull(message = "显示标志不可为空")
    @ApiModelProperty(value = "是否显示", example = "true")
    private Boolean isShow;

    @NotNull(message = "删除标志不可为空")
    @ApiModelProperty(value = "删除标志", example = "false")
    private Boolean whetherDelete;

    @NotNull(message = "套装名称不可为空")
    @ApiModelProperty(value = "套装名称", example = "学生套装")
    private String name;

    @NotNull(message = "图片地址不可为空")
    @ApiModelProperty(value = "套装图片", example = "suit/****")
    private String picture;

    @ApiModelProperty(value = "发货地址", example = "广东东莞")
    private String address;

    private Date updateTime;

    @ApiModelProperty(hidden = true)
    private double price;

    @ApiModelProperty(hidden = true)
    private String part;

    @ApiModelProperty(hidden = true)
    private String size;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSortId() {
        return sortId;
    }

    public void setSortId(Integer sortId) {
        this.sortId = sortId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce == null ? null : introduce.trim();
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture == null ? null : picture.trim();
    }

    public Double getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(Double lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public Double getHighestPrice() {
        return highestPrice;
    }

    public void setHighestPrice(Double highestPrice) {
        this.highestPrice = highestPrice;
    }

    public Boolean getIsShow() {
        return isShow;
    }

    public void setIsShow(Boolean isShow) {
        this.isShow = isShow;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean getWhetherDelete() {
        return whetherDelete;
    }

    public void setWhetherDelete(Boolean whetherDelete) {
        this.whetherDelete = whetherDelete;
    }

    @Override
    public String toString() {
        return "SuitDTO{" +
                "id=" + id +
                ", sortId=" + sortId +
                ", introduce='" + introduce + '\'' +
                ", lowestPrice=" + lowestPrice +
                ", highestPrice=" + highestPrice +
                ", isShow=" + isShow +
                ", name='" + name + '\'' +
                ", picture='" + picture + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }

    public SuitDTO(@NotNull(groups = {Update.class, Delete.class}, message = "套装id不可为空") Integer id, @NotNull(message = "分类id不可为空") Integer sortId, @NotBlank(message = "套装简介不可为空") String introduce, @NotNull(message = "最低价不可为空") Double lowestPrice, @NotNull(message = "最高价不可为空") Double highestPrice, @NotNull(message = "显示标志不可为空") Boolean isShow, @NotNull(message = "套装名称不可为空") String name, @NotNull(message = "图片地址不可为空") String picture, Date updateTime) {
        this.id = id;
        this.sortId = sortId;
        this.introduce = introduce;
        this.lowestPrice = lowestPrice;
        this.highestPrice = highestPrice;
        this.isShow = isShow;
        this.name = name;
        this.picture = picture;
        this.updateTime = updateTime;
    }

    public SuitDTO(Integer id, Integer sortId, String introduce, Double lowestPrice, Double highestPrice, Boolean isShow, Date updateTime, String address) {
        this.id = id;
        this.sortId = sortId;
        this.introduce = introduce;
        this.lowestPrice = lowestPrice;
        this.highestPrice = highestPrice;
        this.isShow = isShow;
        this.updateTime = updateTime;
        this.address = address;
    }

    public SuitDTO() {
    }
}
