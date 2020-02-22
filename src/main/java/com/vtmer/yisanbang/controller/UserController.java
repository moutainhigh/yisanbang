package com.vtmer.yisanbang.controller;

import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.common.exception.api.ApiException;
import com.vtmer.yisanbang.common.exception.api.shiro.ApiUnknownAccountException;
import com.vtmer.yisanbang.common.exception.api.third.ApiCode2SessionException;
import com.vtmer.yisanbang.common.exception.service.third.Code2SessionException;
import com.vtmer.yisanbang.common.util.JwtUtil;
import com.vtmer.yisanbang.service.UserService;
import com.vtmer.yisanbang.vo.Token;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(tags = "微信登录(token)接口")
@RestController
@RequestMapping("/user")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @ApiOperation(value = "微信用户登录", notes = "执行成功后返回用户对应的token")
    @PostMapping("/login")
    public ResponseMessage wxAppletLogin(@ApiParam(name = "code", value = "微信登录接口返回的code", required = true) @RequestBody Map<String, String> request) {
        if (!request.containsKey("code") || request.get("code") == null || request.get("code").equals("")) {
            return ResponseMessage.newErrorInstance("缺少参数code或code不合法");
        }
        try {
            Token token = userService.wxUserLogin(request.get("code"));
            String openId = jwtUtil.getOpenIdByToken(token.getToken());
            // 执行验证过程
            Subject subject = SecurityUtils.getSubject();
            // 用户的账号为openId，密码为一个空格
            UsernamePasswordToken UsernamePasswordToken = new UsernamePasswordToken(openId, "123456");
            subject.login(UsernamePasswordToken);
            if (subject.isAuthenticated()) {
                // 已经认证
                return ResponseMessage.newSuccessInstance(token,"获取token用户成功");
            }
        } catch (Code2SessionException e) {
            throw new ApiCode2SessionException(e.getMessage());
        } catch (UnknownAccountException e) {
            throw new ApiUnknownAccountException(e.getMessage());
        } catch (Exception e) {
            throw new ApiException(e);
        }
        return null;
    }

    @ApiOperation("根据用户登录对应的token获取用户id")
    @GetMapping("/userId")
    public ResponseMessage getUserIdByToken(@ApiParam(name = "token", value = "用户微信登录返回的token", required = true) @RequestBody Map<String, String> request) {
        if (!request.containsKey("token") || request.get("token") == null || request.get("token").equals("")) {
            return ResponseMessage.newErrorInstance("缺少参数用户id或用户id不合法");
        }
        Integer userId = userService.getUserIdByToken(request.get("token"));
        if (null == userId || userId.equals("")) {
            return ResponseMessage.newErrorInstance("获取用户Id失败");
        }
        return ResponseMessage.newSuccessInstance(userId, "获取用户Id成功");
    }

}
