package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.domain.User;
import com.vtmer.yisanbang.vo.Token;

public interface UserService {
    // 根据用户id查找用户
    public User selectByPrimaryKey(Integer userId);

    // 微信用户登录
    Token wxUserLogin(String code);
}
