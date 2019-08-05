package com.jichuangsi.school.timingservice.utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

public class TimeUtils {

    public static long todayMorning(){
        long nowTime =System.currentTimeMillis();
        long todayStartTime = nowTime - (nowTime + TimeZone.getDefault().getRawOffset())% (1000*3600*24);
        return todayStartTime;
    }
    public static long tomorrowMorning(){
        long nowTime =System.currentTimeMillis();
        long tomorrowStartTime = nowTime - (nowTime + TimeZone.getDefault().getRawOffset())% (1000*3600*24)+(1000*3600*24);
        return tomorrowStartTime;
    }
    public static long startTime(String date) {
        //获取指定时间的时间戳，除以1000说明得到的是秒级别的时间戳（10位）
        long time = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(date+" 00:00:00", new ParsePosition(0)).getTime() / 1000;

        //获取时间戳
        long now1 = System.currentTimeMillis();
        long now2 = new Date().getTime();

        return  time;
    }

    public static long endTime(String date) {

        //获取指定时间的时间戳，除以1000说明得到的是秒级别的时间戳（10位）
        long time = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(date+" 23:59:59", new ParsePosition(0)).getTime() / 1000;

        //获取时间戳
        long now1 = System.currentTimeMillis();
        long now2 = new Date().getTime();

        return  time;
    }
}
