package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.domain.Income;
import com.vtmer.yisanbang.vo.IncomeVO;

import java.util.Date;
import java.util.List;

public interface IncomeService {

    int insert(Income income);

    List<Income> selectAll();

    IncomeVO getAll();

    Income getByTime(Date date);
}
