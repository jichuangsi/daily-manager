package com.jichuangsi.school.timingservice.model;

import com.jichuangsi.school.timingservice.entity.Overtimeleave;
import com.jichuangsi.school.timingservice.entity.People;

public class OvertimeleaveModel {
    private String department;
    private String peopleName;
    private String jurisdiction;
    private Overtimeleave overtimeleave;

    public Overtimeleave getOvertimeleave() {
        return overtimeleave;
    }

    public void setOvertimeleave(Overtimeleave overtimeleave) {
        this.overtimeleave = overtimeleave;
    }

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
}
