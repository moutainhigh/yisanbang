package com.vtmer.yisanbang.common;

import com.alibaba.fastjson.JSONObject;
import com.vtmer.yisanbang.common.util.JwtUtil;
import com.vtmer.yisanbang.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    private final Logger logger = LoggerFactory.getLogger(TokenInterceptor.class);
    /**
     * 定义一个线程域，存储登录用户
     */
    private static final ThreadLocal<User> t1 = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setCharacterEncoding("utf-8");
        String token = request.getHeader("accessToken");
        if(null != token) {
            // 验证token是否正确
            boolean flag = jwtUtil.verifyToken(token);
            System.out.println(flag);
            if (flag) {
                logger.info("验证token成功，开始设置user对象进线程域");
                t1.set(JwtUtil.getUserInfoByToken(token));
                return true;
            }
        }
        // 验证不通过，返回认证失败信息
        ResponseMessage responseMessage = ResponseMessage.newErrorInstance("token认证失败");
        response.getWriter().write(JSONObject.toJSONString(responseMessage));
        return false;
    }

    public static User getLoginUser() {
        return t1.get();
    }

}
