package com.vtmer.yisanbang.vo;

import com.vtmer.yisanbang.domain.Income;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class IncomeVo {

    @ApiModelProperty(readOnly = true,example = "7200",value = "总销售金额")
    private double totalPrice;

    @ApiModelProperty(readOnly = true,example = "200",value = "总销售量")
    private Integer totalAmount;

    @ApiModelProperty(value = "每日收益",readOnly = true)
    private List<Income> incomeList;

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<Income> getIncomeList() {
        return incomeList;
    }

    public void setIncomeList(List<Income> incomeList) {
        this.incomeList = incomeList;
    }
}
