package com.jichuangsi.school.timingservice.service;

import com.jichuangsi.school.timingservice.entity.People;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface PeopleService {
     String findPeopleName(String name) ;

    People findPeople(String openId);

    List<People> findAllPeople(String name);

    List<People> findAll();

    List<People> findForD(String d);

    List<People> findAllPeople(String name,String deptId);
}
