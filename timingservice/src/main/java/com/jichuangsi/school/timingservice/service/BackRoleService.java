package com.jichuangsi.school.timingservice.service;

import com.jichuangsi.school.timingservice.constant.ResultCode;
import com.jichuangsi.school.timingservice.dao.mapper.StaffMapper;
import com.jichuangsi.school.timingservice.entity.OpLog;
import com.jichuangsi.school.timingservice.entity.Role;
import com.jichuangsi.school.timingservice.exception.BackUserException;
import com.jichuangsi.school.timingservice.model.UserInfoForToken;
import com.jichuangsi.school.timingservice.repository.IBackUserRepository;
import com.jichuangsi.school.timingservice.repository.IOpLogRepository;
import com.jichuangsi.school.timingservice.repository.IRoleRepository;
import com.jichuangsi.school.timingservice.repository.IStaffRepository;
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

    @Transactional(rollbackFor = Exception.class)
    public void saveRole(UserInfoForToken userInfo, Role role){
        roleRepository.save(role);
        OpLog opLog=new OpLog();
        opLog.setOperatorId(userInfo.getUserId());
        String action="操作角色：".concat(role.getRolename());
        opLog.setOpAction(action);
        opLogRepository.save(opLog);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(UserInfoForToken userInfo,String id)throws BackUserException {
        if(StringUtils.isEmpty(id)){
            throw new BackUserException(ResultCode.PARAM_MISS_MSG);
        }
        if(staffMapper.countStaffByRoleid(id)>0){
            throw new BackUserException(ResultCode.ROLE_STAFF_EXIST);
        }
        if(backUserRepository.countByRoleId(id)>0){
            throw new BackUserException(ResultCode.ROLE_BACKUSER_EXIST);
        }
        roleRepository.deleteById(id);
        OpLog opLog=new OpLog();
        opLog.setOperatorId(userInfo.getUserId());
        String action="删除了一个角色";
        opLog.setOpAction(action);
        opLogRepository.save(opLog);
    }

    public List<Role> getRoleList(){
        return roleRepository.findAll();
    }
}
