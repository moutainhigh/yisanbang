package com.vtmer.yisanbang.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class WxMiniPayOrderResult {

    @ApiModelProperty(value = "小程序的appid", example = "wx447bdd8f7d850c06")
    private String appId;

    @ApiModelProperty(value = "时间戳", example = "1582775722")
    private String timeStamp;

    @ApiModelProperty(value = "随机字符串", example = "1582775722610")
    private String nonceStr;

    @ApiModelProperty(value = "统一下单接口返回的 prepay_id 参数值,提交格式如：prepay_id=*",
            example = "prepay_id=wx271155232900316ea37868391248216100",
            notes = "由于package为java保留关键字，因此改为packageValue. 前端使用时记得要更改为package")
    private String packageValue;

    @ApiModelProperty(value = "签名类型", notes = "默认为MD5,注意此处需与统一下单的签名类型(MD5)一致", example = "MD5")
    private String signType;

    @ApiModelProperty(value = "签名",notes= "具体签名方案参见微信公众号支付帮助文档,这个直接拿来用就好")
    private String paySign;
}
