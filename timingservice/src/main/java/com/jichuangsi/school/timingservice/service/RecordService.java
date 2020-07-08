package com.jichuangsi.school.timingservice.service;

import com.jichuangsi.school.timingservice.entity.Record;
import com.jichuangsi.school.timingservice.model.DailyListModel;
import com.jichuangsi.school.timingservice.model.UserInfoForToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


public interface RecordService {

    void daKa(String name, String wifiName, String longitudeLatitude,String stuas,String openId,String ruleId);

    List<Record> findAllByOpenId(String openId);

    List<Record> findPxcPeopleOpenid();

    List<Record> findAllByOpenIdAndRuleIdAndStuas(String openid, String id);

    List<Record> findAllByOpenIdAndStuas(String openId);

    List<Record> findAllByOpenIdAndRuleIdAndStuas2(String openId, String s);


    List<Record> findAllByOpenIdAndStuasAndTimeBetween(String openId, long timeStart, long timeEnd);

    List<Record> findAllByOpenIdAndTimeBetween(String openId, long timeStart, long timeEnd);

    List<Record> findAllByOpenIdAndStuasAndRuleIdAndTimeBetween(String openId, long timeStart, long timeEnd,String ruleId);

    int countByOpendIdInAndStatusAndTime(List<String> openIds, long timeStart, long timeEnd,String status);

    DailyListModel getStatisticsByMonth(UserInfoForToken userInfoForToken, String dpid, String timeStart, String timeEnd);

    DailyListModel getStatisticsByWeek(UserInfoForToken userInfoForToken, String dpid, String timeStart, String timeEnd);

    DailyListModel backGetStatisticsByMonth(UserInfoForToken userInfoForToken, String dpid, String timeStart, String timeEnd);

    DailyListModel backGetStatisticsByWeek(UserInfoForToken userInfoForToken, String dpid, String timeStart, String timeEnd);

    Map<String,List<Object>> getStatisticsChartByTime(UserInfoForToken userInfoForToken, String dpid, String timeStart, String timeEnd);
}
