package com.vtmer.yisanbang.shiro;

import com.alibaba.fastjson.JSONObject;
import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.common.util.JwtUtil;
import com.vtmer.yisanbang.domain.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT核心过滤器配置
 */
public class JwtFilter extends BasicHttpAuthenticationFilter {

    @Autowired
    private JwtUtil jwtUtil;

    private final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    private static final ThreadLocal<User> t1 = new ThreadLocal<>();

    /**
     * 判断用户是否想要进行 需要验证的操作
     * 检测header里面是否包含Authorization字段即可
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        String auth = getAuthzHeader(request);
        // 如果auth不为null则需要验证
        return auth != null && !"".equals(auth);
    }

    /**
     * 此方法调用登陆，验证逻辑
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (isLoginAttempt(request,response)) {
            // 用户带了token，想要登入，进行shiro认证和token认证
            try {
                // 进行shiro的登录LoginRealm
                executeLogin(request,response);
            } catch (IllegalStateException e) {
                logger.warn("Not find any token,may be token has expired or token is invalid");
                return false;
            } catch (Exception e) {
                logger.error("Error occurs when login",e);
            }
        }
        return super.isAccessAllowed(request,response,mappedValue);
    }

    /**
     * 这里重写了父类的方法，使用我们自己定义的Token类，提交给shiro。这个方法返回null的话会直接抛出异常，进入isAccessAllowed（）的异常处理逻辑。
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        String token = getAuthzHeader(request);
        // 因为是先走shiro认证，再走JWT认证，在这里先要认证token的正确性，对了再去获取openid，否则会抛出异常
        // com.auth0.jwt.exceptions.JWTDecodeException: The token was expected to have 3 parts, but got 1.
        // 在这里有两种情况会返回false，一是token过期，二是token不正确，这两种的处理方案都是一致的，让用户去重新请求登录接口，获取新的token
        boolean res = jwtUtil.verifyToken(token);
        if (!res) {
            // 如果token认证失败，直接返回null，进入isAccessAllowed（）的异常处理逻辑，抛出IllegalStateException异常并捕获
            return null;
        }
        t1.set(jwtUtil.getUserInfoByToken(token));
        String openId = jwtUtil.getOpenIdByToken(token);
        if (StringUtils.isNotBlank(token)) {
            return new UsernamePasswordToken(openId, "123456");
        }
        return null;
    }

    /**
     *  如果Shiro Login认证成功，会进入该方法，等同于用户名密码登录成功，我们这里还判断了是否要刷新Token
     *  暂时不需要该方法 —— 目前的方案是，如果token过期了，前端再次调用小程序登录的方法获取新token访问
     */
    /*
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        String newToken = null;
        if(token instanceof UsernamePasswordToken) {
            // 从request中取出旧的token
            String oldToken = getAuthzHeader(request);
            // 验证该token是否有效
            if (!jwtUtil.verifyToken(oldToken)) {
                // 如果该token验证失败，说明token已经过期了(因为之前通过了shiro登录认证，token应该是正确的)，我们给token续期
                jwtUtil.acceptExpiresAt(oldToken);
            }
        }
        return true;
    }
     */

    /**
     * 提供跨域支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    /**
     *  认证失败依旧继续执行过滤器链
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        //这里是个坑，如果不设置的接受的访问源，那么前端都会报跨域错误，因为这里还没到corsConfig里面
        httpServletResponse.setHeader("Access-Control-Allow-Origin", ((HttpServletRequest) request).getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json");
        ResponseMessage responseMessage = ResponseMessage.newErrorInstance("请先登录后再进行操作");
        httpServletResponse.getWriter().write(JSONObject.toJSON(responseMessage).toString());
        return false;
    }

    public static User getLoginUser() {
        User user = t1.get();
        t1.remove();
        return user;
    }
}
