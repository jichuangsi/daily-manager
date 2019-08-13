package com.jichuangsi.school.timingservice.service;

import com.alibaba.fastjson.JSONObject;
import com.jichuangsi.school.timingservice.commons.Md5Util;
import com.jichuangsi.school.timingservice.constant.ResultCode;
import com.jichuangsi.school.timingservice.dao.mapper.StaffMapper;
import com.jichuangsi.school.timingservice.entity.*;
import com.jichuangsi.school.timingservice.exception.BackUserException;
import com.jichuangsi.school.timingservice.exception.StaffHttpException;
import com.jichuangsi.school.timingservice.model.HttpTokenModel;
import com.jichuangsi.school.timingservice.model.LoginElementModel;
import com.jichuangsi.school.timingservice.model.UserInfoForToken;
import com.jichuangsi.school.timingservice.model.WxLoginModel;
import com.jichuangsi.school.timingservice.repository.*;
import com.jichuangsi.school.timingservice.utils.MappingEntity2ModelCoverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.*;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Service
public class StaffConsoleService {
    @Resource
    private PeopleRepostitory peopleRepostitory;
    @Resource
    private IStaffRepository staffRepository;
    @Resource
    private IStatusRepository statusRepository;
    @Resource
    private StaffMapper staffMapper;
    @Resource
    private IHttpService httpService;
    @Resource
    private IDepartmentRepository departmentRepository;
    @Resource
    private IRoleRepository roleRepository;
    @Resource
    private BackTokenService tokenService;
    @Resource
    private IOpLogRepository opLogRepository;
    @Resource
    private BackUserService backUserService;

