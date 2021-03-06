package com.vtmer.yisanbang.common.quarz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitializationJobManage {

    @Autowired
    private QuartzManager quartzManager;

    @Bean
    public void Initialization(){
        // 每25分钟执行一次
        quartzManager.addJob("未支付订单半小时定时删除",OrderAutoCancelJob.class,25);
        // 每天凌晨三点执行一次 0,10 * * * * ? *    0 0 3 1/1 * ? *
        quartzManager.addJob("每天计算收益情况",CalculateEarningsJob.class," 0 0 3 1/1 * ? *");
        // 每天凌晨四点执行一次
        quartzManager.addJob("每天持久化redis购物车数据",CartDataPersistenceJob.class," 0 0 4 1/1 * ? *");
    }
}
