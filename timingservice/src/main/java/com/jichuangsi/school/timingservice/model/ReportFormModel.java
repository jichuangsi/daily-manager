package com.jichuangsi.school.timingservice.model;

public class ReportFormModel {
    private String department;
    private String peopleName;
    private String jurisdiction;
    private int kq;
    private int qq;

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPeopleName() {
        return peopleName;
    }

    public void setPeopleName(String peopleName) {
        this.peopleName = peopleName;
    }

    public String getJurisdiction() {
        return jurisdiction;
    }

    public void setJurisdiction(String jurisdiction) {
        this.jurisdiction = jurisdiction;
    }

    public int getKq() {
        return kq;
    }

    public void setKq(int kq) {
        this.kq = kq;
    }

    public int getQq() {
        return qq;
    }

    public void setQq(int qq) {
        this.qq = qq;
    }
}
