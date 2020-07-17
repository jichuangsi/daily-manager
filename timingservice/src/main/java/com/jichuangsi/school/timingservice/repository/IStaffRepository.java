package com.jichuangsi.school.timingservice.repository;

import com.jichuangsi.school.timingservice.entity.Department;
import com.jichuangsi.school.timingservice.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;
import javax.xml.soap.SAAJResult;
import java.util.List;

public interface IStaffRepository extends JpaRepository<Staff,String>,PagingAndSortingRepository<Staff,String>,JpaSpecificationExecutor<Staff> {
    Staff findByWechatAndIsDelete(String opendId,String status);
    Staff findByWechat(String opendId);
    int countByAccount(String account);
    List<Staff> findAllByDepartmentAndIsDelete(Department department,String status);

    @Transactional
    @Modifying
    @Query(value = "UPDATE staff SET is_delete=?1 WHERE wechat=?2",nativeQuery = true)
    void updateStaffStatus(String status,String openId);

    List<Staff> findAllByIsDelete(String status);
}
