package com.jichuangsi.school.timingservice.controller;

import com.jichuangsi.school.timingservice.entity.Staff;
import com.jichuangsi.school.timingservice.exception.BackUserException;
import com.jichuangsi.school.timingservice.exception.StaffHttpException;
import com.jichuangsi.school.timingservice.model.HttpTokenModel;
import com.jichuangsi.school.timingservice.model.ResponseModel;
import com.jichuangsi.school.timingservice.model.UserInfoForToken;
import com.jichuangsi.school.timingservice.model.WxLoginModel;
import com.jichuangsi.school.timingservice.service.BackUserService;
import com.jichuangsi.school.timingservice.service.StaffConsoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Api("关于员工Api")
@RequestMapping("/staff")
@CrossOrigin
public class StaffConsoleController {
    @Resource
    private StaffConsoleService staffConsoleService;

    @ApiOperation(value = "获取wx_token", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/getWxToken/{code}")
    public ResponseModel<HttpTokenModel> getWxToken(@PathVariable String code){
        try {
            return ResponseModel.sucess("",staffConsoleService.findTokenByCode(code));
        } catch (StaffHttpException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }
    @ApiOperation(value = "员工通过微信openId注册登录，返回token(请求token为固定token即可)", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/loginStaff/{opendId}")
    public ResponseModel loginStaff(@PathVariable String opendId){
        try {
            return ResponseModel.sucess("",staffConsoleService.loginStaff(opendId)) ;
        } catch (StaffHttpException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "查询员工是否存在", notes = "")
    @ApiImplicitParams({})
    @GetMapping(value = "/checkStaff/{opendId}")
    public ResponseModel checkStaff(@PathVariable String opendId){
        try {
            return ResponseModel.sucess("",staffConsoleService.checkStaff(opendId)) ;
        } catch (StaffHttpException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation("员工注册")
    @ApiImplicitParams({})
    @PostMapping("/registStaff")
    public ResponseModel saveStaff(@RequestBody WxLoginModel model){
        try {
            return ResponseModel.sucess("",staffConsoleService.registStaff(model));
        }catch (StaffHttpException e){
            return ResponseModel.fail("",e.getMessage());
        }catch (BackUserException e){
            return ResponseModel.fail("",e.getMessage());
        }
    }
}
