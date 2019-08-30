package com.jichuangsi.school.timingservice.dao.mapper;

import com.jichuangsi.school.timingservice.model.ReportFormModel2;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

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

    @Select("<script>SELECT d.deptname as department,u.people_name as peopleName,u.jurisdiction as jurisdiction,rule.stuas as stuas,r.stuas as stuas2,rule.time as time,r.time as chockinTime FROM record r INNER JOIN rule ON rule.id=r.rule_id INNER JOIN `user` u ON r.open_id=u.open_id INNER JOIN `department` d ON u.department=d.id WHERE u.open_id=#{openId} AND r.time BETWEEN #{timeStart} AND #{timeEnd}</script>")
    List<ReportFormModel2> findAllByTimeAndOpenId(@Param("openId")String openId,@Param("timeStart")long timeStart, @Param("timeEnd")long timeEnd);
}

