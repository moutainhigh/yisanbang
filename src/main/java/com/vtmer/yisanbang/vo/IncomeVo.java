package com.vtmer.yisanbang.vo;

import com.vtmer.yisanbang.domain.Income;
import lombok.Data;

import java.util.List;

@Data
public class IncomeVo {

    private double totalPrice;

    private Integer totalAmount;

    private List<Income> incomeList;
}
