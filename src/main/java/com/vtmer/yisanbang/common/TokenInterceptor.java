package com.vtmer.yisanbang.common;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenInterceptor implements HandlerInterceptor {

    @Resource
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setCharacterEncoding("utf-8");
        String token = request.getHeader("accessToken");
        if(null != token) {
            // 验证token是否正确
            boolean flag = jwtUtil.verifyToken(token);
            if (flag) {
                return true;
            }
        }
        // 验证不通过，返回认证失败信息
        ResponseMessage responseMessage = ResponseMessage.newErrorInstance("token认证失败");
        response.getWriter().write(JSONObject.toJSONString(responseMessage));
        return false;
    }

}
