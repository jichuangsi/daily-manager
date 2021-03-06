package com.jichuangsi.school.timingservice.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "rule")
public class Rule {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private long time;
    private String wifiName;
    private String longitudeLatitude;
    private String wucha;
    private String stuas;
    private String timestatus;

    public String getWucha() {
        return wucha;
    }

    public void setWucha(String wucha) {
        this.wucha = wucha;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
