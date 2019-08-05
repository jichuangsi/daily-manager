package com.jichuangsi.school.timingservice.repository;

import com.jichuangsi.school.timingservice.entity.OpLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOpLogRepository extends JpaRepository<OpLog,String> {
}