    @Transactional(rollbackFor = Exception.class)
    public void saveStaff(Staff staff){
        staffRepository.save(staff);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteStaff(String id){
        staffRepository.deleteById(id);
    }

    public List<Staff> getStaffList(){
        return staffRepository.findAll();
    }

    //按条件分页查询url
    public Page<Staff> getStaffListByPage(UserInfoForToken userInfo,int pageNum, int pageSize, String staffName, String statusId)throws BackUserException{
        BackUser user=backUserService.getBackUserById(userInfo.getUserId());
        pageNum=pageNum-1;
        Pageable pageable=new PageRequest(pageNum,pageSize);
        Page<Staff> page=staffRepository.findAll((Root<Staff> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)->{
            List<Predicate> predicateList = new ArrayList<>();
            if(statusId!=null && statusId!=""){
                Join<Staff,Status> departmentJoin=root.join("status",JoinType.LEFT);
                predicateList.add(criteriaBuilder.equal(departmentJoin.get("id"), statusId));
            }
            if(staffName!=null && staffName!=""){
                predicateList.add(criteriaBuilder.like(root.get("name"),"%"+staffName+"%"));
            }
            if (user!=null){
                //user.getRoleName().equals("M") || user.getRoleName().equals("院长") || user.getRoleName().equals("副院长")
                if (user.getRoleName().equals("部长")){
                    Department department=departmentRepository.findByid(user.getDeptId());
                    predicateList.add(criteriaBuilder.equal(root.get("department"),department));
                }
                if (user.getRoleName().equals("员工")){
                    predicateList.add(criteriaBuilder.equal(root.get("wechat"),user.getWechat()));
                }
            }

            return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        },pageable);
        return page;
    }

    //查询状态列表
    public List<Status> getStatusList(){
        return  statusRepository.findAll();
    }

    //修改状态
    @Transactional(rollbackFor = Exception.class)
    public void updateStatusById(UserInfoForToken userInfo, String statusId, String wechat){
        staffMapper.updateStatusById(wechat,statusId);
        OpLog opLog=new OpLog(userInfo.getUserNum(),"修改","修改员工状态");
        opLogRepository.save(opLog);
    }
    //修改部门
    @Transactional(rollbackFor = Exception.class)
    public void updateDeptById(UserInfoForToken userInfo, String deptId, String wechat){
        staffMapper.updateDepartmentById(wechat,deptId);
        Department department=departmentRepository.findByid(deptId);
        BackUser backUser=backUserService.findBackUserByWechat(wechat);
        backUser.setDeptId(department.getId());
        backUser.setDeptName(department.getDeptname());
        backUserService.saveBackUser(backUser);
        OpLog opLog=new OpLog(userInfo.getUserNum(),"修改","修改员工部门");
        //lai
        peopleRepostitory.updateDPMTforOPENID(deptId,wechat);
        opLogRepository.save(opLog);
    }
    //修改角色
    @Transactional(rollbackFor = Exception.class)
    public void updateRoleById(UserInfoForToken userInfo, String roleId, String wechat){
        staffMapper.updateRoleById(wechat,roleId);
        Role role=roleRepository.findByid(roleId);
        BackUser backUser=backUserService.findBackUserByWechat(wechat);
        backUser.setRoleId(role.getId());
        backUser.setRoleName(role.getRolename());
        backUserService.saveBackUser(backUser);
        OpLog opLog=new OpLog(userInfo.getUserNum(),"修改","修改员工角色信息");
        //lai
        peopleRepostitory.updateJSQXforOPENID(roleId,wechat);
        opLogRepository.save(opLog);
    }

    //获取token
    public HttpTokenModel findTokenByCode(String code) throws StaffHttpException {
        if (StringUtils.isEmpty(code)){
            throw new StaffHttpException(ResultCode.PARAM_MISS_MSG);
        }
        String result = "";
        try {
            result = httpService.findWxTokenModel(code);
        } catch (StaffHttpException e) {
            throw new StaffHttpException(e.getMessage());
        }
        HttpTokenModel tokenModel =  JSONObject.parseObject(result,HttpTokenModel.class);
        return tokenModel;
    }

    //查询用户是否存在
    public LoginElementModel checkStaff(String opendId) throws StaffHttpException {
        if(StringUtils.isEmpty(opendId)){
            throw new StaffHttpException(ResultCode.PARAM_MISS_MSG);
        }
        Staff staff=staffRepository.findByWechat(opendId);
        LoginElementModel model=new LoginElementModel();
       /* if (null==staff){
            model.setResultCode(ResultCode.OPENDID_NOFIND);
            model.setDepartmentList(departmentRepository.findAll());
            model.setRoleList(roleRepository.findAll());
            return  model;
        }*/
       if (staff!=null){
           model.setResultCode(ResultCode.OPENDID_ISEXIST_MSG);
           return model;
       }
        model.setResultCode(ResultCode.OPENDID_NOFIND);
        return model;
    }

    //用户登录
    public LoginElementModel loginStaff(String opendId) throws StaffHttpException {
        if(StringUtils.isEmpty(opendId)){
            throw new StaffHttpException(ResultCode.PARAM_MISS_MSG);
        }
        Staff staff=staffRepository.findByWechat(opendId);
        LoginElementModel model=new LoginElementModel();
        if (null==staff){
            model.setResultCode(ResultCode.OPENDID_NOFIND);
            model.setDepartmentList(departmentRepository.findAll());
            model.setRoleList(roleRepository.findAll());
            return  model;
        }
        UserInfoForToken userInfo=MappingEntity2ModelCoverter.CONVERTERFROMSTAFF(staff);
        try {
            String userStr = JSONObject.toJSONString(userInfo);
            model.setResultCode(ResultCode.OPENDID_ISEXIST_MSG);
            model.setAccessToken(tokenService.createdToken(userStr));
            return model;
        } catch (UnsupportedEncodingException e) {
            throw new StaffHttpException(ResultCode.TOKEN_CHECK_ERR_MSG);
        }
    }

    //用户注册
    public LoginElementModel registStaff(WxLoginModel model) throws StaffHttpException,BackUserException {
        if (StringUtils.isEmpty(model.getAccount()) || StringUtils.isEmpty(model.getPwd())
                || StringUtils.isEmpty(model.getName()) || StringUtils.isEmpty(model.getOpendId())
                || StringUtils.isEmpty(model.getDeptId()) || StringUtils.isEmpty(model.getRoleId())){
            throw new BackUserException(ResultCode.PARAM_MISS_MSG);
        }
        Staff staff=staffRepository.findByWechat(model.getOpendId());
        if (null==staff){
            staff=new Staff();
            Department department=departmentRepository.findByid(model.getDeptId());
            Role role=new Role();
            role.setId(model.getRoleId());
            role.setRolename(model.getRoleName());
            Status status=statusRepository.findByName("在职");
            staff.setDepartment(department);
            staff.setRole(role);
            staff.setName(model.getName());
            staff.setStatus(status);
            staff.setWechat(model.getOpendId());
            staff.setAccount(model.getAccount());
            staff.setPwd(Md5Util.encodeByMd5(model.getPwd()));
            staffRepository.save(staff);
            backUserService.registBackUser(model);

            //赖
            People people = new People();
            people.setDepartment(model.getDeptId());
            people.setJurisdiction(model.getRoleName());
            people.setOpenId(model.getOpendId());
            people.setPeopleName(model.getName());
            peopleRepostitory.save(people);
        }
        UserInfoForToken userInfo=MappingEntity2ModelCoverter.CONVERTERFROMSTAFF(staff);
        try {
            LoginElementModel loginModel=new LoginElementModel();
            String userStr = JSONObject.toJSONString(userInfo);
            loginModel.setResultCode(ResultCode.OPENDID_ISEXIST_MSG);
            loginModel.setAccessToken(tokenService.createdToken(userStr));
            return loginModel;
        } catch (UnsupportedEncodingException e) {
            throw new StaffHttpException(ResultCode.TOKEN_CHECK_ERR_MSG);
        }
    }
}
