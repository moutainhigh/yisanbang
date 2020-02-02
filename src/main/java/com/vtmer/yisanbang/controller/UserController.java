package com.vtmer.yisanbang.controller;

import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.vo.Token;
import com.vtmer.yisanbang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/userId")
    public ResponseMessage getUserIdByToken(@RequestBody Map<String, String> token) {
        Integer userId = userService.getUserIdByToken(token.get("token"));
        if (null == userId || userId.equals("")) {
            return ResponseMessage.newErrorInstance("获取用户Id失败");
        }
        return ResponseMessage.newSuccessInstance(userId, "获取用户Id成功");
    }

}
