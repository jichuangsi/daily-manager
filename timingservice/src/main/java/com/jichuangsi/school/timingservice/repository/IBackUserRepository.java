package com.jichuangsi.school.timingservice.repository;

import com.jichuangsi.school.timingservice.entity.BackUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface IBackUserRepository extends JpaRepository<BackUser,String> {
    BackUser findByAccountAndPwdAndStatus(String account, String pwd, String status);
    int countByAccount(String account);
    BackUser findByid(String id);
    BackUser findByWechat(String id);
    int countByDeptId(String deptId);
    int countByRoleId(String roleId);
    List<BackUser> findByRoleIdAndStatus(String roleId,String status);
    List<BackUser> findByDeptId(String deptId);
    void deleteByWechat(String wechat);
    @Transactional
    @Modifying
    @Query(value = "UPDATE backuser SET status=?1 WHERE wechat=?2",nativeQuery = true)
    void updateStatus(String status,String opendId);
    @Transactional
    @Modifying
    @Query(value = "UPDATE backuser SET role_name=?2 WHERE role_id=?1",nativeQuery = true)
    void updateBackUserRole(String roleId,String roleName);
}
