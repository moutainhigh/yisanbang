package com.vtmer.yisanbang.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.vtmer.yisanbang.domain.User;
import com.vtmer.yisanbang.vo.WxAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {


    private final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    /**
     * 设置token过期时间：30分钟
     */
    private static final long EXPIRE_TIME = 30 * 60;
    /**
     * token私钥
     */
    private static final String TOKEN_SECRET = "c0a3e1c5de644ebda158414a10627212";

    /**
     * token名
     */
    public static final String TOKEN_HEADER = "Authorization";

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 根据微信用户登录信息创建token
     * 此处的token会被缓存到redis中用作二次验证
     *
     * @param wxAccount
     * @return
     */
    public String createTokenByUser(WxAccount wxAccount) {
        String jwtId = UUID.randomUUID().toString();
        // 设置过期时间
        Date expireTime = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        // 加密算法进行签名得到token
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        // 附带用户信息，生成签名
        String token = JWT.create()
                .withClaim("userId",wxAccount.getUserId())
                .withClaim("openId", wxAccount.getOpenId())
                .withClaim("sessionKey", wxAccount.getSessionKey())
                .withClaim("jwt-id", jwtId)
                .withExpiresAt(expireTime)
                .sign(algorithm);
        // redis缓存JWT,并设置过期时间
        redisTemplate.opsForValue().set("JWT-SESSION-" + jwtId, token, EXPIRE_TIME, TimeUnit.SECONDS);
        return token;
    }

    public boolean verifyToken(String token) {
        try {
            // 根据token解密，解密出jwt-id，先从redis中查出redisToken，判断是否相同
            String redisToken = (String) redisTemplate.opsForValue().get("JWT-SESSION-" + getJwtIdByToken(token));
            if (redisToken!=null) {
                if (!redisToken.equals(token)) {
                    logger.info("redis中的token和用户提交的token不一致");
                    return false;
                }
            } else {
                // 查询不到token，说明token过期或者是token有误
                logger.info("redis中查询不到token");
                return false;
            }
            // 效验token
            acceptExpiresAt(redisToken);
            return true;
        // 捕捉到任何异常均视为校验失败
        } catch (Exception e) {
            logger.info("校验过程中捕获到异常");
            return false;
        }
    }

    /**
     * JWT、token续期
     */
    public void acceptExpiresAt(String token) {
        // 获取算法相同的JWTVerifier
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        JWTVerifier verifier = JWT.require(algorithm)
                .withClaim("userId",getUserIdByToken(token))
                .withClaim("openId", getOpenIdByToken(token))
                .withClaim("sessionKey", getSessionKeyByToken(token))
                .withClaim("jwt-id", getJwtIdByToken(token))
                // JWT续期
                .acceptExpiresAt(System.currentTimeMillis() + EXPIRE_TIME)
                .build();
        // 验证token
        verifier.verify(token);
        // 续期redis中缓存的JWT
        redisTemplate.opsForValue().set("JWT-SESSION-" + getJwtIdByToken(token), token, EXPIRE_TIME, TimeUnit.SECONDS);
    }
    /**
     * 根据token获取jwt-id
     */
    public String getJwtIdByToken(String token) throws JWTDecodeException {
        return JWT.decode(token).getClaim("jwt-id").asString();
    }

    /**
     * 根据token获取userId
     */
    public Integer getUserIdByToken(String token) throws JWTDecodeException {
        return JWT.decode(token).getClaim("userId").asInt();
    }

    /**
     * 根据token获取openId
     */
    public String getOpenIdByToken(String token) throws JWTDecodeException {
        return JWT.decode(token).getClaim("openId").asString();
    }

    /**
     * 根据token获取sessionKey
     */
    public String getSessionKeyByToken(String token) throws JWTDecodeException {
        return JWT.decode(token).getClaim("sessionKey").asString();
    }

    public User getUserInfoByToken(String token) throws JWTDecodeException {
        User user = new User();
        user.setId(getUserIdByToken(token));
        user.setOpenId(getOpenIdByToken(token));
        return user;
    }
}
