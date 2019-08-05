package com.jichuangsi.school.timingservice.commons;

import com.jichuangsi.school.timingservice.service.BackUserService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
public class InitializationService {
    @Resource
    private BackUserService backUserService;

    @PostConstruct
    public void insertSuperMan(){
        try {
            backUserService.insertSuperMan();
        } catch (Exception e) {
        }
    }
}
