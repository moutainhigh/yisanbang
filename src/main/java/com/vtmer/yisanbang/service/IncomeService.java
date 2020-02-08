package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.domain.Income;

import java.util.List;

public interface IncomeService {

    int insert(Income income);

    List<Income> selectAll();
}
