package com.jichuangsi.school.timingservice.service;

import com.jichuangsi.school.timingservice.entity.Overtimeleave;

import java.util.List;

public interface OLService {
    void AOL(String openId, String stuas, String msg,long start,long end);

    List<Overtimeleave> getOLList(String openId);

    List<Overtimeleave> getOLList1();
    List<Overtimeleave> getOLList2();


    List<Overtimeleave> getOLForOpenId1(String openId);

    List<Overtimeleave> getOLForOpenId2(String openId);

    List<Overtimeleave> getAll();

    List<Overtimeleave> getAllOL(String openId);

    void AOLSH(Overtimeleave overtimeleave);
}
