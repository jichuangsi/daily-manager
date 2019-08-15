package com.jichuangsi.school.timingservice.entity;

import javax.persistence.*;

@Entity
@Table(name = "role_department")
public class RoleDepartment {
    @javax.persistence.Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    private String roleId;
    private String deptId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }
}
