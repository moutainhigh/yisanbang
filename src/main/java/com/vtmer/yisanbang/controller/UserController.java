package com.vtmer.yisanbang.controller;

import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.dto.Token;
import com.vtmer.yisanbang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseMessage wxAppletLogin(@RequestBody Map<String, String> request) {
        if (!request.containsKey("code") || request.get("code") == null || request.get("code").equals("")) {
            return ResponseMessage.newErrorInstance("缺少参数code或code不合法");
        }
        Token token = userService.wxUserLogin(request.get("code"));
        return ResponseMessage.newSuccessInstance(token,"获取token用户成功");
    }

}
