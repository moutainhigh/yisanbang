package com.vtmer.yisanbang.dto;

import com.vtmer.yisanbang.common.validGroup.Delete;
import com.vtmer.yisanbang.common.validGroup.Insert;
import com.vtmer.yisanbang.common.validGroup.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
@ApiModel(value = "suit对象", description = "套装对象suit")
public class SuitDto {
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

    private Date updateTime;

    @ApiModelProperty(hidden = true)
    private String name;

    @ApiModelProperty(hidden = true)
    private String picture;

    @ApiModelProperty(hidden = true)
    private double price;

    @ApiModelProperty(hidden = true)
    private String part;

    @ApiModelProperty(hidden = true)
    private String size;

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

    @Override
    public String toString() {
        return "SuitDto{" +
                "id=" + id +
                ", sortId=" + sortId +
                ", introduce='" + introduce + '\'' +
                ", lowestPrice=" + lowestPrice +
                ", highestPrice=" + highestPrice +
                ", isShow=" + isShow +
                '}';
    }

    public SuitDto(Integer id, Integer sortId, String introduce, Double lowestPrice, Double highestPrice, Boolean isShow, Date updateTime) {
        this.id = id;
        this.sortId = sortId;
        this.introduce = introduce;
        this.lowestPrice = lowestPrice;
        this.highestPrice = highestPrice;
        this.isShow = isShow;
        this.updateTime = updateTime;
    }

    public SuitDto() {
    }
}
