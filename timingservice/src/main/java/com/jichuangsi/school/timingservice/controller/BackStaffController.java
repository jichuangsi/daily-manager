package com.jichuangsi.school.timingservice.controller;

import com.jichuangsi.school.timingservice.entity.Staff;
import com.jichuangsi.school.timingservice.exception.BackUserException;
import com.jichuangsi.school.timingservice.model.ResponseModel;
import com.jichuangsi.school.timingservice.model.UserInfoForToken;
import com.jichuangsi.school.timingservice.service.StaffConsoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Api("关于后台角色权限管理Api")
@RequestMapping("/backstaff")
@CrossOrigin
public class BackStaffController {
    @Resource
    private StaffConsoleService staffConsoleService;

    @ApiOperation("多条件分页查询员工列表")
    @ApiImplicitParams({})
    @GetMapping("/getStaffListByPage")
    public ResponseModel<Page<Staff>> getStaffListByPage(@ModelAttribute UserInfoForToken userInfo, @RequestParam(required = false)String statusId, @RequestParam(required = false)String staffName,@RequestParam(required = false)String deptId, @RequestParam int pageNum, @RequestParam int pageSize){
        try {
            return ResponseModel.sucess("",staffConsoleService.getStaffListByPage(userInfo,pageNum,pageSize,staffName,statusId,deptId));
        }catch (BackUserException e){
            return ResponseModel.fail("",e.getMessage());
        }

    }

    @ApiOperation("查询状态列表")
    @ApiImplicitParams({})
    @GetMapping("/getStatusList")
    public ResponseModel getRoleUrlListByPage(@ModelAttribute UserInfoForToken userInfo){
        return ResponseModel.sucess("",staffConsoleService.getStatusList());
    }

    @ApiOperation("修改员工状态信息")
    @ApiImplicitParams({})
    @GetMapping("/updateStaff")
    public ResponseModel updateStaffStatus(@ModelAttribute UserInfoForToken userInfo, @RequestParam String wechat, @RequestParam String statusId){
        try {
            staffConsoleService.updateStatusById(userInfo,statusId,wechat);
        }catch (BackUserException e){
            return  ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }
    @ApiOperation("修改员工部门信息")
    @ApiImplicitParams({})
    @GetMapping("/updateStaffDept")
    public ResponseModel updateStaffDept(@ModelAttribute UserInfoForToken userInfo, @RequestParam String wechat, @RequestParam String deptId){
        try {
            staffConsoleService.updateDeptById(userInfo,deptId, wechat);
        }catch (BackUserException e){
            return  ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }
    @ApiOperation("修改员工角色信息")
    @ApiImplicitParams({})
    @GetMapping("/updateStaffRole")
    public ResponseModel updateStaffRole(@ModelAttribute UserInfoForToken userInfo, @RequestParam String wechat, @RequestParam String roleId){
        try {
            staffConsoleService.updateRoleById(userInfo,roleId,wechat);
        }catch (BackUserException e){
            return  ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation("修改员工个人信息")
    @ApiImplicitParams({})
    @PostMapping("/updateStaffInfo")
    public ResponseModel updateStaffInfo(@ModelAttribute UserInfoForToken userInfo, @RequestBody Staff staff){
        try {
            staffConsoleService.updateStaff(userInfo,staff);
        }catch (BackUserException e){
            return  ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation("修改员工密码")
    @ApiImplicitParams({})
    @GetMapping("/updateStaffPwd")
    public ResponseModel updateStaffPwd(@ModelAttribute UserInfoForToken userInfo, @RequestParam String openid,@RequestParam String pwd){
        try {
            staffConsoleService.updateStaffPwd(userInfo,openid,pwd);
        }catch (BackUserException e){
            return  ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation("删除员工（全部删除）")
    @ApiImplicitParams({})
    @PostMapping("/deleteStaffInfo/{opendId}")
    public ResponseModel deleteStaffInfo(@ModelAttribute UserInfoForToken userInfo,@PathVariable String opendId){
        try {
            staffConsoleService.deleteUser(userInfo,opendId);
        }catch (BackUserException e){
            return  ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation("冻结员工")
    @ApiImplicitParams({})
    @PostMapping("/frozenStaffInfo/{opendId}")
    public ResponseModel frozenStaffInfo(@ModelAttribute UserInfoForToken userInfo,@PathVariable String opendId){
        try {
            staffConsoleService.frozenStaffInfo(userInfo,opendId);
        }catch (BackUserException e){
            return  ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation("恢复员工")
    @ApiImplicitParams({})
    @PostMapping("/thawStaffInfo/{opendId}")
    public ResponseModel thawStaffInfo(@ModelAttribute UserInfoForToken userInfo,@PathVariable String opendId){
        try {
            staffConsoleService.thawStaffInfo(userInfo,opendId);
        }catch (BackUserException e){
            return  ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }
}
