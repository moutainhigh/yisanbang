package com.vtmer.yisanbang.common.quarz;

import com.vtmer.yisanbang.service.CartService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class CartDataPersistenceJob implements Job {

    @Autowired
    private CartService cartService;

    private static final Logger logger = LoggerFactory.getLogger(CartDataPersistenceJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("开始持久化redis购物车数据到mysql");
        cartService.cartDataPersistence();
    }
}
