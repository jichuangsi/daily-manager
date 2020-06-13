package com.jichuangsi.school.timingservice.service;

import com.jichuangsi.school.timingservice.entity.Rule;
import com.jichuangsi.school.timingservice.entity.RuleFather;

import java.util.List;


public interface RuleService {

    String findWifi();

    String findLL();

    void insertRule(Long time, String wifiName, String longitudeLatitude, String stuas,String wucha);

    List<Rule> getRulelist();

    Rule getRule();

    Rule getLastRule();

    void cleanFather();

    void copyRlueModel();

    void updateRule(Rule rule);

    List<Rule> getRuleForTime(long timeStart, long timeEnd);

    List<Rule> getRulelistForTime();

    void delRule(String ruleFatherId);

    List<RuleFather> getRuleFatherList();

    Rule getRuleById(Integer Id);

    Rule insertRule(Rule rule);

    void ruleFatherStopAndStart(RuleFather ruleFather);

    void delRule2(String ruleid);

    List<Rule> getRuleForTime(long timeStart, long timeEnd,String status);
}
