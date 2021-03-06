package com.jichuangsi.school.timingservice.service;

import com.alibaba.fastjson.JSONObject;
import com.jichuangsi.school.timingservice.commons.Md5Util;
import com.jichuangsi.school.timingservice.constant.ResultCode;
import com.jichuangsi.school.timingservice.constant.Status;
import com.jichuangsi.school.timingservice.entity.BackUser;
import com.jichuangsi.school.timingservice.entity.Role;
import com.jichuangsi.school.timingservice.exception.BackUserException;
import com.jichuangsi.school.timingservice.model.BackUserModel;
import com.jichuangsi.school.timingservice.model.UpdatePwdModel;
import com.jichuangsi.school.timingservice.model.UserInfoForToken;
import com.jichuangsi.school.timingservice.model.WxLoginModel;
import com.jichuangsi.school.timingservice.repository.IBackUserRepository;
import com.jichuangsi.school.timingservice.repository.IRoleRepository;
import com.jichuangsi.school.timingservice.repository.IStatusRepository;
import com.jichuangsi.school.timingservice.utils.MappingEntity2ModelCoverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

@Service
public class BackUserService {
    @Resource
    private IBackUserRepository backUserRepository;
    @Resource
    private BackTokenService backTokenService;
    @Resource
    private IStatusRepository statusRepository;
    @Resource
    private IRoleRepository roleRepository;
    public void registBackUser(WxLoginModel model) throws BackUserException {
        /*if (StringUtils.isEmpty(model.getAccount()) || StringUtils.isEmpty(model.getPwd())
                || StringUtils.isEmpty(model.getName()) || StringUtils.isEmpty(model.getOpendId())
                || StringUtils.isEmpty(model.getDeptId()) || StringUtils.isEmpty(model.getRoleId())){
            throw new BackUserException(ResultCode.PARAM_MISS_MSG);
        }*/
        if (backUserRepository.countByAccount(model.getAccount()) > 0){
            throw new BackUserException(ResultCode.ACCOUNT_ISEXIST_MSG);
        }
        BackUser user=new BackUser();
        user.setWechat(model.getOpendId());
        user.setDeptId(model.getDeptId());
        user.setDeptName(model.getDeptname());
        user.setAccount(model.getAccount());
        user.setPwd(Md5Util.encodeByMd5(model.getPwd()));
        user.setRoleId(model.getRoleId());
        user.setRoleName(model.getRoleName());
        user.setStatus(Status.ACTIVATE.getName());
        user.setUserName(model.getName());
        backUserRepository.save(user);
    }
    public String loginBackUser(BackUserModel model)throws BackUserException {
       if (StringUtils.isEmpty(model.getAccount()) || StringUtils.isEmpty(model.getPwd())){
            throw new BackUserException(ResultCode.PARAM_MISS_MSG);
        }
        BackUser backUser = backUserRepository.findByAccountAndPwdAndStatus(model.getAccount(),Md5Util.encodeByMd5(model.getPwd()),Status.ACTIVATE.getName());
        if (null == backUser){
            throw new BackUserException(ResultCode.ACCOUNT_NOTEXIST_MSG);
        }
        String user = JSONObject.toJSONString(MappingEntity2ModelCoverter.CONVERTERFROMBACKUSERINFO(backUser));
        try {
            return backTokenService.createdToken(user);
        } catch (UnsupportedEncodingException e) {
            throw new BackUserException(e.getMessage());
        }
    }

    public void insertSuperMan() throws BackUserException {
        if (backUserRepository.countByAccount("Admin") > 0){
            throw new BackUserException(ResultCode.ACCOUNT_ISEXIST_MSG);
        }
        BackUser userInfo = new BackUser();
        userInfo.setStatus(Status.ACTIVATE.getName());
        userInfo.setAccount("admin");
        userInfo.setUserName("admin");
        userInfo.setRoleId("123456");
        userInfo.setRoleName("M");
        userInfo.setDeptId("123456");
        userInfo.setDeptName("S");
        userInfo.setPwd(Md5Util.encodeByMd5("admin"));
        userInfo.setWechat("123456");
        userInfo.setCreatedTime(new Date().getTime());
        backUserRepository.save(userInfo);
    }

    public BackUser getBackUserById(String userId) throws BackUserException {
        if(StringUtils.isEmpty(userId)){
            throw new BackUserException(ResultCode.PARAM_MISS_MSG);
        }
        return backUserRepository.findByid(userId);
    }

    public void updateBackUserPwd(UserInfoForToken userInfoForToken, UpdatePwdModel model)throws BackUserException {
        if(StringUtils.isEmpty(model.getFirstPwd()) || StringUtils.isEmpty(model.getSecondPwd())){
            throw new BackUserException(ResultCode.PARAM_MISS_MSG);
        }
        if(!(model.getFirstPwd().equals(model.getSecondPwd()))){
            throw new BackUserException(ResultCode.PWD_NOT_MSG);
        }
        BackUser user=backUserRepository.findByid(userInfoForToken.getUserId());
        if(null==user){
            throw new BackUserException(ResultCode.ACCOUNT_NOTEXIST_MSG);
        }
        user.setPwd(Md5Util.encodeByMd5(model.getSecondPwd()));
        backUserRepository.save(user);
    }
    //修改员工密码
    public void updateBackUserPwd2(UserInfoForToken userInfoForToken,BackUser user,String pwd)throws BackUserException {
        if(StringUtils.isEmpty(pwd) || user==null){
            throw new BackUserException(ResultCode.PARAM_MISS_MSG);
        }
        user.setPwd(Md5Util.encodeByMd5(pwd));
        backUserRepository.save(user);
    }
    @Transactional(rollbackFor = Exception.class)
    public void saveStatus(com.jichuangsi.school.timingservice.entity.Status status){
        statusRepository.save(status);
    }

    public BackUser findBackUserByWechat(String wechat){
        return backUserRepository.findByWechat(wechat);
    }
    @Transactional(rollbackFor = Exception.class)
    public void saveBackUser(BackUser backUser){
        backUserRepository.save(backUser);
    }

    public List<BackUser> findBackUserByRoleName(String roleName)throws BackUserException{
        Role role=roleRepository.findByRolename(roleName);
        if (role!=null){
            return backUserRepository.findByRoleIdAndStatus(role.getId(),Status.ACTIVATE.getName());
        }else{
            throw new BackUserException(ResultCode.SELECT_NULL_MSG);
        }
    }
}
