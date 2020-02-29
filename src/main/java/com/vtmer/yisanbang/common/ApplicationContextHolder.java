package com.vtmer.yisanbang.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextHolder implements ApplicationContextAware {
    private static ApplicationContext ctx;

    @Override
    //向工具类注入applicationContext
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;     //ctx就是注入的applicationContext
    }

    //外部调用ctx
    public static ApplicationContext getCtx(){
        return ctx;
    }


    //getbean有两种方式拿：1）按类型拿  2）按名字拿
    //从应用上下文里面获得类实例（即bean容器里面获得类容器）
    //为什么我们现在要采用这种麻烦的方法（以前直接用Autowired注解自动装配进去了）--- 这与redis连接池有关
    //用Redis时，建了许多连接池，我们在redis里面拿缓存对象时，缓存对象与每个连接都有一个RedisTemplate,你在注入时用自动注入，不同
    // RedisTemplate是同类型同名的，注入时你得到的是哪个连接使用的redisTemplate呢？所以你注入时分不清
    //所以我们重新封装一个getBean的方法，按指定类型或名字来拿bean实例
    public static <T> T getBean(Class<T> tClass){
        return ctx.getBean(tClass);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name){
        return (T) ctx.getBean(name);
    }

}