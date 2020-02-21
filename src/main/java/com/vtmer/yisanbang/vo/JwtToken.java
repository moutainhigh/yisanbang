package com.vtmer.yisanbang.vo;

import lombok.Data;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * 鉴权用的token vo ，实现 AuthenticationToken
 */
@Data
public class JwtToken implements AuthenticationToken {

    private String token;

    private String loginType;

    public JwtToken() {

    }

    public JwtToken(String token,String loginType) {
        this.token = token;
        this.loginType = loginType;
    }

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
    @Override
    public String toString() {
        return token;
    }
}