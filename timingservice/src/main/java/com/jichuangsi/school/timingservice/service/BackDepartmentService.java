package com.jichuangsi.school.timingservice.service;

import com.jichuangsi.school.timingservice.constant.ResultCode;
import com.jichuangsi.school.timingservice.dao.mapper.StaffMapper;
import com.jichuangsi.school.timingservice.entity.Department;
import com.jichuangsi.school.timingservice.entity.OpLog;
import com.jichuangsi.school.timingservice.exception.BackUserException;
import com.jichuangsi.school.timingservice.model.UserInfoForToken;
import com.jichuangsi.school.timingservice.repository.IBackUserRepository;
import com.jichuangsi.school.timingservice.repository.IDepartmentRepository;
import com.jichuangsi.school.timingservice.repository.IOpLogRepository;
import com.jichuangsi.school.timingservice.repository.IStaffRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BackDepartmentService {
    @Resource
    private IDepartmentRepository departmentRepository;
    @Resource
    private IOpLogRepository opLogRepository;
    @Resource
    private StaffMapper staffMapper;
    @Resource
    private IBackUserRepository backUserRepository;

    @Transactional(rollbackFor = Exception.class)
    public void saveDepartment(UserInfoForToken userInfo,Department department){
        departmentRepository.save(department);
        OpLog opLog=new OpLog();
        opLog.setOperatorId(userInfo.getUserId());
        String action="操作部门：".concat(department.getDeptname());
        opLog.setOpAction(action);
        opLogRepository.save(opLog);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteDepartment(UserInfoForToken userInfo,String id)throws BackUserException{
        if(staffMapper.countStaffByDeptid(id)>0){
            throw new BackUserException(ResultCode.DEPT_STAFF_EXIST);
        }
        if(backUserRepository.countByDeptId(id)>0){
            throw new BackUserException(ResultCode.DEPT_BACKUSER_EXIST);
        }
        departmentRepository.deleteById(id);
        OpLog opLog=new OpLog();
        opLog.setOperatorId(userInfo.getUserId());
        String action="删除了一个部门：".concat(departmentRepository.findByid(id).getDeptname());
        opLog.setOpAction(action);
        opLogRepository.save(opLog);
    }

    public List<Department> getDepartmentList(){
        return departmentRepository.findAll();
    }
}
