package com.jichuangsi.school.timingservice.repository;

import com.jichuangsi.school.timingservice.entity.SQFlie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
@Repository
public interface SQRepostitory extends JpaRepository<SQFlie,String> {
    @Transactional
    @Modifying
    @Query(value = "insert into sqfile(open_id,uuid,rule_id,time,msg,stuas) values(?1,?2,?3,?4,?5,?6)",nativeQuery = true)
    void insertFile(String openId, String uuid,String ruleId,long time,String msg,String stuas);

    List<SQFlie> findAllByOpenIdAndRuleId(String openId,String ruleId);

    List<SQFlie> findAllByOpenId(String openId);

    List<SQFlie> findAllByStuas(String stuas);

    SQFlie findFirstByOpenIdAndRuleId(String openId,String ruleId);


    List<SQFlie> findAllByStuasAndStuas(String stuas,String stuas2);

    List<SQFlie> findAllByOpenIdAndStuas(String openId, String stuas);

    List<SQFlie> findAllByOpenIdAndStuasAndStuas(String openId, String stuas, String stuas1);
}
