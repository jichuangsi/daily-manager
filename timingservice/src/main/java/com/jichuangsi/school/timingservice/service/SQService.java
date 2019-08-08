package com.jichuangsi.school.timingservice.service;

import com.jichuangsi.school.timingservice.entity.Img;
import com.jichuangsi.school.timingservice.entity.SQFlie;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface SQService {
    void saveFile(String openId, String uuid,String ruleId,String msg);
    List<SQFlie> getFile(String openId);

    void saveImg(byte[] file, String uuid);

    List<Img> getImgList(String uuid);

    List<SQFlie> getFile();

    SQFlie getSQ(String openId, String ruleId);

    List<SQFlie> getAllUnapproved();

    List<SQFlie> getAllapproved();

    List<SQFlie> getUnapprovedSQForName(String openId);

    List<SQFlie> getapprovedSQForName(String openId);

    List<SQFlie> getAllsq();

    List<SQFlie> getSQ2(String openId);
}
