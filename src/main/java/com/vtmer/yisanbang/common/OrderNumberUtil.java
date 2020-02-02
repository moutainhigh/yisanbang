package com.vtmer.yisanbang.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class OrderNumberUtil {

    private static String getTime() {
        SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("时间戳："+sdfTime.format(new Date()));
        return sdfTime.format(new Date());
    }

    private static int getRandomNum(){
        Random r = new Random();
        return r.nextInt(900000)+100000;
    }

    public static String getOrderNumber() {
        String orderNumber = getTime().replaceAll("[[\\s-:punct:]]", "") + getRandomNum();
        return orderNumber;
    }

}
