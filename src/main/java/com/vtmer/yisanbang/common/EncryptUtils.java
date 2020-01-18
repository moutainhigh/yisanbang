package com.vtmer.yisanbang.common;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

public class EncryptUtils {
    public static Object encrypt(String userName, String enablePassword) {
        // 加密算法
        String hashAlgorithmName = "md5";
        // 盐值
        String salt = userName;
        // 散列次数
        int hashIterations = 1024;
        // 盐
        ByteSource credentialsSalt = ByteSource.Util.bytes(salt);
        return new SimpleHash(hashAlgorithmName, enablePassword, credentialsSalt, hashIterations);
    }
}
