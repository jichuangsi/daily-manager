package com.jichuangsi.school.timingservice.job;

import com.jichuangsi.school.timingservice.service.RuleService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

@Component
public class RuleJob {

    @Resource
    private RuleService ruleService;

//    @Scheduled(cron="0 0 0 ? * MON-FRI")
   @Scheduled(cron="0 0 0 * * ? ")

    private void process(){
            ruleService.copyRlueModel();
       System.out.println(new Date().toString());
       ArrayList<String> strings = new ArrayList<>();

   }

}
