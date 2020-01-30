package com.vtmer.yisanbang.common;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.vtmer.yisanbang.vo.WxAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {

    /**
     * 设置token过期时间：15分钟
     */
    private static final long EXPIRE_TIME = 15 * 60 * 1000;
    /**
     * token私钥
     */
    private static final String TOKEN_SECRET = "c0a3e1c5de644ebda158414a10627212";

    @Autowired
    private StringRedisTemplate redisTemplate;


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
            String redisToken = redisTemplate.opsForValue().get("JWT-SESSION-" + getJwtIdByToken(token));
            if (!redisToken.equals(token)) {
                return false;
            }
            // 获取算法相同的JWTVerifier
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("openId", getOpenIdByToken(redisToken))
                    .withClaim("sessionKey", getSessionKeyByToken(redisToken))
                    .withClaim("jwt-id", getJwtIdByToken(redisToken))
                    // JWT续期
                    .acceptExpiresAt(System.currentTimeMillis() + EXPIRE_TIME)
                    .build();
            // 验证token
            verifier.verify(redisToken);
            // 续期redis中缓存的JWT
            redisTemplate.opsForValue().set("JWT-SESSION-" + getJwtIdByToken(token), redisToken, EXPIRE_TIME, TimeUnit.SECONDS);
            return true;
        // 捕捉到任何异常均视为校验失败
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 根据token获取jwt-id
     */
    public String getJwtIdByToken(String token) throws JWTDecodeException {
        return JWT.decode(token).getClaim("jwt-id").asString();
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

}
