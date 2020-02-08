package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.domain.Income;
import com.vtmer.yisanbang.vo.IncomeVo;

import java.util.List;

public interface IncomeService {

    int insert(Income income);

    List<Income> selectAll();

    IncomeVo getAll();

    Income getByTime(Long timestamp);
}
