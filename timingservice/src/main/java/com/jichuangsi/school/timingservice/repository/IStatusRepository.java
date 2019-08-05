package com.jichuangsi.school.timingservice.repository;

import com.jichuangsi.school.timingservice.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IStatusRepository extends JpaRepository<Status,String>{
    Status findByName(String name);
}
