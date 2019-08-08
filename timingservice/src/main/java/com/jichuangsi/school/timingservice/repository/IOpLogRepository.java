package com.jichuangsi.school.timingservice.repository;

import com.jichuangsi.school.timingservice.entity.OpLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IOpLogRepository extends JpaRepository<OpLog,String>,PagingAndSortingRepository<OpLog,String>,JpaSpecificationExecutor<OpLog> {

}
