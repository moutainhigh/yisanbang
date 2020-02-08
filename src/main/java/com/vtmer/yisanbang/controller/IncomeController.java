package com.vtmer.yisanbang.controller;

import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.domain.Income;
import com.vtmer.yisanbang.service.IncomeService;
import com.vtmer.yisanbang.vo.IncomeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/income")
public class IncomeController {

    @Autowired
    private IncomeService incomeService;
    /**
     * 获取所有的收益记录
     * @return
     */
    @GetMapping("/getAll")
    public ResponseMessage getAll() {
        IncomeVo incomeVo = incomeService.getAll();
        if (incomeVo!=null) {
            return ResponseMessage.newSuccessInstance(incomeVo,"获取收益情况成功");
        } else {
            return ResponseMessage.newSuccessInstance("目前暂无收益信息");
        }
    }

    @GetMapping("/getByTime/{timestamp}")
    public ResponseMessage getByTime(@PathVariable("timestamp") Long timestamp) {
        Income income = incomeService.getByTime(timestamp);
        if (income != null) {
            return ResponseMessage.newSuccessInstance(income,"获取收益信息成功");
        } else {
            return ResponseMessage.newErrorInstance("该日期无收益信息或获取失败");
        }
    }
}
