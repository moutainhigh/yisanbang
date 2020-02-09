package com.vtmer.yisanbang.controller;

import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.domain.Income;
import com.vtmer.yisanbang.service.IncomeService;
import com.vtmer.yisanbang.vo.IncomeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("收益信息接口")
@RestController
@RequestMapping("/income")
public class IncomeController {

    @Autowired
    private IncomeService incomeService;
    /**
     * 获取所有的收益记录
     * @return
     */
    @ApiOperation(value = "获取所有(每天)的收益记录",notes = "其中包括总收益和总销售量")
    @GetMapping("/getAll")
    public ResponseMessage<IncomeVo> getAll() {
        IncomeVo incomeVo = incomeService.getAll();
        if (incomeVo!=null) {
            return ResponseMessage.newSuccessInstance(incomeVo,"获取收益情况成功");
        } else {
            return ResponseMessage.newSuccessInstance("目前暂无收益信息");
        }
    }

    @ApiOperation(value = "获取某日的收益信息",notes = "传某个日期的时间戳")
    @GetMapping("/getByTime/{timestamp}")
    public ResponseMessage<Income> getByTime(@ApiParam(value = "时间戳",name = "timestamp",example = "timestamp")
                                         @PathVariable Long timestamp) {
        Income income = incomeService.getByTime(timestamp);
        if (income != null) {
            return ResponseMessage.newSuccessInstance(income,"获取收益信息成功");
        } else {
            return ResponseMessage.newErrorInstance("该日期无收益信息或获取失败");
        }
    }
}
