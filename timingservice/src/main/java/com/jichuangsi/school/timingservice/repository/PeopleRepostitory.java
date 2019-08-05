package com.jichuangsi.school.timingservice.repository;

import com.jichuangsi.school.timingservice.entity.People;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PeopleRepostitory extends JpaRepository<People,String>{
    People findFirstByPeopleName(String name);

    People findAllByOpenId(String openId);

    List<People> findAllByPeopleName(String name);
}
