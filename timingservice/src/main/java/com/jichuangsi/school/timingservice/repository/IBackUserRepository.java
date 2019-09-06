package com.jichuangsi.school.timingservice.repository;

import com.jichuangsi.school.timingservice.entity.BackUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IBackUserRepository extends JpaRepository<BackUser,String> {
    BackUser findByAccountAndPwdAndStatus(String account, String pwd, String status);
    int countByAccount(String account);
    BackUser findByid(String id);
    BackUser findByWechat(String id);
    int countByDeptId(String deptId);
    int countByRoleId(String roleId);
    List<BackUser> findByRoleName(String roleName);
    List<BackUser> findByDeptId(String deptId);

}
