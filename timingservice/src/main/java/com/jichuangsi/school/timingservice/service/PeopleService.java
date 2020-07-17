package com.jichuangsi.school.timingservice.service;

import com.jichuangsi.school.timingservice.entity.People;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface PeopleService {
     String findPeopleName(String name) ;

    People findPeople(String openId);

    List<People> findAllPeopleByStatus(String name,String status);

    List<People> findAllByStatus(String status);

    List<People> findForDAndStatus(String d,String status);

    List<People> findAllPeopleByStatus(String name,String deptId,String status);
}
