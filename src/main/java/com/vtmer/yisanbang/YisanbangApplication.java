package com.vtmer.yisanbang;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.vtmer.yisanbang.mapper")
public class YisanbangApplication {

    public static void main(String[] args) {
        SpringApplication.run(YisanbangApplication.class, args);
    }

}
