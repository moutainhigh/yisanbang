package com.vtmer.yisanbang.service.impl;

import com.vtmer.yisanbang.domain.Income;
import com.vtmer.yisanbang.mapper.IncomeMapper;
import com.vtmer.yisanbang.service.IncomeService;
import com.vtmer.yisanbang.vo.IncomeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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

    @Override
    public IncomeVo getAll() {
        List<Income> incomeList = incomeMapper.selectAll();
        if (incomeList == null) {
            return null;
        } else {
            double totalPrice = 0;
            Integer totalAmount = 0;
            IncomeVo incomeVo = new IncomeVo();
            for (Income income : incomeList) {
                totalPrice += income.getTotalPrice();
                totalAmount += income.getTotalAmount();
            }
            incomeVo.setTotalAmount(totalAmount);
            incomeVo.setTotalPrice(totalPrice);
            incomeVo.setIncomeList(incomeList);
            return incomeVo;
        }
    }

    @Override
    public Income getByTime(Date date) {
        return incomeMapper.getByTime(date);
    }
}
