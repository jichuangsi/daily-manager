package com.jichuangsi.school.timingservice.job;

import com.jichuangsi.school.timingservice.service.RuleService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Component
public class RuleJob {

    @Resource
    private RuleService ruleService;

//    @Scheduled(cron="0 0 0 ? * MON-FRI")
//   @Scheduled(cron="* * * ? * MON-FRI")
   @Scheduled(cron="0 5 * ? * MON-FRI")
    private void process(){
            ruleService.copyRlueModel();
       System.out.println(new Date().toString());
    }

}
