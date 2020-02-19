package com.vtmer.yisanbang.common.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class OrderNumberUtil {

    private static final int maxLength = 17;

    private static Random rand;

    static {
        try {
            rand = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    /**
     * 更具id进行加密+加随机数组成固定长度编码
     */
    private static String toCode(Integer id) {
        String idStr = id.toString();
        StringBuilder idsbs = new StringBuilder();
        for (int i = idStr.length() - 1 ; i >= 0; i--) {
            idsbs.append(idStr.charAt(i)-'0');
        }
        return idsbs.append(getRandom(maxLength - idStr.length())).toString();
    }

    /**
     * 生成固定长度随机码
     * @param n    长度
     */
    private static long getRandom(long n) {
        long min = 1,max = 9;
        for (int i = 1; i < n; i++) {
            min *= 10;
            max *= 10;
        }
        long rangeLong = (((long) (new Random().nextDouble() * (max - min)))) + min ;
        return rangeLong;
    }

    /**
     * 生成不带类别标头的编码
     * @param userId
     */
    private static synchronized String getCode(Integer userId){
        userId = userId == null ? 10000 : userId;
        return toCode(userId);
    }

    /**
     * 生成退货单号编码
     * @param userId
     */
    public static String getRefundNumber(Integer userId){
        return "2" + getCode(userId);
    }

    private static String getTime() {
        SimpleDateFormat sdfTime = new SimpleDateFormat("yyyyMMddHHmmss");

        return sdfTime.format(new Date());
    }

    private static int getRandomNum(){
        return rand.nextInt(900000)+100000;
    }

    public static String getOrderNumber() {
        return getTime() + getRandomNum();
    }

}
