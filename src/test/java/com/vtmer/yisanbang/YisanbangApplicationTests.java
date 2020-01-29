package com.vtmer.yisanbang;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class YisanbangApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void createUuid() {
        UUID uuid = UUID.randomUUID();
        System.out.println(uuid);
    }

}
