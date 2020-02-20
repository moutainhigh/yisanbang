package com.vtmer.yisanbang.shiro;

import com.vtmer.yisanbang.domain.Permission;
import com.vtmer.yisanbang.mapper.PermissionMapper;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Autowired
    private PermissionMapper permissionMapper;

    /**
     * 创建ShiroFilterFactoryBean
     */
    /*
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        Map<String, Filter> filterMap = new HashMap<>();
        //filterMap.put("jwt", new JwtFilter());
        factoryBean.setFilters(filterMap);
        // 设置安全管理器
        factoryBean.setSecurityManager(securityManager);
        factoryBean.setFilterChainDefinitionMap(setFilterChainDefinitionMap());
        return factoryBean;
    }

     */

    private Map<String, String> setFilterChainDefinitionMap() {
        // 添加Shiro内置过滤器
        Map<String, String> filterRuleMap = new LinkedHashMap<>();
        // 设置首页内容、登录、登出页面等无需认证
        filterRuleMap.put("/admin/login", "anon");
        filterRuleMap.put("/user/login", "anon");
        filterRuleMap.put("/admin/logout", "anon");
        filterRuleMap.put("/ad/list", "anon");
        filterRuleMap.put("/carousel/list", "anon");
        // 获取所有需要权限认证的接口路径
        List<Permission> permissions = permissionMapper.selectAll();
        for (Permission p : permissions) {
            //filterRuleMap.put(p.getUrl(), "perms[" + p.getUrl() + "]");
        }
        //filterRuleMap.put("/**", "jwt");
        //filterRuleMap.put("/**", "authc");
        return filterRuleMap;
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

    /**
     * 开启注解验证
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

}
