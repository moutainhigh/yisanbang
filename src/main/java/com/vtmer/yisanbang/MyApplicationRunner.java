package com.vtmer.yisanbang;

import com.vtmer.yisanbang.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;


public class MyApplicationRunner implements ApplicationRunner{

    @Autowired
    private OrderService orderService;

    /**
     * springboot一启动就执行订单超时自动取消任务
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        orderService.orderTimeOutLogic();
    }
}
