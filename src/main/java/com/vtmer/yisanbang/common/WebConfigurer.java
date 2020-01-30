package com.vtmer.yisanbang.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 实例化微信登录拦截器，并添加规则
 */
@Configuration
public class WebConfigurer implements WebMvcConfigurer {

    @Autowired
    private TokenInterceptor tokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .excludePathPatterns("/admin/**")
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/user/userId")
                // .excludePathPatterns("/cart/**")
                .addPathPatterns("/**");
    }

}
