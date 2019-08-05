package com.jichuangsi.school.timingservice.utils;

import java.util.*;

public class TimeUtils {

    public static long todayMorning(){
        long nowTime =System.currentTimeMillis();
        long todayStartTime = nowTime - (nowTime + TimeZone.getDefault().getRawOffset())% (1000*3600*24);
        return todayStartTime;
    }
}
