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
    @Transactional
    @Modifying
    @Query(value = "insert into ol(open_id,stuas,msg,start,end,time,stuas2) values(?1,?2,?3,?4,?5,?6,?7)",nativeQuery = true)
    void insertRecord(String openId, String stuas, String msg,long start,long end,long time,String stuas2);

    List<Overtimeleave> findAllByOpenId(String openId);

    List<Overtimeleave> findAllByStuas(String stuas);

    List<Overtimeleave> findAllByStuasAndStuas(String stuas, String stuas2);

    List<Overtimeleave> findAllByOpenIdAndStuas(String openId, String stuas);

    List<Overtimeleave> findAllByOpenIdAndStuasAndStuas(String openId, String stuas, String stuas1);
}
