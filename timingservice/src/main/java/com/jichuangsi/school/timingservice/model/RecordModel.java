package com.jichuangsi.school.timingservice.model;

import com.jichuangsi.school.timingservice.entity.People;

public class RecordModel {
    private People people;
    private long time;

    public People getPeople() {
        return people;
    }

    public void setPeople(People people) {
        this.people = people;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
