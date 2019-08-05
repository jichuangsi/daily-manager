package com.jichuangsi.school.timingservice.repository;

import com.jichuangsi.school.timingservice.entity.BackUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBackUserRepository extends JpaRepository<BackUser,String> {
    BackUser findByAccountAndPwdAndStatus(String account, String pwd, String status);
    int countByAccount(String account);
    BackUser findByid(String id);
    int countByDeptId(String deptId);
    int countByRoleId(String roleId);
}
