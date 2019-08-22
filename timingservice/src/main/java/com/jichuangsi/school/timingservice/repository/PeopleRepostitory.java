package com.jichuangsi.school.timingservice.repository;

import com.jichuangsi.school.timingservice.entity.People;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface PeopleRepostitory extends JpaRepository<People,String>{
    People findFirstByPeopleName(String name);

    People findOneByOpenId(String openId);

    List<People> findByPeopleNameLike(String name);

    @Transactional
    @Modifying
    @Query(value = "UPDATE user SET department=?1 WHERE open_id=?2",nativeQuery = true)
    int updateDPMTforOPENID(String deptId, String wechat);

    @Transactional
    @Modifying
    @Query(value = "UPDATE user SET jurisdiction=?1 WHERE open_id=?2",nativeQuery = true)
    int updateJSQXforOPENID(String jurisdiction, String wechat);


    List<People> findAllByDepartment(String d);

    List<People> findByDepartmentAndPeopleNameLike(String deptId,String name);
}
