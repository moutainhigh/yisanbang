package com.vtmer.yisanbang.common.quarz;

import com.vtmer.yisanbang.service.OrderService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@DisallowConcurrentExecution
public class OrderAutoCancelJob implements Job {

    @Autowired
    OrderService orderService;

    // 订单有效时间 30分钟
    private static final long EFFECTIVE_TIME = 30 * 60 * 1000;

    @Transactional
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        orderService.orderTimeOutLogic();
    }
}
