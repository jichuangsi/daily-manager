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
        staffConsoleService.updateStatusById(userInfo,statusId,wechat);
        return ResponseModel.sucessWithEmptyData("");
    }
    @ApiOperation("修改员工部门信息")
    @ApiImplicitParams({})
    @GetMapping("/updateStaffDept")
    public ResponseModel updateStaffDept(@ModelAttribute UserInfoForToken userInfo, @RequestParam String wechat, @RequestParam String deptId){
        staffConsoleService.updateDeptById(userInfo,deptId, wechat);
        return ResponseModel.sucessWithEmptyData("");
    }
    @ApiOperation("修改员工角色信息")
    @ApiImplicitParams({})
    @GetMapping("/updateStaffRole")
    public ResponseModel updateStaffRole(@ModelAttribute UserInfoForToken userInfo, @RequestParam String wechat, @RequestParam String roleId){
        staffConsoleService.updateRoleById(userInfo,roleId,wechat);
        return ResponseModel.sucessWithEmptyData("");
    }
}
