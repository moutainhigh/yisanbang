package com.vtmer.yisanbang.controller;

import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.common.annotation.RequestLog;
import com.vtmer.yisanbang.domain.Income;
import com.vtmer.yisanbang.service.IncomeService;
import com.vtmer.yisanbang.vo.IncomeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Past;
import java.util.Date;

@Api(tags = "收益信息接口",value = "后台管理部分")
@RestController
@RequestMapping("/income")
public class IncomeController {

    @Autowired
    private IncomeService incomeService;

    /**
     * 获取所有的收益记录
     * @return
     */
    @RequestLog(module = "收益信息", operationDesc = "获取所有(每天)的收益记录")
    @ApiOperation(value = "获取所有(每天)的收益记录", notes = "其中包括总收益和总销售量")
    @GetMapping("/getAll")
    public ResponseMessage<IncomeVO> getAll() {
        IncomeVO incomeVo = incomeService.getAll();
        if (incomeVo != null) {
            return ResponseMessage.newSuccessInstance(incomeVo, "获取收益情况成功");
        } else {
            return ResponseMessage.newSuccessInstance("目前暂无收益信息");
        }
    }

    @RequestLog(module = "收益信息", operationDesc = "获取某日的收益信息")
    @ApiOperation(value = "获取某日的收益信息", notes = "传某个日期的时间戳")
    @GetMapping("/getByTime/{date}")
    public ResponseMessage<Income> getByTime(@ApiParam(value = "过去的一个日期", name = "date", example = "2020-2-14", required = true)
                                             @Past(message = "需要一个过去的日期") @DateTimeFormat(pattern = "yyyy-mm-dd")
                                             @PathVariable Date date) {
        System.out.println(date);
        Income income = incomeService.getByTime(date);
        if (income != null) {
            return ResponseMessage.newSuccessInstance(income, "获取收益信息成功");
        } else {
            return ResponseMessage.newSuccessInstance("该日期无收益信息");
        }
    }
}
