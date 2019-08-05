package com.jichuangsi.school.timingservice.repository;

import com.jichuangsi.school.timingservice.entity.RoleUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IRoleUrlRepository extends JpaRepository<RoleUrl,String>,PagingAndSortingRepository<RoleUrl,String>,JpaSpecificationExecutor<RoleUrl> {
}
