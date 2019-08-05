package com.jichuangsi.school.timingservice.repository;

import com.jichuangsi.school.timingservice.entity.UrlRelation;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IUrlRelationRepository extends JpaRepository<UrlRelation,String> {

    //
    void deleteByUid(String uid);
}
