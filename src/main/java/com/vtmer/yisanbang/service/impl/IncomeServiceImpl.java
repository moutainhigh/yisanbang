package com.vtmer.yisanbang.service.impl;

import com.vtmer.yisanbang.domain.Income;
import com.vtmer.yisanbang.mapper.IncomeMapper;
import com.vtmer.yisanbang.service.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncomeServiceImpl implements IncomeService {

    @Autowired
    private IncomeMapper incomeMapper;

    @Override
    public int insert(Income income) {
        return incomeMapper.insert(income);
    }

    @Override
    public List<Income> selectAll() {
        return incomeMapper.selectAll();
    }
}
