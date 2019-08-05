package com.jichuangsi.school.timingservice.service;

import com.jichuangsi.school.timingservice.entity.Rule;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
