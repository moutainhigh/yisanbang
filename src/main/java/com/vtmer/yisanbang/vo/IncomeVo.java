package com.vtmer.yisanbang.vo;

import com.vtmer.yisanbang.domain.Income;
import lombok.Data;

import java.util.List;

@Data
public class IncomeVo {

    private double totalPrice;

    private Integer totalAmount;

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
