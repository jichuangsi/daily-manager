package com.jichuangsi.school.timingservice.model;

import com.jichuangsi.school.timingservice.entity.Department;
import com.jichuangsi.school.timingservice.entity.Role;

import java.util.List;

public class LoginElementModel {
    private String resultCode;
    private List<Department> departmentList;//部门信息
    private List<Role> roleList;//角色信息
    private String accessToken;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public List<Department> getDepartmentList() {
        return departmentList;
    }

    public void setDepartmentList(List<Department> departmentList) {
        this.departmentList = departmentList;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
