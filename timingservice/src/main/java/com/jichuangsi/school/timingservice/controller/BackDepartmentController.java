package com.jichuangsi.school.timingservice.controller;

import com.jichuangsi.school.timingservice.entity.Department;
import com.jichuangsi.school.timingservice.exception.BackUserException;
import com.jichuangsi.school.timingservice.model.ResponseModel;
import com.jichuangsi.school.timingservice.model.UserInfoForToken;
import com.jichuangsi.school.timingservice.service.BackDepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Api("关于后台部门管理Api")
@RequestMapping("/backdepartment")
@CrossOrigin
public class BackDepartmentController {
    @Resource
    private BackDepartmentService departmentService;

    @ApiOperation("保存部门信息")
    @ApiImplicitParams({})
    @PostMapping("/saveDepartment")
    public ResponseModel saveDepartment(@ModelAttribute UserInfoForToken userInfo,@RequestBody Department department){
        try{
            departmentService.saveDepartment(userInfo,department);
        }catch (BackUserException e){
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation("修改部门信息")
    @ApiImplicitParams({})
    @PostMapping("/updateDepartment")
    public ResponseModel updateDepartment(@ModelAttribute UserInfoForToken userInfo,@RequestBody Department department){
        try{
            departmentService.updateDepartment(userInfo,department);
        }catch (BackUserException e){
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }
    @ApiOperation("删除部门信息")
    @ApiImplicitParams({})
    @GetMapping("/deleteDepartment/{deptId}")
    public ResponseModel deleteDepartment(@ModelAttribute UserInfoForToken userInfo,@PathVariable String deptId){
        try {
            departmentService.deleteDepartment(userInfo,deptId);
            return ResponseModel.sucessWithEmptyData("");
        }catch (BackUserException e){
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation("查询部门列表")
    @ApiImplicitParams({})
    @GetMapping("/getDepartmentList")
    public ResponseModel getDepartmentList(@ModelAttribute UserInfoForToken userInfo){
        return ResponseModel.sucess("",departmentService.getDepartmentList());
    }
}
