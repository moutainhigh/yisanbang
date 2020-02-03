package com.vtmer.yisanbang.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "wx.pay")
public class WxPayProperties {
    /**
     * 设置小程序的appId
     */
    private String appId;

    /**
     * 微信支付商户号
     */
    private String mchId;

    /**
     * 微信支付商户密钥1
     */
    private String mchKey;

    /**
     * payNotifyURL:异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
     */
    private String payNotifyURL;

    /**
     * refundNotifyURL:异步接收微信退款结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
     */
    private String refundNotifyURL;

    /**
     * tradeType：交易类型，小程序为JSAPI
     */
    private String tradeType;

    /**
     * apiclient_cert.p12文件的绝对路径，或者如果放在项目中，请以classpath:开头指定
     */
    private String keyPath;
}
