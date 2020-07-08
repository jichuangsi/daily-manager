package com.jichuangsi.school.timingservice.entity;

import javax.persistence.*;

@Entity
@Table(name = "rulefather")
public class RuleFather {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer Id;
    private long time;
    private String wifiName;
    private String longitudeLatitude;
    private String wucha;
    private String stuas;
    private String qiting;
    private String timestatus;

    public String getQiting() {
        return qiting;
    }

    public void setQiting(String qiting) {
        this.qiting = qiting;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getWifiName() {
        return wifiName;
    }

    public void setWifiName(String wifiName) {
        this.wifiName = wifiName;
    }

    public String getLongitudeLatitude() {
        return longitudeLatitude;
    }

    public void setLongitudeLatitude(String longitudeLatitude) {
        this.longitudeLatitude = longitudeLatitude;
    }

    public String getWucha() {
        return wucha;
    }

    public void setWucha(String wucha) {
        this.wucha = wucha;
    }

    public String getStuas() {
        return stuas;
    }

    public void setStuas(String stuas) {
        this.stuas = stuas;
    }

    public String getTimestatus() {
        return timestatus;
    }

    public void setTimestatus(String timestatus) {
        this.timestatus = timestatus;
    }
}
