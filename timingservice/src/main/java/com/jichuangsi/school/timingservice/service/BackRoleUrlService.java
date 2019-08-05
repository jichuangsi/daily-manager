package com.jichuangsi.school.timingservice.service;

import com.jichuangsi.school.timingservice.constant.ResultCode;
import com.jichuangsi.school.timingservice.dao.mapper.UrlRelationMapper;
import com.jichuangsi.school.timingservice.entity.*;
import com.jichuangsi.school.timingservice.exception.BackUserException;
import com.jichuangsi.school.timingservice.model.RoleUrlModel;
import com.jichuangsi.school.timingservice.model.RoleUrlUseWayModel;
import com.jichuangsi.school.timingservice.model.UrlMapping;
import com.jichuangsi.school.timingservice.model.UserInfoForToken;
import com.jichuangsi.school.timingservice.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class BackRoleUrlService {
    @Resource
    private IRoleUrlRepository roleUrlRepository;
    @Resource
    private IUseModuleRespository useModuleRespository;
    @Resource
    private IUrlRelationRepository urlRelationRepository;
    @Resource
    private UrlRelationMapper urlRelationMapper;
    @Resource
    private IOpLogRepository opLogRepository;
    @Resource
    private IStaticPageRespository staticPageRespository;

    //获得全部url
    public List<RoleUrl> getRoleUrlList(){
        return roleUrlRepository.findAll();
    }

    //按条件分页查询url
    public Page<RoleUrl> getRoleUrlListByPage(int pageNum,int pageSize,String usewayid,String name){
        pageNum=pageNum-1;
        Pageable pageable=new PageRequest(pageNum,pageSize);
        Page<RoleUrl> page=roleUrlRepository.findAll((Root<RoleUrl> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)->{
            List<Predicate> predicateList = new ArrayList<>();
            if(usewayid!=null && usewayid!=""){
                Join<RoleUrl,UseModule> useWayJoin=root.join("useWay",JoinType.LEFT);
                predicateList.add(criteriaBuilder.equal(useWayJoin.get("id"), usewayid));
            }
            if(name!=null && name!=""){
                predicateList.add(criteriaBuilder.like(root.get("name"),"%"+name+"%"));
            }
            return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        },pageable);
        return page;
    }

    //添加角色对应的url
    @Transactional(rollbackFor = Exception.class)
    public void insertUrlRelation(UrlRelation urlRelation){
        urlRelationRepository.save(urlRelation);
    }

    //批量添加角色对应的url
    public void batchInsertUrlRelation(UserInfoForToken userInfo, List<UrlRelation> urlRelation){
        urlRelationRepository.saveAll(urlRelation);
        OpLog opLog=new OpLog();
        opLog.setOperatorId(userInfo.getUserId());
        String action="分配权限";
        opLog.setOpAction(action);
        opLogRepository.save(opLog);

    }
    //根据id批量删除用户相关url
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteRoleUrl(UserInfoForToken userInfo,List<UrlRelation>  urlRelationId){
        urlRelationRepository.deleteInBatch(urlRelationId);
        OpLog opLog=new OpLog();
        opLog.setOperatorId(userInfo.getUserId());
        String action="移除权限";
        opLog.setOpAction(action);
        opLogRepository.save(opLog);
    }

    //查询url父级分类
    public List<UseModule> getAllUseModule(){
        return useModuleRespository.findAll();
    }

    public List<RoleUrlModel> getAllRoleUrlModel(String roleId){
        return urlRelationMapper.getRoleUrlByRoleId(roleId);
    }

    //查询模块
    public List<UrlMapping> getModelAndStaticPageByRoleId(String roleId)throws BackUserException {
        if(StringUtils.isEmpty(roleId)){
            throw  new BackUserException(ResultCode.PARAM_MISS_MSG);
        }
        return urlRelationMapper.getModelAndStaticPageByRoleId(roleId);
    }

    public List<UrlMapping> getStaticPageAndRoleUrlByRoleId(String roleId,String pageId)throws BackUserException{
        if(StringUtils.isEmpty(roleId)||StringUtils.isEmpty(pageId)){
            throw  new BackUserException(ResultCode.PARAM_MISS_MSG);
        }
        return urlRelationMapper.getStaticPageAndRoleUrlByRoleIdAndPageId(roleId,pageId);
    }
    //获取全部url
    public List<RoleUrlUseWayModel> getAllRoleUrl(){
        return urlRelationMapper.getAllRoleUrl();
    }

    //查询url父级分类
    public List<StaticPage> getAllStaticPage(){
        return staticPageRespository.findAll();
    }
    //根据角色id查询url
    public List<UrlMapping> getUrlByRoleId(String roleId){return urlRelationMapper.getStaticPageAndRoleUrlByRoleId(roleId);}
}
