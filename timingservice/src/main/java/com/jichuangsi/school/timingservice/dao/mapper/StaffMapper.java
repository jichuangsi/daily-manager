package com.jichuangsi.school.timingservice.dao.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface StaffMapper {
    //修改状态
    @Update("<script>UPDATE staff SET `statusid`=#{statusId} WHERE `wechat`=#{wechat}</script>")
    void updateStatusById(@Param("wechat") String wechat, @Param("statusId") String statusId);
    //修改部门
    @Update("<script>UPDATE staff SET `deptid`=#{deptId} WHERE `wechat`=#{wechat}</script>")
    void updateDepartmentById(@Param("wechat") String wechat, @Param("deptId") String deptId);
    //修改角色
    @Update("<script>UPDATE staff SET `roleid`=#{roleId} WHERE `wechat`=#{wechat}</script>")
    void updateRoleById(@Param("wechat") String wechat, @Param("roleId") String roleId);

    @Select("<script>select count(1) from staff where deptid=#{deptid}</script>")
    int countStaffByDeptid(String deptId);
    @Select("<script>select count(1) from staff where roleid=#{roleId}</script>")
    int countStaffByRoleid(String roleId);
}

