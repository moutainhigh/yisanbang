package com.vtmer.yisanbang.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 实例化微信登录拦截器，并添加规则
 */
@Configuration
public class WebConfigurer implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/admin/**");
    }

}
