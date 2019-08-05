package com.jichuangsi.school.timingservice.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "department")
@GenericGenerator(name = "jpa-uuid",strategy = "uuid")
public class Department {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;
    private String deptname;//部门名称
    private String deptdescribe;//部门描述

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }

    public String getDeptdescribe() {
        return deptdescribe;
    }

    public void setDeptdescribe(String deptdescribe) {
        this.deptdescribe = deptdescribe;
    }
}
