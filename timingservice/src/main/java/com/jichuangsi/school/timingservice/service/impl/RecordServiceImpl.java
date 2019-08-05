package com.jichuangsi.school.timingservice.service.impl;

import com.jichuangsi.school.timingservice.entity.Record;
import com.jichuangsi.school.timingservice.repository.RecordRepostitory;
import com.jichuangsi.school.timingservice.service.RecordService;
import com.jichuangsi.school.timingservice.utils.TimeUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class RecordServiceImpl implements RecordService {

    @Resource
    private RecordRepostitory recordRepostitory;


    @Override
    public void daKa(String name, String wifiName, String longitudeLatitude, String stuas,String openId,String ruleId) {
        long nowTime =System.currentTimeMillis();
            recordRepostitory.insertRecord(longitudeLatitude,name, stuas,nowTime,wifiName,openId,ruleId);
//        Record record = new Record();
//        record.setLongitudeLatitude(longitudeLatitude);
//        record.setName(name);
//        record.setOpenId(openId);
//        record.setStuas("1");
//
//        recordRepostitory.save(record);
    }

    @Override
    public List<Record> findAllByOpenId(String openId) {

        return recordRepostitory.findAllByOpenIdAndTimeGreaterThanEqual(openId,TimeUtils.todayMorning());
    }

    @Override
    public List<Record> findPxcPeopleOpenid() {

        return recordRepostitory.findAllByTimeGreaterThanEqualOrderByTimeDesc(TimeUtils.todayMorning());
    }

    @Override
    public Record findAllByOpenIdAndRuleIdAndStuas(String openId, String ruleId) {

        return recordRepostitory.findFirstByOpenIdAndRuleIdAndStuas(openId,ruleId,"0");
    }

    @Override
    public List<Record> findAllByOpenIdAndStuas(String openId) {
        return recordRepostitory.findAllByOpenIdAndStuas(openId,"0");
    }
}