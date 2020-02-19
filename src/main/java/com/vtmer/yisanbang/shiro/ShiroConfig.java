package com.vtmer.yisanbang.shiro;

import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.*;

@Configuration
public class ShiroConfig {
    /**
     * 创建ShiroFilterFactoryBean
     */
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("jwt", new JwtFilter());
        factoryBean.setFilters(filterMap);
        // 设置安全管理器
        factoryBean.setSecurityManager(securityManager);
        // 添加Shiro内置过滤器
        Map<String, String> filterRuleMap = new LinkedHashMap<>();
        //filterRuleMap.put("/admin/**", "anon");
        //filterRuleMap.put("/user/**", "anon");
        //filterRuleMap.put("/cart/**", "anon");
        filterRuleMap.put("/**","authc");

        factoryBean.setFilterChainDefinitionMap(filterRuleMap);
        return factoryBean;
    }

    /**
     * 创建DefaultWebSecurityManager
     */
    @Bean("securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("adminRealm") AdminRealm adminRealm,
                                                                  @Qualifier("userRealm") UserRealm userRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        ModularRealmAuthenticator modularRealmAuthenticator = new ModularRealmAuthenticator();
        // 设置只要有一个Realm验证成功即可，只返回第一个Realm身份验证成功的认证信息
        modularRealmAuthenticator.setAuthenticationStrategy(new FirstSuccessfulStrategy());
        securityManager.setAuthenticator(modularRealmAuthenticator);

        List<Realm> realms = new ArrayList<>();
        // 配置多个Realm
        realms.add(adminRealm);
        realms.add(userRealm);
        securityManager.setRealms(realms);
        return securityManager;
    }

    /**
     * 创建Realm
     */
    @Bean("adminRealm")
    public AdminRealm getAdminRealm() {
        return new AdminRealm();
    }

    @Bean("userRealm")
    public UserRealm getUserRealm() {
        return new UserRealm();
    }

    @Bean
    public static DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setUsePrefix(true);
        return defaultAdvisorAutoProxyCreator;
    }
}
