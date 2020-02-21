package com.vtmer.yisanbang.shiro;

import com.vtmer.yisanbang.common.util.JwtUtil;
import com.vtmer.yisanbang.mapper.AdminMapper;
import com.vtmer.yisanbang.service.AdminRoleService;
import com.vtmer.yisanbang.service.PermissionService;
import com.vtmer.yisanbang.vo.JwtToken;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

public class UserRealm extends AuthorizingRealm {

    private static final Logger logger = LoggerFactory.getLogger(UserRealm.class);

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private AdminRoleService adminRoleService;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("进入token授权流程...");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        // 获取当前登陆用户
        Subject subject = SecurityUtils.getSubject();
        String token = subject.getPrincipal().toString();
        // token续期
        jwtUtil.verifyToken(token);
        // System.out.println(token);
        String userOpenId = jwtUtil.getOpenIdByToken(token);
        // 获取角色(id)并获取角色对应的权限url
        List<String> permUrls = new ArrayList<>();
        for (Object roleId : adminRoleService.selectRoleIdByName(userOpenId)) {
            permUrls.addAll(permissionService.selectUrlByRoleId((Integer) roleId));
        }
        // System.out.println(permUrls.size());
        authorizationInfo.addStringPermissions(permUrls);
        return authorizationInfo;
    }

    /**
     * 校验 验证token逻辑
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
        System.out.println("进入验证流程...");
        String jwtToken = (String) token.getCredentials();
        logger.debug("token值为"+jwtToken);
        String wxOpenId = jwtUtil.getOpenIdByToken(jwtToken);
        String sessionKey = jwtUtil.getSessionKeyByToken(jwtToken);
        if (wxOpenId == null || wxOpenId.equals("")) {
            throw new UnknownAccountException("user account not exists , please check your token");
        }
        if (sessionKey == null || sessionKey.equals("")) {
            throw new AuthenticationException("sessionKey is invalid , please check your token");
        }
        if (!jwtUtil.verifyToken(jwtToken)) {
            throw new AuthenticationException("token is invalid , please check your token");
        }
        return new SimpleAuthenticationInfo(token, token, getName());
    }

    /**
     * 自定义 JWT的 Realm
     * 重写 Realm 的 supports() 方法是通过 JWT 进行登录判断的关键
     * <p>
     * 注意坑点 : 必须重写此方法，不然Shiro会报错
     * 因为创建了 JWTToken 用于替换Shiro原生 token,所以必须在此方法中显式的进行替换，否则在进行判断时会一直失败
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 设置realm的HashedCredentialsMatcher
     */
    @PostConstruct
    public void setHashedCredentialsMatcher() {
        this.setCredentialsMatcher(credentialsMatcher());
    }

    /**
     * 注意坑点 : 密码校验 , 这里因为是JWT形式,就无需密码校验和加密,直接让其返回为true(如果不设置的话,该值默认为false,即始终验证不通过)
     */
    private CredentialsMatcher credentialsMatcher() {
        return (token, info) -> true;
    }
}
