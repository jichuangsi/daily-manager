package com.jichuangsi.school.timingservice.dao.mapper;

import com.jichuangsi.school.timingservice.entity.Overtimeleave;
import com.jichuangsi.school.timingservice.entity.Record;
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

    @Select("<script>select count(1) from staff where deptid=#{deptid} and is_delete='0'</script>")
    int countStaffByDeptid(String deptId);
    @Select("<script>select count(1) from staff where roleid=#{roleId} and is_delete='0</script>")
    int countStaffByRoleid(String roleId);

    @Select("<script>SELECT d.deptname as department,u.people_name as peopleName,u.jurisdiction as jurisdiction,rule.stuas as stuas,r.stuas as stuas2,rule.time as time,r.time as chockinTime,rule.timestatus as timestatus FROM record r INNER JOIN rule ON rule.id=r.rule_id INNER JOIN `user` u ON r.open_id=u.open_id INNER JOIN `department` d ON u.department=d.id WHERE u.open_id=#{openId}  AND r.time BETWEEN #{timeStart} AND #{timeEnd}</script>")
    List<ReportFormModel2> findAllByTimeAndOpenId(@Param("openId")String openId,@Param("timeStart")long timeStart, @Param("timeEnd")long timeEnd);



    @Select("<script>SELECT d.deptname as department,u.people_name as peopleName,u.jurisdiction as jurisdiction,rule.stuas as stuas,r.stuas as stuas2,rule.time as time,r.time as chockinTime \n" +
            "FROM record r INNER JOIN rule ON rule.id=r.rule_id INNER JOIN `user` u ON r.open_id=u.open_id INNER JOIN `department` d ON u.department=d.id \n" +
            "WHERE r.open_id=#{openId} AND r.rule_id=#{ruleId}\n" +
            "ORDER BY r.time desc\n" +
            "LIMIT 0,1</script>")
    ReportFormModel2 findAllByOpenIdAndRuleId(@Param("openId")String openId,@Param("ruleId")String ruleId);

    /**
     * 统计打卡人数
     * @param ruleId
     * @param deptId
     * @return
     */
    @Select(value = "<script>SELECT  record.* FROM record INNER JOIN staff ON record.open_id=staff.wechat WHERE rule_id IN" +
            "<foreach collection='ruleId' item='item' open='(' separator=',' close=')'>#{item}</foreach> AND staff.deptid=#{deptId} AND staff.is_delete='0' GROUP BY rule_id,open_id </script>")
    List<Record> getRuleIdAndOpenId(@Param("ruleId")List<Integer> ruleId,@Param("deptId")String deptId);

    /**
     * 根据状态统计考勤人数
     * @param ruleId
     * @param deptId
     * @param status
     * @return
     */
    @Select(value = "<script>SELECT  record.* FROM record INNER JOIN staff ON record.open_id=staff.wechat WHERE rule_id IN" +
            "<foreach collection='ruleId' item='item' open='(' separator=',' close=')'>#{item}</foreach> AND staff.deptid=#{deptId}  AND staff.is_delete='0' AND record.stuas=#{status} " +
            "GROUP BY rule_id,open_id </script>")
    List<Record> getRuleIdAndOpenIdAndStatus(@Param("ruleId")List<Integer> ruleId,@Param("deptId")String deptId,@Param("status")String status);

    /**
     * 根据时间部门查询请假信息
     * @param deptId
     * @param timeStart
     * @param timeEnd
     * @return
     */
    @Select(value = "<script>SELECT ol.* FROM ol LEFT JOIN staff s ON ol.open_id=s.wechat WHERE s.deptid=#{deptId} AND s.is_delete='0' AND ol.stuas=2 AND ol.time BETWEEN #{timeStart} AND #{timeEnd}</script>")
    List<Overtimeleave> getListByDeptIdAndTime(@Param("deptId")String deptId,@Param("timeStart")long timeStart, @Param("timeEnd")long timeEnd);
}

