package com.jichuangsi.school.timingservice.service.impl;

import com.jichuangsi.school.timingservice.entity.Overtimeleave;
import com.jichuangsi.school.timingservice.repository.OLRepostitory;
import com.jichuangsi.school.timingservice.service.OLService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OLServiceImpl implements OLService {
    @Resource
    private OLRepostitory olRepostitory;


    @Override
    public void AOL(String openId, String stuas, String msg,long start,long end) {
        long nowTime =System.currentTimeMillis();
        olRepostitory.insertRecord(openId,stuas,msg,start,end,nowTime,"0");
    }

    @Override
    public List<Overtimeleave> getOLList(String openId) {
        return olRepostitory.findAllByOpenId(openId);
    }

    @Override
    public List<Overtimeleave> getOLList1() {
        return olRepostitory.findAllByStuas2("0");
    }

    @Override
    public List<Overtimeleave> getOLList2() {
        return  olRepostitory.findAllByStuas2OrStuas2("1","2");
    }

    @Override
    public List<Overtimeleave> getOLForOpenId1(String openId) {
        return olRepostitory.findAllByOpenIdAndStuas2(openId,"0");
    }

    @Override
    public List<Overtimeleave> getOLForOpenId2(String openId) {
        return olRepostitory.findAllByOpenIdAndStuas2OrStuas2(openId,"1","2");
    }

    @Override
    public List<Overtimeleave> getAll() {
        return olRepostitory.findAll();
    }

    @Override
    public List<Overtimeleave> getAllOL(String openId) {
        return olRepostitory.findAllByOpenId(openId);
    }
}
