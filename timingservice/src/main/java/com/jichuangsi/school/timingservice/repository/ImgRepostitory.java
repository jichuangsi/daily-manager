package com.jichuangsi.school.timingservice.repository;

import com.jichuangsi.school.timingservice.entity.Img;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ImgRepostitory extends JpaRepository<Img,Integer> {

    @Transactional
    @Modifying
    @Query(value = "insert into img(uuid,img) values(?1,?2)",nativeQuery = true)
    void insertRecord(String uuid, byte[] file);


    List<Img> findAllByUuid(String uuid);
}
