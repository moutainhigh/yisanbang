package com.vtmer.yisanbang.controller;

import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.common.exception.api.ApiException;
import com.vtmer.yisanbang.common.exception.api.third.ApiCode2SessionException;
import com.vtmer.yisanbang.common.exception.service.third.Code2SessionException;
import com.vtmer.yisanbang.common.util.JwtUtil;
import com.vtmer.yisanbang.service.UserService;
import com.vtmer.yisanbang.vo.Token;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
            return ResponseMessage.newSuccessInstance(token,"获取token用户成功");
        } catch (Code2SessionException e) {
            throw new ApiCode2SessionException(e.getMessage());
        } catch (Exception e) {
            throw new ApiException(e);
        }
    }

}
