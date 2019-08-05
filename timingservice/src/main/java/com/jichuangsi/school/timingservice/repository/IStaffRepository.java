package com.jichuangsi.school.timingservice.repository;

import com.jichuangsi.school.timingservice.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.xml.soap.SAAJResult;
import java.util.List;

public interface IStaffRepository extends JpaRepository<Staff,String>,PagingAndSortingRepository<Staff,String>,JpaSpecificationExecutor<Staff> {
    Staff findByWechat(String opendId);
    int countByAccount(String account);
}
