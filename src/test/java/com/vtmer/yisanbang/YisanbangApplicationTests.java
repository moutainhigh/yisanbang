package com.vtmer.yisanbang;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.vtmer.yisanbang.common.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class YisanbangApplicationTests {

    /**
     * 设置token过期时间
     */
    private static final long EXPIRE_TIME = 15000 * 60 * 1000;
    /**
     * token私钥
     */
    private static final String TOKEN_SECRET = "c0a3e1c5de644ebda158414a10627212";

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private JwtUtil jwtUtil;

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
        Object redisToken = redisTemplate.opsForValue().get("JWT-SESSION-ffeb2ba0-c0bb-479f-a42d-e1719f23ee94");
        System.out.println(redisToken);
        Object test = redisTemplate.opsForValue().get("123");
        System.out.println(test);
    }

    @Test
    void test3() {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzZXNzaW9uS2V5IjoiRjMzanNSWHIzektieDhaRDgwc2Jadz09Iiwiand0LWlkIjoiZmZlYjJiYTAtYzBiYi00NzlmLWE0MmQtZTE3MTlmMjNlZTk0Iiwib3BlbklkIjoiby1MT1g1QXNzUU5MRk9hN3h0OHgyRDRFc01VUSIsImV4cCI6MTU4MDM5NDA4NX0.0IWImDElgxoDDZV___6Xm7d60CWcEwJOYELwl1292-4";
        String openId = jwtUtil.getOpenIdByToken(token);
        System.out.println(openId);
        String sessionKey = jwtUtil.getSessionKeyByToken(token);
        System.out.println(sessionKey);
        String jwtId = jwtUtil.getJwtIdByToken(token);
        System.out.println(jwtId);
    }

    @Test
    void test4() {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzZXNzaW9uS2V5IjoiRjMzanNSWHIzektieDhaRDgwc2Jadz09Iiwiand0LWlkIjoiZmZlYjJiYTAtYzBiYi00NzlmLWE0MmQtZTE3MTlmMjNlZTk0Iiwib3BlbklkIjoiby1MT1g1QXNzUU5MRk9hN3h0OHgyRDRFc01VUSIsImV4cCI6MTU4MDM5NDA4NX0.0IWImDElgxoDDZV___6Xm7d60CWcEwJOYELwl1292-4";
        // token = "123";
        boolean flag = jwtUtil.verifyToken(token);
        System.out.println(flag);
    }

    @Test
    void test5() {
        String jwtId = UUID.randomUUID().toString();
        // 设置过期时间
        Date expireTime = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        // 加密算法进行签名得到token
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        // 附带用户信息，生成签名
        String token = JWT.create()
                .withClaim("openId", "o-LOX5AssQNLFOa7xt8x2D4EsMUQ")
                .withClaim("sessionKey", "F33jsRXr3zKbx8ZD80sbZw==")
                .withClaim("jwt-id", jwtId)
                .withExpiresAt(expireTime)
                .sign(algorithm);
        // redis缓存JWT,并设置过期时间
        redisTemplate.opsForValue().set("JWT-SESSION-" + jwtId, token, EXPIRE_TIME, TimeUnit.SECONDS);
    }
}
