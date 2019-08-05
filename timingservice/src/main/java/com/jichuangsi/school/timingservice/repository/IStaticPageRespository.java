package com.jichuangsi.school.timingservice.repository;

import com.jichuangsi.school.timingservice.entity.StaticPage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IStaticPageRespository extends JpaRepository<StaticPage,String> {
}
