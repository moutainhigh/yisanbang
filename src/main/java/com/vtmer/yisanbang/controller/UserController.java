package com.vtmer.yisanbang.controller;

import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.vo.Token;
import com.vtmer.yisanbang.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(tags = "微信登录(token)接口")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "微信用户登录", notes = "执行成功后返回用户对应的token")
    @PostMapping("/login")
    public ResponseMessage wxAppletLogin(@ApiParam(name = "code", value = "微信登录接口返回的code", required = true) @RequestBody Map<String, String> request) {
        if (!request.containsKey("code") || request.get("code") == null || request.get("code").equals("")) {
            return ResponseMessage.newErrorInstance("缺少参数code或code不合法");
        }
        Token token = userService.wxUserLogin(request.get("code"));
        return ResponseMessage.newSuccessInstance(token,"获取token用户成功");
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
