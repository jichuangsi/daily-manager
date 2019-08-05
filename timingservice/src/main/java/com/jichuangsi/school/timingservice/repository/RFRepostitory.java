package com.jichuangsi.school.timingservice.repository;

import com.jichuangsi.school.timingservice.entity.RuleFather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface RFRepostitory extends JpaRepository<RuleFather,String> {

    @Transactional
    @Modifying
    @Query(value = "truncate table rulefather",nativeQuery = true)
    public void truncateTable();

    @Transactional
    @Modifying
    @Query(value = "insert into rulefather(time,wifi_name,longitude_latitude,stuas,wucha) values(?1,?2,?3,?4,?5)",nativeQuery = true)
    void insertRule(Long time, String wifiName, String longitudeLatitude, String stuas,String wucha);
}
