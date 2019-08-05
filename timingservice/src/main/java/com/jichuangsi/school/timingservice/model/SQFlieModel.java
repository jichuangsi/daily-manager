package com.jichuangsi.school.timingservice.model;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public class SQFlieModel {
    private Integer Id;
    private String openId;
    private String ruleId;
    private List<File> file;
    private long time;
    private String msg;

    public List<File> getFile() {
        return file;
    }

    public void setFile(List<File> file) {
        this.file = file;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }





    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }


}
