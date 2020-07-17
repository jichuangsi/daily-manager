package com.jichuangsi.school.timingservice.repository;

import com.jichuangsi.school.timingservice.entity.Overtimeleave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface OLRepostitory extends JpaRepository<Overtimeleave,String> {


    List<Overtimeleave> findAllByOpenId(String openId);

    List<Overtimeleave> findAllByStuas2AndOpenIdIn(String stuas,List<String> opids);

    List<Overtimeleave> findAllByStuas2AndStuas2(String stuas, String stuas2);

    List<Overtimeleave> findAllByOpenIdAndStuas2(String openId, String stuas);

    List<Overtimeleave> findAllByOpenIdAndStuas2AndStuas2(String openId, String stuas, String stuas1);

    List<Overtimeleave> findAllByStuas2OrStuas2AndOpenIdIn(String s, String s1,List<String> opids);

    List<Overtimeleave> findAllByOpenIdOrStuas2(String openId, String s);

    List<Overtimeleave> findAllByOpenIdAndStuas2OrStuas2(String openId, String s, String s1);

    List<Overtimeleave> findAllByOpenIdIn(List<String> opids);

    @Transactional
    @Modifying
    @Query(value = "insert into ol(open_id,stuas,msg,start,end,time,stuas2) values(?1,?2,?3,?4,?5,?6,?7)",nativeQuery = true)
    void insertRecord(String openId, String stuas, String msg,long start,long end,long time,String stuas2);

    @Transactional
    @Modifying
    @Query(value = "insert into ol(open_id,stuas,msg,start,end,stuas2) values(?1,?2,?3,?4,?5,?6)",nativeQuery = true)
    void insertRecord2(String openId, String stuas, String msg,long start,long end,String stuas2);

    void deleteByOpenId(String opendId);
}
