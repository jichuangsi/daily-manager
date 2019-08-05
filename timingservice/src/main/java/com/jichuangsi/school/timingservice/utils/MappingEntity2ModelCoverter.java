package com.jichuangsi.school.timingservice.utils;

import com.jichuangsi.school.timingservice.entity.BackUser;
import com.jichuangsi.school.timingservice.entity.RoleUrl;
import com.jichuangsi.school.timingservice.entity.Staff;
import com.jichuangsi.school.timingservice.model.RoleUrlUseWayModel;
import com.jichuangsi.school.timingservice.model.UserInfoForToken;

import java.util.ArrayList;
import java.util.List;

public final class MappingEntity2ModelCoverter {
    public static final List<RoleUrlUseWayModel> CONVERTERFROMROLEURLTOROLEURLMODEL(List<RoleUrl> roleurls){
        List<RoleUrlUseWayModel> roleUrlModels=new ArrayList<>();
        for (RoleUrl roleurl: roleurls) {
            RoleUrlUseWayModel model=new RoleUrlUseWayModel();
            model.setId(roleurl.getId());
            model.setName(roleurl.getName());
            model.setUrl(roleurl.getUrl());
            /*model.setUsewayid(roleurl.getUseModule().getId());
            model.setUsewayname(roleurl.getUseModule().getName());*/
            roleUrlModels.add(model);
        }
        return roleUrlModels;
    }
    public final static UserInfoForToken CONVERTERFROMBACKUSERINFO(BackUser userInfo){
        UserInfoForToken userInfoForToken = new UserInfoForToken();
        userInfoForToken.setUserId(userInfo.getId());
        userInfoForToken.setUserName(userInfo.getUserName());
        userInfoForToken.setUserNum(userInfo.getAccount());
        return userInfoForToken;
    }
    public static final UserInfoForToken CONVERTERFROMSTAFF(Staff staff){
        UserInfoForToken userInfo = new UserInfoForToken();
        userInfo.setUserNum(staff.getWechat());
        userInfo.setUserId(staff.getId().toString());
        userInfo.setUserName(staff.getName());
        userInfo.setRoleName(staff.getRole().getRolename());
        return userInfo;
    }
}
