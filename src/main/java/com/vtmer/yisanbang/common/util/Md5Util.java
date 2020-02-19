package com.vtmer.yisanbang.common.util;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;

public class Md5Util {
    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        // 创建MessageDigest对象，添加MD5处理
        MessageDigest digest = null;
        byte buffer[] = new byte[1024];
        int len;
        try (FileInputStream in = new FileInputStream(file)) {
            digest = MessageDigest.getInstance("MD5");
            // 读取图片
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        BigInteger bigInt = new BigInteger(1, digest.digest());
        System.out.println(bigInt);
        // 返回16进制表示形式
        return bigInt.toString(16);
    }

}
