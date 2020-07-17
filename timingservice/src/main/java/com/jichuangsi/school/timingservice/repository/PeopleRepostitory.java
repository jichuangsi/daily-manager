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

    People findOneByOpenIdAndIsDelete(String openId,String isdelete);

    List<People> findByPeopleNameLikeAndIsDelete(String name,String status);

    List<People> findAllByPeopleNameLikeAndIsDeleteAndDepartmentAndJurisdiction(String name,String status,String deptname,String jurisdiction);

    List<People> findAllByIsDeleteAndDepartmentAndJurisdictionLike(String status,String deptname,String jurisdiction);

    @Transactional
    @Modifying
    @Query(value = "UPDATE user SET department=?1 WHERE open_id=?2",nativeQuery = true)
    int updateDPMTforOPENID(String deptId, String wechat);

    @Transactional
    @Modifying
    @Query(value = "UPDATE user SET jurisdiction=?1 WHERE open_id=?2",nativeQuery = true)
    int updateJSQXforOPENID(String jurisdiction, String wechat);

    @Transactional
    @Modifying
    @Query(value = "UPDATE user SET is_delete=?1 WHERE open_id=?2",nativeQuery = true)
    int updateStatus(String status,String opendId);

    List<People> findAllByDepartmentAndIsDelete(String d,String status);

    List<People> findByDepartmentAndPeopleNameLikeAndIsDelete(String deptId,String name,String status);

    @Transactional
    void deleteByOpenId(String opendId);

    List<People> findAllByIsDelete(String status);
}
