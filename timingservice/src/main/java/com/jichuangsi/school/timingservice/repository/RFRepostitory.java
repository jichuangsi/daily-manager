package com.jichuangsi.school.timingservice.repository;

import com.jichuangsi.school.timingservice.entity.RuleFather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface RFRepostitory extends JpaRepository<RuleFather,String> {

    @Transactional
    @Modifying
    @Query(value = "truncate table rulefather",nativeQuery = true)
    public void truncateTable();

    @Transactional
    @Modifying
    @Query(value = "insert into rulefather(time,wifi_name,longitude_latitude,stuas,wucha,qiting,timestatus) values(?1,?2,?3,?4,?5,?6,?7)",nativeQuery = true)
    void insertRule(Long time, String wifiName, String longitudeLatitude, String stuas,String wucha,String qiting,String timestatus);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM rulefather WHERE id = ?1",nativeQuery = true)
    void deleteForId(String ruleFatherId);

    @Transactional
    @Query(value = "SELECT * FROM rulefather ORDER BY time",nativeQuery = true)
    List<RuleFather> findAllOrderBy();
}
