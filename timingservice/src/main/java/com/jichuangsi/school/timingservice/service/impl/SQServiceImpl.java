package com.jichuangsi.school.timingservice.service.impl;

import com.jichuangsi.school.timingservice.entity.Img;
import com.jichuangsi.school.timingservice.entity.SQFlie;
import com.jichuangsi.school.timingservice.repository.ImgRepostitory;
import com.jichuangsi.school.timingservice.repository.SQRepostitory;
import com.jichuangsi.school.timingservice.service.SQService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service

public class SQServiceImpl implements SQService{

    @Resource
    private ImgRepostitory imgRepostitory;

    @Resource
    private SQRepostitory sqRepostitory;


    @Override
    public void saveFile(String openId,String uuid,String ruleId,String msg) {
        long nowTime =System.currentTimeMillis();
        sqRepostitory.insertFile(openId,uuid,ruleId,nowTime,msg,"0");
    }

    @Override
    public List<SQFlie> getFile(String openId) {
        List<SQFlie> allByOpenIdAndRuleId = sqRepostitory.findAllByOpenId(openId);

        return allByOpenIdAndRuleId;
    }

    @Override
    public void saveImg(byte[] file, String uuid) {
        imgRepostitory.insertRecord(uuid,file);
    }

    @Override
    public List<Img> getImgList(String uuid) {

        return imgRepostitory.findAllByUuid(uuid);
    }

    @Override
    public List<SQFlie> getFile() {
        return sqRepostitory.findAllByStuas("0");
    }

    @Override
    public SQFlie getSQ(String openId, String ruleId) {
        return sqRepostitory.findFirstByOpenIdAndRuleId(openId,ruleId);
    }

    @Override
    public List<SQFlie> getAllUnapproved() {
        return sqRepostitory.findAllByStuas("0");
    }

    @Override
    public List<SQFlie> getAllapproved() {
        return sqRepostitory.findAllByStuasOrStuas("1","2");
    }

    @Override
    public List<SQFlie> getUnapprovedSQForName(String openId) {
        return sqRepostitory.findAllByOpenIdAndStuas(openId,"0");
    }

    @Override
    public List<SQFlie> getapprovedSQForName(String openId) {
        return sqRepostitory.findAllByOpenIdAndStuasOrStuas(openId,"1","2");
    }

    @Override
    public List<SQFlie> getAllsq() {
        return sqRepostitory.findAll();
    }

    @Override
    public List<SQFlie> getSQ2(String openId) {
        return sqRepostitory.findAllByOpenId(openId);
    }

    @Override
    public void SSSH(SQFlie sqFlie) {
        sqRepostitory.save(sqFlie);
    }
}
