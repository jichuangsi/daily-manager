package com.jichuangsi.school.timingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SchoolTimingserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchoolTimingserviceApplication.class, args);
    }
}
