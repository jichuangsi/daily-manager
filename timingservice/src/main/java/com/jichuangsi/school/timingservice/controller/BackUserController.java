package com.jichuangsi.school.timingservice.controller;

import com.jichuangsi.school.timingservice.entity.BackUser;
import com.jichuangsi.school.timingservice.exception.BackUserException;
import com.jichuangsi.school.timingservice.model.BackUserModel;
import com.jichuangsi.school.timingservice.model.ResponseModel;
import com.jichuangsi.school.timingservice.model.UserInfoForToken;
import com.jichuangsi.school.timingservice.model.WxLoginModel;
import com.jichuangsi.school.timingservice.service.BackRoleUrlService;
import com.jichuangsi.school.timingservice.service.BackUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Api("关于后台角色权限管理Api")
@RequestMapping("/backuser")
@CrossOrigin
public class BackUserController {
    @Resource
    private BackUserService backUserService;
    @Resource
    private BackRoleUrlService backRoleUrlService;

    @ApiOperation("后台注册")
    @ApiImplicitParams({})
    @PostMapping("/registBackUser")
    public ResponseModel registBackUser(@RequestBody WxLoginModel userModel){
        try {
            backUserService.registBackUser(userModel);
        }catch (BackUserException e){
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }
    @ApiOperation("后台登录")
    @ApiImplicitParams({})
    @PostMapping("/LoginBackUser")
    public ResponseModel loginBackUser(@RequestBody BackUserModel userModel){
        try {
            return ResponseModel.sucess("",backUserService.loginBackUser(userModel));
        }catch (BackUserException e){
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation("根据id查询后台角色")
    @ApiImplicitParams({})
    @GetMapping("/getBackuserById")
    public ResponseModel getBackuserById(@ModelAttribute UserInfoForToken userInfo){
        try {
            return ResponseModel.sucess("",backUserService.getBackUserById(userInfo.getUserId()));
        }catch (BackUserException e){
            return ResponseModel.fail("",e.getMessage());
        }

    }

    @ApiOperation("查询模块列表")
    @ApiImplicitParams({})
    @GetMapping("/getUsemoduleList")
    public ResponseModel getUsemoduleList(@ModelAttribute UserInfoForToken userInfo){
        return ResponseModel.sucess("",backRoleUrlService.getAllUseModule());
    }

}
