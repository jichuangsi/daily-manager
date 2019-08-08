package com.jichuangsi.school.timingservice.controller;

import com.jichuangsi.school.timingservice.entity.Status;
import com.jichuangsi.school.timingservice.exception.BackUserException;
import com.jichuangsi.school.timingservice.model.*;
import com.jichuangsi.school.timingservice.service.BackRoleUrlService;
import com.jichuangsi.school.timingservice.service.BackUserService;
import com.jichuangsi.school.timingservice.service.OpLogService;
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
    @Resource
    private OpLogService opLogService;

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

    @ApiOperation("后台修改密码")
    @ApiImplicitParams({})
    @PostMapping("/updateBackUserPwd")
    public ResponseModel updateBackUserPwd(@ModelAttribute UserInfoForToken userInfoForToken, @RequestBody UpdatePwdModel model){
        try {
            backUserService.updateBackUserPwd(userInfoForToken,model);
            return ResponseModel.sucessWithEmptyData("");
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

    @ApiOperation("分页查询行为日志列表")
    @ApiImplicitParams({})
    @GetMapping("/getOpLogByNameAndPage")
    public ResponseModel getOpLogByNameAndPage(@ModelAttribute UserInfoForToken userInfo, @RequestParam int pageNum, @RequestParam int pageSize, @RequestParam(required = false) String name){
        return ResponseModel.sucess("",opLogService.getOpLogByNameAndPage(pageNum,pageSize,name));
    }


    @ApiOperation(value = "根据id删除行为日志", notes = "")
    @ApiImplicitParams({})
    @GetMapping("/deleteOpLog")
    public ResponseModel deleteOpLog(@ModelAttribute UserInfoForToken userInfo, @RequestParam String opId){
        opLogService.deleteOplog(opId);
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "添加状态", notes = "")
    @ApiImplicitParams({})
    @PostMapping("/saveStatus")
    public ResponseModel saveStatus(@ModelAttribute UserInfoForToken userInfo, @RequestBody Status status){
        backUserService.saveStatus(status);
        return ResponseModel.sucessWithEmptyData("");
    }
}
