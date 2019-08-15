package com.jichuangsi.school.timingservice.service;

import com.jichuangsi.school.timingservice.constant.ResultCode;
import com.jichuangsi.school.timingservice.dao.mapper.StaffMapper;
import com.jichuangsi.school.timingservice.dao.mapper.UrlRelationMapper;
import com.jichuangsi.school.timingservice.entity.OpLog;
import com.jichuangsi.school.timingservice.entity.Role;
import com.jichuangsi.school.timingservice.entity.RoleDepartment;
import com.jichuangsi.school.timingservice.exception.BackUserException;
import com.jichuangsi.school.timingservice.model.DeptModel;
import com.jichuangsi.school.timingservice.model.UserInfoForToken;
import com.jichuangsi.school.timingservice.repository.IBackUserRepository;
import com.jichuangsi.school.timingservice.repository.IOpLogRepository;
import com.jichuangsi.school.timingservice.repository.IRoleDepartmentRepository;
import com.jichuangsi.school.timingservice.repository.IRoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BackRoleService {
    @Resource
    private IRoleRepository roleRepository;
    @Resource
    private IOpLogRepository opLogRepository;
    @Resource
    private StaffMapper staffMapper;
    @Resource
    private IBackUserRepository backUserRepository;
    @Resource
    private IRoleDepartmentRepository roleDepartmentRepository;
    @Resource
    private UrlRelationMapper urlRelationMapper;

    @Transactional(rollbackFor = Exception.class)
    public void saveRole(UserInfoForToken userInfo, Role role){
        roleRepository.save(role);
        OpLog opLog=new OpLog(userInfo.getUserNum(),"添加","添加角色");
        opLogRepository.save(opLog);
    }
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(UserInfoForToken userInfo, Role role){
        roleRepository.save(role);
        OpLog opLog=new OpLog(userInfo.getUserNum(),"修改","修改角色");
        opLogRepository.save(opLog);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(UserInfoForToken userInfo, String id)throws BackUserException {
        if(StringUtils.isEmpty(id)){
            throw new BackUserException(ResultCode.PARAM_MISS_MSG);
        }
        if(staffMapper.countStaffByRoleid(id)>0){
            throw new BackUserException(ResultCode.ROLE_STAFF_EXIST);
        }
        if(backUserRepository.countByRoleId(id)>0){
            throw new BackUserException(ResultCode.ROLE_BACKUSER_EXIST);
        }
        OpLog opLog=new OpLog(userInfo.getUserNum(),"删除","删除角色");
        opLogRepository.save(opLog);
        roleRepository.deleteById(id);
    }

    public List<Role> getRoleList(){
        return roleRepository.findAll();
    }

    //根据角色查询管理部门
    public List<DeptModel> getRoleDepartment(String roleId) throws BackUserException{
        if(roleId==null){
            throw new BackUserException(ResultCode.PARAM_MISS_MSG);
        }
        return urlRelationMapper.findByRoleId(roleId);
    }
    //批量增加
    @Transactional(rollbackFor = Exception.class)
    public void batchInsertRoleDepartment(List<RoleDepartment> roleDepartmentList){
        roleDepartmentRepository.saveAll(roleDepartmentList);
    }
    //批量删除
    @Transactional(rollbackFor = Exception.class)
    public void batchdeleteRoleDepartment(List<RoleDepartment> roleDepartmentList){
        roleDepartmentRepository.deleteInBatch(roleDepartmentList);
    }
}
