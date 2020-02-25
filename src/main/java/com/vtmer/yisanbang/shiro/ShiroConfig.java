package com.vtmer.yisanbang.shiro;

import com.vtmer.yisanbang.domain.Permission;
import com.vtmer.yisanbang.mapper.PermissionMapper;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
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
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        Map<String, Filter> filterMap = factoryBean.getFilters();
        filterMap.put("jwt", jwtFilter());
        //将自定义 的FormAuthenticationFilter注入shiroFilter中
        filterMap.put("perms",new ShiroPermissionFilter());
        factoryBean.setFilters(filterMap);
        // 设置安全管理器
        factoryBean.setSecurityManager(securityManager);
        factoryBean.setFilterChainDefinitionMap(setFilterChainDefinitionMap());
        return factoryBean;
    }

    private Map<String, String> setFilterChainDefinitionMap() {
        // 添加Shiro内置过滤器
        Map<String, String> filterRuleMap = new LinkedHashMap<>();
        // 设置首页内容、登录、登出页面等无需认证
        filterRuleMap.put("/admin/login", "anon");
        filterRuleMap.put("/user/login", "anon");
        filterRuleMap.put("/admin/logout", "anon");
        filterRuleMap.put("/ad/list", "anon");
        filterRuleMap.put("/carousel/list", "anon");
        // 管理员添加接口放行，方便测试
        filterRuleMap.put("/admin/addAdmin", "anon");
        // get接口放行...
        // 分类模块接口放行
        filterRuleMap.put("/sort/get/**","anon");
        // 单间商品与套装商品管理接口放行
        filterRuleMap.put("/goodsAndSuit/**","anon");
        // 商品查询接口放行
        filterRuleMap.put("/goods/get/**","anon");
        // 商品详情接口放行
        filterRuleMap.put("/goodsDetail/get/**","anon");
        // 获取商家默认收货地址接口放行
        filterRuleMap.put("/businessAddress/getDefault","anon");
        // 套装接口放行
        filterRuleMap.put("/suit/get/**","anon");
        // 套装详情接口放行
        filterRuleMap.put("/suitDetail/get/**","anon");
        // 广告接口放行
        filterRuleMap.put("/ad/get/**","anon");
        // 轮播图接口放行
        filterRuleMap.put("/carousel/get/**","anon");
        // 部件尺寸管理接口放行
        filterRuleMap.put("/partSize/get/**","anon");
        // 颜色尺寸管理接口放行
        filterRuleMap.put("/colorSize/get/**","anon");
        //swagger接口权限 开放
        filterRuleMap.put("/swagger-ui.html", "anon");
        filterRuleMap.put("/doc.html", "anon");
        filterRuleMap.put("/v2/api-docs", "anon");
        filterRuleMap.put("/v2/api-docs-ext", "anon");
        filterRuleMap.put("/webjars/**", "anon");
        filterRuleMap.put("/v2/**", "anon");
        filterRuleMap.put("/swagger-resources/**", "anon");
        filterRuleMap.put("/static/**", "anon");
        filterRuleMap.put("/**", "jwt");
        // 获取所有需要权限认证的接口路径
        List<Permission> permissions = permissionMapper.selectAll();
        for (Permission p : permissions) {
            filterRuleMap.put(p.getUrl(), "perms[" + p.getUrl() + "]");
        }
        return filterRuleMap;
    }

    /**
     * 创建DefaultWebSecurityManager
     */
    @Bean("securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("loginRealm") LoginRealm loginRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(loginRealm);
        return securityManager;
    }

    /**
     * 创建Realm
     */
    @Bean("loginRealm")
    public LoginRealm getLoginRealm() {
        return new LoginRealm();
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
    /*
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
     */

    @Bean
    public FilterRegistrationBean registerJwtFilter(@Autowired JwtFilter jwtFilter) {
        // 设置jwt filter不自动注册到spring管理的监听器中，防止与shiro filter同级，导致该监听器必定执行
        FilterRegistrationBean<JwtFilter> jwtFilterRegister = new FilterRegistrationBean<>(jwtFilter);
        jwtFilterRegister.setEnabled(false);
        return jwtFilterRegister;
    }

    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter();
    }

}
