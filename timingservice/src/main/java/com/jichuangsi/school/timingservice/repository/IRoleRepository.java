package com.jichuangsi.school.timingservice.repository;

import com.jichuangsi.school.timingservice.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoleRepository extends JpaRepository<Role,String>{
    Role findByid(String id);
    Role findByRolename(String name);
}
