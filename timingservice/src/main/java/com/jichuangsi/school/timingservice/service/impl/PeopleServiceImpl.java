package com.jichuangsi.school.timingservice.service.impl;

import com.jichuangsi.school.timingservice.entity.People;
import com.jichuangsi.school.timingservice.repository.PeopleRepostitory;
import com.jichuangsi.school.timingservice.service.PeopleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PeopleServiceImpl implements PeopleService {

    @Resource
    private PeopleRepostitory peopleRepostitory;


    @Override
    public String findPeopleName(String openId) {
        return  peopleRepostitory.findOneByOpenId(openId).getPeopleName();
    }

    @Override
    public People findPeople(String openId) {
        return peopleRepostitory.findOneByOpenId(openId);
    }

    @Override
    public List<People> findAllPeople(String name) {
        return peopleRepostitory.findByPeopleNameLike("%"+name+"%");
    }

    @Override
    public List<People> findAll() {
        return peopleRepostitory.findAll();
    }

    @Override
    public List<People> findForD(String d) {
        return peopleRepostitory.findAllByDepartment(d);
    }

    @Override
    public List<People> findAllPeople(String name, String deptId) {
        return peopleRepostitory.findByDepartmentAndPeopleNameLike(deptId,"%"+name+"%");
    }


}
