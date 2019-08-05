package com.jichuangsi.school.timingservice.repository;

import com.jichuangsi.school.timingservice.entity.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
@Repository
public interface RuleRepostitory extends JpaRepository<Rule,String>{

    Rule findFirstByTimeGreaterThanEqual(long time);

    @Transactional
    @Modifying
    @Query(value = "insert into rule(time,wifi_name,longitude_latitude,stuas,wucha) values(?1,?2,?3,?4,?5)",nativeQuery = true)
    void insertRule(Long time, String wifiName, String longitudeLatitude, String stuas,String wucha);

    List<Rule> findAllByTimeGreaterThanEqual(long time);

    @Query(value = "SELECT * FROM rule  ORDER BY id DESC LIMIT 1",nativeQuery = true)
    Rule findLastRule();


    @Transactional
    @Modifying
    @Query(value = "UPDATE rule SET longitude_latitude=?5,stuas=?4,time=?3,wifi_name=?6,wucha=?1, WHERE id=?2",nativeQuery = true)
    int updateRule(String wucha, Integer id, long time, String stuas, String longitudeLatitude, String wifiName);
}
