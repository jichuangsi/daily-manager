package com.jichuangsi.school.timingservice.service.impl;

import com.jichuangsi.school.timingservice.entity.Rule;
import com.jichuangsi.school.timingservice.entity.RuleFather;
import com.jichuangsi.school.timingservice.repository.RFRepostitory;
import com.jichuangsi.school.timingservice.repository.RuleRepostitory;
import com.jichuangsi.school.timingservice.service.RuleService;
import com.jichuangsi.school.timingservice.utils.TimeUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class RuleServiceImpl implements RuleService {

    @Resource
    private RuleRepostitory ruleRepostitory;

    @Resource
    private RFRepostitory rfRepostitory;

    @Override
    public String findWifi() {
        return  ruleRepostitory.findFirstByTimeGreaterThanEqual(TimeUtils.todayMorning()).getWifiName();
    }

    @Override
    public String findLL() {
        return ruleRepostitory.findFirstByTimeGreaterThanEqual(TimeUtils.todayMorning()).getLongitudeLatitude();
    }

    @Override
    public void insertRule(Long time, String wifiName, String longitudeLatitude, String stuas,String wucha) {
        rfRepostitory.insertRule(time,wifiName,longitudeLatitude,stuas,wucha,"1");
    }

    @Override
    public List<Rule> getRulelist() {
       return ruleRepostitory.findAllByTimeGreaterThanEqual(TimeUtils.todayMorning());
    }

    @Override
    public Rule getRule() {
        long nowTime =System.currentTimeMillis();
        return ruleRepostitory.findFirstByTimeGreaterThanEqual(nowTime);
    }

    @Override
    public Rule getLastRule() {

        return ruleRepostitory.findLastRule();
    }

    @Override
    public void cleanFather() {
        rfRepostitory.truncateTable();
    }

    @Override
    public void copyRlueModel() {
        List<RuleFather> all = rfRepostitory.findAll();
        ;
        for (RuleFather rf:all
             ) {
            long time= rf.getTime()+TimeUtils.todayMorning();
            ruleRepostitory.insertRule(time,rf.getWifiName(),rf.getLongitudeLatitude(),rf.getStuas(),rf.getWucha());
        }
    }

    @Override
    public void updateRule(Rule rule) {
        ruleRepostitory.updateRule(rule.getWucha(),rule.getId(),rule.getTime(),rule.getStuas(),rule.getLongitudeLatitude(),rule.getWifiName() );
    }

    @Override
    public List<Rule> getRuleForTime(long timeStart, long timeEnd) {
        return ruleRepostitory.findAllByTimeBetween(timeStart,timeEnd);
    }

    @Override
    public List<Rule> getRulelistForTime() {
        long nowTime =System.currentTimeMillis();
        return ruleRepostitory.findAllByTimeBetween(TimeUtils.todayMorning(),nowTime);
    }

    @Override
    public void delRule(String ruleFatherId) {
        rfRepostitory.deleteForId(ruleFatherId);
    }

    @Override
    public List<RuleFather> getRuleFatherList() {
        return rfRepostitory.findAll();
    }

    @Override
    public Rule getRuleById(Integer ruleId) {
        return ruleRepostitory.findFirstById(ruleId);
    }

    @Override
    public Rule insertRule(Rule rule) {
        return ruleRepostitory.save(rule);
    }

    @Override
    public void ruleFatherStopAndStart(RuleFather ruleFather) {
        rfRepostitory.save(ruleFather);
    }


}
