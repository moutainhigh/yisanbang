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
    }

}
