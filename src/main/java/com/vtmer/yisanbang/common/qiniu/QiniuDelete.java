package com.vtmer.yisanbang.common.qiniu;

import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.rs.RSClient;

public class QiniuDelete {

    // 设置好账号的ACCESS_KEY和SECRET_KEY
    private static String ACCESS_KEY = VariableName.accessKey; // 这两个登录七牛 账号里面可以找到
    private static String SECRET_KEY = VariableName.secretKey;

    // 要上传的空间
    private static String bucketName = VariableName.bucket; // 对应要上传到七牛上 你的那个路径（自己建文件夹 注意设置公开

    public static void deleteFile(String fileName) {
        Mac mac = new Mac(ACCESS_KEY, SECRET_KEY);
        RSClient rsClient = new RSClient(mac);
        try {
            rsClient.delete(bucketName, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
