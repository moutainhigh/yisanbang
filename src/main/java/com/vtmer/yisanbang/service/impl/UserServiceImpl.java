package com.vtmer.yisanbang.service.impl;

import com.vtmer.yisanbang.common.HttpUtil;
import com.vtmer.yisanbang.common.JSONUtil;
import com.vtmer.yisanbang.common.JwtUtil;
import com.vtmer.yisanbang.domain.User;
import com.vtmer.yisanbang.vo.Token;
import com.vtmer.yisanbang.mapper.UserMapper;
import com.vtmer.yisanbang.service.UserService;
import com.vtmer.yisanbang.vo.Code2SessionResponse;
import com.vtmer.yisanbang.vo.WxAccount;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.net.URI;

@Service
public class UserServiceImpl implements UserService {

    @Value("${wx.applet.appid}")
    private String appId;

    @Value("${wx.applet.appsecret}")
    private String appSecret;

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private JwtUtil jwtUtil;

    @Autowired
    private UserMapper userMapper;

    /**
     * 微信官方API：code2session
     * 用户获取微信用户的openId和sessionKey
     *
     * @param code
     * @return
     */
    private String code2Session(String code) {
        String code2SessionUrl = "https://api.weixin.qq.com/sns/jscode2session";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("appid", appId);
        params.add("secret", appSecret);
        params.add("js_code", code);
        params.add("grant_type", "authorization_code");
        URI code2Session = HttpUtil.getUriWithParams(code2SessionUrl, params);
        return restTemplate.exchange(code2Session, HttpMethod.GET, new HttpEntity<String>(new HttpHeaders()),
                String.class).getBody();
    }

    @Override
    public Token wxUserLogin(String code) {
        // code2session接口返回JSON数据
        String resultJson = code2Session(code);
        // 解析数据
        Code2SessionResponse response = JSONUtil.toJavaObject(resultJson, Code2SessionResponse.class);
        if (!"0".equals(response.getErrcode())) {
            throw new AuthenticationException("code2session失败：" + response.getErrmsg());
        } else {
            // 查询数据库是否存在该微信用户
            User user = userMapper.selectUserByOpenId(response.getOpenid());
            if (null == user) {
                // 若不存在则新建用户到数据库中
                userMapper.addUser(response.getOpenid());
            }
            // JWT返回自定义登录态token并把token缓存到redis中
            WxAccount wxAccount = new WxAccount();
            wxAccount.setOpenId(response.getOpenid());
            wxAccount.setSessionKey(response.getSession_key());
            String token = jwtUtil.createTokenByUser(wxAccount);
            return new Token(token);
        }
    }

    @Override
    public Integer getUserIdByToken(String token) {
        String openId = jwtUtil.getOpenIdByToken(token);
        User user = userMapper.selectUserByOpenId(openId);
        if (null != user) {
            return user.getId();
        }
        return null;
    }

    public String getOpenIdByUserId(Integer userId) {
        return userMapper.selectOpenIdByUserId(userId);
    }

    @Override
    public User selectByPrimaryKey(Integer userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

}