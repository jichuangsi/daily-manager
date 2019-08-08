package com.jichuangsi.school.timingservice.service;

import com.jichuangsi.school.timingservice.entity.OpLog;
import com.jichuangsi.school.timingservice.repository.IOpLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class OpLogService {
    @Resource
    private IOpLogRepository opLogRepository;

    public Page<OpLog> getOpLogByNameAndPage(int pageNum, int pageSize, String name){
        pageNum=pageNum-1;
        Sort sort=new Sort(Sort.Direction.DESC,"createdTime");
        Pageable pageable=new PageRequest(pageNum,pageSize,sort);
        Page<OpLog> page=opLogRepository.findAll((Root<OpLog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)->{
            List<Predicate> predicateList = new ArrayList<>();
            if(name!=null && name!=""){
                predicateList.add(criteriaBuilder.like(root.get("operatorName"),"%"+name+"%"));
            }
            return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        },pageable);
        return page;
    }

    @Transactional(rollbackFor = Exception.class)
    public  void  deleteOplog(String opId){
        opLogRepository.deleteById(opId);
    }
}
