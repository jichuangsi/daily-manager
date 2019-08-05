package com.jichuangsi.school.timingservice.job;

import com.jichuangsi.school.timingservice.service.RuleService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RuleJob {

    @Resource
    private RuleService ruleService;

    @Scheduled(cron="0/3 * * * * ?")
    private void process(){
            ruleService.copyRlueModel();
    }

}
