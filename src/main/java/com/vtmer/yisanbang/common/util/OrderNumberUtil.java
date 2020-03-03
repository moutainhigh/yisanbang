package com.vtmer.yisanbang.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class OrderNumberUtil {

    private static Integer number = 100;//唯一数字,集群第一台=0，第二台=200000,第三台=400000
    private static int maxNum=200000;//最大值,集群第一台=200000，第二台=400000,第三台=600000
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");//年月日格式

    private static final long maxLength = 17;

    /**
     * uuid生成没有 — 的编号
     * @return
     */
    public static String createUUID(){
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("\\-", "");
    }

    /**
     * 生成订单编号 17+ 位数
     * @return
     */
    public static String getOrderNumber(){
        number++;//唯一数字自增
        if(number>=maxNum){ // 值的上限，超过就归零
            number=maxNum-200000;
        }
        return sdf.format(new Date())+number;//返回时间+一毫秒内唯一数字的编号，区分机器可以加字母ABC...
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

}
