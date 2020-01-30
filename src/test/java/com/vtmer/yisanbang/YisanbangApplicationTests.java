package com.vtmer.yisanbang;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.UUID;

@SpringBootTest
class YisanbangApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${wx.applet.appid}")
    private String addId;

    @Test
    void contextLoads() {
    }

    @Test
    void createUuid() {
        UUID uuid = UUID.randomUUID();
        System.out.println(uuid);
    }

    @Test
    void test1() {
        System.out.println(addId);
    }

    @Test
    void test2() {
        redisTemplate.opsForValue().set("123", "123456");
    }

}
