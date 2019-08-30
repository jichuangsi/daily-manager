package com.jichuangsi.school.timingservice.service;

import com.jichuangsi.school.timingservice.constant.ResultCode;
import com.jichuangsi.school.timingservice.dao.mapper.StaffMapper;
import com.jichuangsi.school.timingservice.entity.BackUser;
import com.jichuangsi.school.timingservice.entity.Department;
import com.jichuangsi.school.timingservice.entity.OpLog;
import com.jichuangsi.school.timingservice.exception.BackUserException;
import com.jichuangsi.school.timingservice.model.UserInfoForToken;
import com.jichuangsi.school.timingservice.repository.IBackUserRepository;
import com.jichuangsi.school.timingservice.repository.IDepartmentRepository;
import com.jichuangsi.school.timingservice.repository.IOpLogRepository;
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
    public void saveDepartment(UserInfoForToken userInfo, Department department) throws BackUserException{
        if(departmentRepository.countByDeptname(department.getDeptname())>0){
            throw new BackUserException(ResultCode.DEPT_ISEXIST_MSG);
        }
        departmentRepository.save(department);
        OpLog opLog=new OpLog(userInfo.getUserNum(),"添加","添加部门");
        opLogRepository.save(opLog);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateDepartment(UserInfoForToken userInfo, Department department)throws BackUserException{
        if(departmentRepository.countByDeptname(department.getDeptname())>0){
            throw new BackUserException(ResultCode.DEPT_ISEXIST_MSG);
        }
        departmentRepository.save(department);
        List<BackUser> backUsers=backUserRepository.findByDeptId(department.getId());
        if (backUsers!=null){
            for (BackUser user:backUsers) {
                user.setDeptName(department.getDeptname());
                backUserRepository.save(user);
            }
        }
        OpLog opLog=new OpLog(userInfo.getUserNum(),"修改","修改部门");
        opLogRepository.save(opLog);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteDepartment(UserInfoForToken userInfo, String id)throws BackUserException {
        if(staffMapper.countStaffByDeptid(id)>0){
            throw new BackUserException(ResultCode.DEPT_STAFF_EXIST);
        }
        if(backUserRepository.countByDeptId(id)>0){
            throw new BackUserException(ResultCode.DEPT_BACKUSER_EXIST);
        }
        OpLog opLog=new OpLog(userInfo.getUserNum(),"删除","删除部门");
        opLogRepository.save(opLog);
        departmentRepository.deleteById(id);
    }

    public List<Department> getDepartmentList(){
        return departmentRepository.findAll();
    }
}
