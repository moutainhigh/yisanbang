package com.vtmer.yisanbang;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.vtmer.yisanbang.mapper")
@EnableTransactionManagement
@EnableScheduling
@EnableKnife4j
@EnableConfigurationProperties
public class YisanbangApplication {

    public static void main(String[] args) {
        SpringApplication.run(YisanbangApplication.class, args);
    }

}
