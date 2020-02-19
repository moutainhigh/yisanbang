package com.vtmer.yisanbang.shiro;

import com.vtmer.yisanbang.mapper.AdminMapper;
import com.vtmer.yisanbang.service.AdminRoleService;
import com.vtmer.yisanbang.service.PermissionService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

public class AdminRealm extends AuthorizingRealm {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private AdminRoleService adminRoleService;

    @Autowired
    private AdminMapper adminMapper;

    /**
     * 执行授权逻辑
     * 权限核心配置，根据数据库查询该用户的角色和权限
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("进入授权流程...");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        // 获取当前登陆用户
        Subject subject = SecurityUtils.getSubject();
        String adminName = subject.getPrincipal().toString();
        System.out.println(adminName);
        // 获取角色(id)并获取角色对应的权限url
        List<String> permUrls = new ArrayList<>();
        for (Object roleId : adminRoleService.selectRoleIdByName(adminName)) {
            permUrls.addAll(permissionService.selectUrlByRoleId((Integer) roleId));
        }
        // System.out.println(permUrls.size());
        authorizationInfo.addStringPermissions(permUrls);
        return authorizationInfo;
    }

    /**
     * 执行认证(登陆)逻辑
     * 凭证信息
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("进入认证流程...");
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        // 1.判断用户名是否存在
        String CorrectPassword = adminMapper.selectPasswordByName(token.getUsername());
        if (CorrectPassword == null) {
            // 用户名不存在
            return null; // Shiro底层会抛出UnKnowAccountException
        }
        // 2.判断密码是否正确
        String credentials = token.getUsername();
        return new SimpleAuthenticationInfo(token.getUsername(), CorrectPassword, ByteSource.Util.bytes(credentials), getName());
    }

    /**
     * 设置realm的HashedCredentialsMatcher
     */
    @PostConstruct
    public void setHashedCredentialsMatcher() {
        this.setCredentialsMatcher(hashedCredentialsMatcher());
    }

    /**
     * 凭证匹配：指定加密算法和散列次数
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        hashedCredentialsMatcher.setHashIterations(1024);
        return hashedCredentialsMatcher;
    }


}
