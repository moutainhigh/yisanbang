package com.vtmer.yisanbang.service.impl;

import com.vtmer.yisanbang.common.exception.service.third.Code2SessionException;
import com.vtmer.yisanbang.common.util.EncryptUtils;
import com.vtmer.yisanbang.common.util.HttpUtil;
import com.vtmer.yisanbang.common.util.JSONUtil;
import com.vtmer.yisanbang.common.util.JwtUtil;
import com.vtmer.yisanbang.domain.Admin;
import com.vtmer.yisanbang.domain.AdminRole;
import com.vtmer.yisanbang.domain.User;
import com.vtmer.yisanbang.mapper.AdminMapper;
import com.vtmer.yisanbang.mapper.AdminRoleMapper;
import com.vtmer.yisanbang.mapper.UserMapper;
import com.vtmer.yisanbang.service.UserService;
import com.vtmer.yisanbang.vo.Code2SessionResponse;
import com.vtmer.yisanbang.vo.Token;
import com.vtmer.yisanbang.vo.WxAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

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

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AdminRoleMapper adminRoleMapper;

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
        logger.info("开始请求微信登录接口，获取openid和sessionkey");
        URI code2Session = HttpUtil.getUriWithParams(code2SessionUrl, params);
        return restTemplate.exchange(code2Session, HttpMethod.GET, new HttpEntity<String>(new HttpHeaders()),
                String.class).getBody();
    }

    @Override
    public Token wxUserLogin(String code) {
        // code2session接口返回JSON数据
        String resultJson = code2Session(code);
        logger.info("code2session接口返回JSON数据{}", resultJson);
        // 解析数据
        Code2SessionResponse response = JSONUtil.toJavaObject(resultJson, Code2SessionResponse.class);

        if (!"0".equals(response.getErrcode())) {
            throw new Code2SessionException("code2session失败：" + response.getErrmsg());
        } else {
            String openid = response.getOpenid();
            String sessionKey = response.getSession_key();
            //String sessionKey = "GZY0V5EDGRQXMjllyjSPGg==";
            //String openid = "oSWGq5VifPgfIIF7eHFjNh9GEr_g";
            // 查询数据库是否存在该微信用户
            User user = userMapper.selectUserByOpenId(openid);
            User newUser = new User();
            if (null == user) {
                logger.info("微信登录--用户不在数据库中，向数据库插入信息");
                // String openid = response.getOpenid();
                // 若不存在则新建用户到数据库中
                newUser.setOpenId(openid);
                userMapper.addUser(newUser);
                user = newUser;
                // 把用户openId插入Admin表中，并分配普通用户角色，并设置密码为一个空格
                Admin admin = new Admin();
                admin.setName(user.getOpenId());
                admin.setPassword(EncryptUtils.encrypt(openid, "123456").toString());
                adminMapper.insertAdmin(admin);
                AdminRole adminRole = new AdminRole();
                adminRole.setAdminId(admin.getId());
                adminRole.setRoleId(3);
                adminRoleMapper.insert(adminRole);
            }
            // JWT返回自定义登录态token并把token缓存到redis中
            WxAccount wxAccount = new WxAccount();
            wxAccount.setUserId(user.getId());
            wxAccount.setOpenId(user.getOpenId());
            wxAccount.setSessionKey(sessionKey);
            String token = jwtUtil.createTokenByUser(wxAccount);
            logger.info("JWT返回自定义登录态token[{}]，并把token缓存到redis中", token);
            return new Token(token);
        }
    }

        @Override
        public Integer getUserIdByToken (String token){
            String openId = jwtUtil.getOpenIdByToken(token);
            User user = userMapper.selectUserByOpenId(openId);
            if (null != user) {
                return user.getId();
            }
            return null;
        }

        @Override
        public String getOpenIdByUserId (Integer userId){
            return userMapper.selectOpenIdByUserId(userId);
        }

        @Override
        public User selectByPrimaryKey (Integer userId){
            return userMapper.selectByPrimaryKey(userId);
        }

    }
