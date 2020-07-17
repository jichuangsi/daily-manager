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
        return  peopleRepostitory.findOneByOpenIdAndIsDelete(openId,"0").getPeopleName();
    }

    @Override
    public People findPeople(String openId) {
        return peopleRepostitory.findOneByOpenIdAndIsDelete(openId,"0");
    }

    @Override
    public List<People> findAllPeopleByStatus(String name,String status) {
        return peopleRepostitory.findByPeopleNameLikeAndIsDelete("%"+name+"%",status);
    }

    @Override
    public List<People> findAllByStatus(String status) {
        return peopleRepostitory.findAllByIsDelete(status);
    }

    @Override
    public List<People> findForDAndStatus(String d,String status) {
        return peopleRepostitory.findAllByDepartmentAndIsDelete(d,status);
    }

    @Override
    public List<People> findAllPeopleByStatus(String name, String deptId,String status) {
        return peopleRepostitory.findByDepartmentAndPeopleNameLikeAndIsDelete(deptId,"%"+name+"%",status);
    }


}
