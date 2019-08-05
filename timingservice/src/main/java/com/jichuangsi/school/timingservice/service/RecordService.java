package com.jichuangsi.school.timingservice.service;

import com.jichuangsi.school.timingservice.entity.Record;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface RecordService {

    void daKa(String name, String wifiName, String longitudeLatitude,String stuas,String openId,String ruleId);

    List<Record> findAllByOpenId(String openId);

    List<Record> findPxcPeopleOpenid();

    Record findAllByOpenIdAndRuleIdAndStuas(String openid, String id);

    List<Record> findAllByOpenIdAndStuas(String openId);
}
