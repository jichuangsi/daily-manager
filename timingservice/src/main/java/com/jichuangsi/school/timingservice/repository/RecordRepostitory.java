package com.jichuangsi.school.timingservice.repository;

import com.jichuangsi.school.timingservice.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface RecordRepostitory extends JpaRepository<Record,String>{
    @Transactional
    @Modifying
    @Query(value = "insert into record(longitude_latitude,name,stuas,time,wifi_name,open_id,rule_id) values(?1,?2,?3,?4,?5,?6,?7)",nativeQuery = true)
    void insertRecord(String longitudeLatitude,String name, String stuas,long time, String wifiName,String openId,String ruleId);

    List<Record> findAllByOpenIdAndTimeGreaterThanEqual(String openId,long time);

    List<Record> findAllByTimeGreaterThanEqualOrderByTime(long time);

    Record findFirstByOpenIdAndRuleIdAndStuas(String openId,String ruleId,String stuas);

    List<Record> findAllByOpenIdAndStuas(String openId, String stuas);

    Record findFirstByOpenIdAndRuleIdAndStuasOrStuasOrStuas(String openId, String ruleId, String s, String s1, String s2);


    List<Record> findFirstByOpenIdAndRuleIdAndStuasOrStuas(String openId, String ruleId, String Stuas, String Stuas1);

    @Query(value = "SELECT * FROM record WHERE `open_id`=?1 AND `rule_id`=?2 AND (`stuas`=?3 OR `stuas`=?4)",nativeQuery = true)
    List<Record> findFirstByOpenIdAndRuleIdAndStuasOrOpenIdAndRuleIdAndStuasOrderByStuas(String openId1, String ruleId1, String s, String s1);

    List<Record> findAllByOpenIdAndStuasAndTimeBetween(String openId,String stuas, long timeStart, long timeEnd);

    List<Record> findAllByOpenIdAndTimeBetween(String openId, long timeStart, long timeEnd);

    List<Record> findAllByOpenIdAndStuasAndTimeBetweenAndRuleId(String openId,String stuas, long timeStart, long timeEnd,String ruleId);

    void deleteByOpenId(String opendId);

    int countByOpenIdInAndStuasAndTimeBetween(List<String> openIds,String stuas, long timeStart, long timeEnd);
}
