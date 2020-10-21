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
    @Resource
    private IBackUserRepository backUserRepository;
    @Resource
    private IUrlRelationRepository urlRelationRepository;
    @Resource
    private SQRepostitory sqRepostitory;
    @Resource
    private ImgRepostitory imgRepostitory;
    @Resource
    private OLRepostitory olRepostitory;
    @Resource
    private RecordRepostitory recordRepostitory;

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
    public Page<Staff> getStaffListByPage(UserInfoForToken userInfo,int pageNum, int pageSize, String staffName, String statusId,String deptId)throws BackUserException{
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
                if (user.getRoleName().equals("部门负责人")){
                    Department department=departmentRepository.findByid(user.getDeptId());
                    predicateList.add(criteriaBuilder.equal(root.get("department"),department));
                }
                if (user.getRoleName().equals("普通工作人员")){
                    predicateList.add(criteriaBuilder.equal(root.get("wechat"),user.getWechat()));
                }
                if (user.getRoleName().equals("副检察长")){
                    if(deptId!=null){
                        Department department=departmentRepository.findByid(deptId);
                        predicateList.add(criteriaBuilder.equal(root.get("department"),department));
                    }
                    if(deptId==null || deptId.equals("")){
                        Department department=new Department();
                        department.setDeptname("不查询");
                        department.setId("12345");
                        department.setDeptdescribe("不查询");
                        predicateList.add(criteriaBuilder.equal(root.get("department"),department));
                    }
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
    public void updateStatusById(UserInfoForToken userInfo, String statusId, String wechat)throws BackUserException{
        if(StringUtils.isEmpty(statusId) || StringUtils.isEmpty(wechat)){
            throw new BackUserException(ResultCode.PARAM_MISS_MSG);
        }
        staffMapper.updateStatusById(wechat,statusId);
        OpLog opLog=new OpLog(userInfo.getUserNum(),"修改","修改员工状态");
        opLogRepository.save(opLog);
    }
    //修改部门
    @Transactional(rollbackFor = Exception.class)
    public void updateDeptById(UserInfoForToken userInfo, String deptId, String wechat)throws BackUserException{
        if(StringUtils.isEmpty(deptId) || StringUtils.isEmpty(wechat)){
            throw new BackUserException(ResultCode.PARAM_MISS_MSG);
        }
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
    public void updateRoleById(UserInfoForToken userInfo, String roleId, String wechat)throws BackUserException{
        if(StringUtils.isEmpty(roleId)|| StringUtils.isEmpty(wechat)){
            throw new BackUserException(ResultCode.PARAM_MISS_MSG);
        }
        staffMapper.updateRoleById(wechat,roleId);
        Role role=roleRepository.findByid(roleId);
        BackUser backUser=backUserService.findBackUserByWechat(wechat);
        backUser.setRoleId(role.getId());
        backUser.setRoleName(role.getRolename());
        backUserService.saveBackUser(backUser);
        OpLog opLog=new OpLog(userInfo.getUserNum(),"修改","修改员工角色信息");
        //lai
        peopleRepostitory.updateJSQXforOPENID(role.getRolename(),wechat);
        opLogRepository.save(opLog);
    }

    //修改员工信息
    @Transactional(rollbackFor = Exception.class)
    public void updateStaff(UserInfoForToken userInfo,Staff staff)throws BackUserException{
        if (staff==null){
            throw new BackUserException(ResultCode.PARAM_MISS_MSG);
        }
        Department department=departmentRepository.findByid(staff.getDepartment().getId());
        Role role=roleRepository.findByid(staff.getRole().getId());
        BackUser user=backUserService.findBackUserByWechat(staff.getWechat());
        user.setDeptName(department.getDeptname());
        user.setRoleName(role.getRolename());
        user.setRoleId(staff.getRole().getId());
        user.setDeptId(staff.getDepartment().getId());
        user.setUserName(staff.getName());
        //lai
        peopleRepostitory.updateDPMTforOPENID(staff.getDepartment().getId(),staff.getWechat());
        //lai
        peopleRepostitory.updateJSQXforOPENID(role.getRolename(),staff.getWechat());
        OpLog opLog=new OpLog(userInfo.getUserNum(),"修改","修改员工个人信息");
        opLogRepository.save(opLog);
        staffRepository.save(staff);
        backUserService.saveBackUser(user);
    }
    //修改员工信息
    @Transactional(rollbackFor = Exception.class)
    public void updateStaffPwd(UserInfoForToken userInfo,String openId,String pwd)throws BackUserException{
        if (StringUtils.isEmpty(openId) || StringUtils.isEmpty(pwd)){
            throw new BackUserException(ResultCode.PARAM_MISS_MSG);
        }
        BackUser user=backUserService.findBackUserByWechat(openId);
        if(user==null){
            throw new BackUserException(ResultCode.SELECT_NULL_MSG);
        }
        Staff staff=staffRepository.findByWechatAndIsDelete(openId,"0");
        if(staff==null){
            throw new BackUserException(ResultCode.SELECT_NULL_MSG);
        }
        user.setPwd(Md5Util.encodeByMd5(pwd));
        staff.setPwd(Md5Util.encodeByMd5(pwd));
        OpLog opLog=new OpLog(userInfo.getUserNum(),"修改","修改员工密码");
        opLogRepository.save(opLog);
        backUserService.saveBackUser(user);
        staffRepository.save(staff);
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
        Staff staff=staffRepository.findByWechatAndIsDelete(opendId,"0");
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
        if(staffRepository.countByAccount(model.getAccount())>0){
            throw new BackUserException(ResultCode.ACCOUNT_ISEXIST_MSG2);
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
            staff.setIsDelete("0");
            staffRepository.save(staff);
            backUserService.registBackUser(model);

            //赖
            People people = new People();
            people.setDepartment(model.getDeptId());
            people.setJurisdiction(model.getRoleName());
            people.setOpenId(model.getOpendId());
            people.setPeopleName(model.getName());
            people.setIsDelete("0");
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

    /**
     * 删除用户
     * @param userInfo
     * @param opendId
     * @throws BackUserException
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(UserInfoForToken userInfo,String opendId)throws BackUserException{
        if(StringUtils.isEmpty(opendId)){
            throw new BackUserException(ResultCode.PARAM_MISS_MSG);
        }
        Staff staff=staffRepository.findByWechatAndIsDelete(opendId,"0");
        if (staff==null){
            throw new BackUserException(ResultCode.SELECT_NULL_MSG);
        }
        peopleRepostitory.deleteByOpenId(opendId);//人员表
        backUserRepository.deleteByWechat(opendId);//后台人员
        urlRelationRepository.deleteByUid(staff.getId().toString());//角色关联
        List<SQFlie> list=sqRepostitory.findAllByOpenId(opendId);
        List<String> imgs=new ArrayList<>();
        for (SQFlie item:list) {
            imgs.add(item.getUuid());
        }
        imgRepostitory.deleteByUuidIn(imgs);//申请表图片
        sqRepostitory.deleteByOpenId(opendId);//申请表
        olRepostitory.deleteByOpenId(opendId);//请假
        recordRepostitory.deleteByOpenId(opendId);//考勤表
        staffRepository.delete(staff);//员工表
        OpLog opLog=new OpLog(userInfo.getUserNum(),"删除","删除人员："+staff.getName());
        opLogRepository.save(opLog);
    }

    /**
     * 冻结用户 逻辑删除
     * @param userInfo
     * @param opendId
     * @throws BackUserException
     */
    @Transactional(rollbackFor = Exception.class)
    public void frozenStaffInfo(UserInfoForToken userInfo,String opendId)throws BackUserException{
        if(StringUtils.isEmpty(opendId)){
            throw new BackUserException(ResultCode.PARAM_MISS_MSG);
        }
        Staff staff=staffRepository.findByWechatAndIsDelete(opendId,"0");
        if (staff==null){
            throw new BackUserException(ResultCode.SELECT_NULL_MSG);
        }
        staffRepository.updateStaffStatus("1",opendId);
        peopleRepostitory.updateStatus("1",opendId);
        backUserRepository.updateStatus(com.jichuangsi.school.timingservice.constant.Status.DELETE.getName(),opendId);
        OpLog opLog=new OpLog(userInfo.getUserNum(),"冻结","冻结用户："+staff.getName());
        opLogRepository.save(opLog);
    }

    /**
     * 恢复员工个人信息
     * @param userInfo
     * @param opendId
     * @throws BackUserException
     */
    @Transactional(rollbackFor = Exception.class)
    public void thawStaffInfo(UserInfoForToken userInfo,String opendId)throws BackUserException{
        if(StringUtils.isEmpty(opendId)){
            throw new BackUserException(ResultCode.PARAM_MISS_MSG);
        }
        Staff staff=staffRepository.findByWechatAndIsDelete(opendId,"1");
        if (staff==null){
            throw new BackUserException(ResultCode.SELECT_NULL_MSG);
        }
        staffRepository.updateStaffStatus("0",opendId);
        peopleRepostitory.updateStatus("0",opendId);
        backUserRepository.updateStatus(com.jichuangsi.school.timingservice.constant.Status.ACTIVATE.getName(),opendId);
        OpLog opLog=new OpLog(userInfo.getUserNum(),"恢复","恢复用户："+staff.getName());
        opLogRepository.save(opLog);
    }
}
