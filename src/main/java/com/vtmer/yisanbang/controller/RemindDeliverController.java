package com.vtmer.yisanbang.controller;

import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.common.annotation.RequestLog;
import com.vtmer.yisanbang.domain.RemindDeliver;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Api(tags = "提醒发货接口")
@RestController
@RequestMapping("/remindDeliver")
public class RemindDeliverController {

    @RequestLog(module = "提醒发货", operationDesc = "获取所有的提醒消息")
    @ApiOperation(value = "获取所有的提醒消息")
    @GetMapping("/getAll")
    public ResponseMessage<RemindDeliver> getAll() {
        return null;
    }

    @RequestLog(module = "提醒发货", operationDesc = "用户点击[提醒发货]")
    @ApiOperation(value = "用户点击[提醒发货]")
    @PostMapping("/post/{orderNumber}")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "校验token", name = "Authorization", paramType = "header", required = true)
    })
    public ResponseMessage post(@ApiParam(name = "orderNumber", value = "订单编号", required = true)
                                @NotBlank(message = "订单编号为空") @PathVariable String orderNumber) {
        return null;
    }

    @RequestLog(module = "提醒发货", operationDesc = "删除提醒发货通知")
    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除提醒发货通知", notes = "删除之后不可撤回，消息会消失")
    public ResponseMessage delete(@ApiParam(name = "id", value = "提醒发货信息id", required = true)
                                  @NotNull(message = "提醒发货信息id") @PathVariable int id) {
        return null;
    }

    @RequestLog(module = "提醒发货", operationDesc = "更新待阅状态为已阅")
    @PutMapping("/updateRead/{id}")
    @ApiOperation(value = "更新待阅状态为已阅")
    public ResponseMessage updateRead(@ApiParam(name = "id", value = "提醒发货信息id", required = true)
                                  @NotNull(message = "提醒发货信息id") @PathVariable int id) {
        return null;
    }

    @RequestLog(module = "提醒发货", operationDesc = "更新待办状态为已处理")
    @PutMapping("/updateDone/{id}")
    @ApiOperation(value = "更新待办状态为已处理")
    public ResponseMessage updateDone(@ApiParam(name = "id", value = "提醒发货信息id", required = true)
                                      @NotNull(message = "提醒发货信息id") @PathVariable int id) {
        return null;
    }

}
