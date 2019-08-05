package com.jichuangsi.school.timingservice.model;

public class RuleModel {
    private Integer Id;
    private long time;
    private String wifiName;
    private String longitudeLatitude;
    private String stuas;
    private String stuas2="1";

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

    public String getStuas() {
        return stuas;
    }

    public void setStuas(String stuas) {
        this.stuas = stuas;
    }

    public String getStuas2() {
        return stuas2;
    }

    public void setStuas2(String stuas2) {
        this.stuas2 = stuas2;
    }
}
