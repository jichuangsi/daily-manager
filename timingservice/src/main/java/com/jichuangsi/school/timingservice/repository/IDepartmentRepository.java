package com.jichuangsi.school.timingservice.repository;

import com.jichuangsi.school.timingservice.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDepartmentRepository extends JpaRepository<Department,String>{
    Department findByid(String id);
}
