package com.jichuangsi.school.timingservice.controller;

import com.jichuangsi.school.timingservice.entity.Role;
import com.jichuangsi.school.timingservice.exception.BackUserException;
import com.jichuangsi.school.timingservice.model.ResponseModel;
import com.jichuangsi.school.timingservice.model.UrlModel;
import com.jichuangsi.school.timingservice.model.UserInfoForToken;
import com.jichuangsi.school.timingservice.service.BackRoleService;
import com.jichuangsi.school.timingservice.service.BackRoleUrlService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Api("关于后台角色权限管理Api")
@RequestMapping("/backrole")
@CrossOrigin
public class BackRoleConsoleController {
    @Resource
    private BackRoleService roleService;
    @Resource
    private BackRoleUrlService backRoleUrlService;

    ////////角色操作

    @ApiOperation("保存角色信息")
    @ApiImplicitParams({})
    @PostMapping("/saveRole")
    public ResponseModel saveRole(@ModelAttribute UserInfoForToken userInfo, @RequestBody Role role){
        roleService.saveRole(userInfo,role);
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation("修改角色信息")
    @ApiImplicitParams({})
    @PostMapping("/updateRole")
    public ResponseModel updateRole(@ModelAttribute UserInfoForToken userInfo,@RequestBody Role role){
        roleService.saveRole(userInfo,role);
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation("删除角色信息")
    @ApiImplicitParams({})
    @GetMapping("/deleteRole/{roleId}")
    public ResponseModel deleteRole(@ModelAttribute UserInfoForToken userInfo,@PathVariable String roleId){
        try {
            roleService.deleteRole(userInfo,roleId);
            return ResponseModel.sucessWithEmptyData("");
        }catch (BackUserException e){
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation("查询角色列表")
    @ApiImplicitParams({})
    @GetMapping("/getRoleList")
    public ResponseModel getRoleList(@ModelAttribute UserInfoForToken userInfo){
        return ResponseModel.sucess("",roleService.getRoleList());
    }

    /////父级分类模块操作

    @ApiOperation("查询模块列表")
    @ApiImplicitParams({})
    @GetMapping("/getUsemoduleList")
    public ResponseModel getUsemoduleList(@ModelAttribute UserInfoForToken userInfo){
        return ResponseModel.sucess("",backRoleUrlService.getAllUseModule());
    }

    @ApiOperation("查询静态页面列表")
    @ApiImplicitParams({})
    @GetMapping("/getStaticPageList")
    public ResponseModel getStaticPageList(@ModelAttribute UserInfoForToken userInfo){
        return ResponseModel.sucess("",backRoleUrlService.getAllStaticPage());
    }

    /*@ApiOperation("多条件分页查询url列表")
    @ApiImplicitParams({})
    @GetMapping("/getRoleUrlListByPage")
    public ResponseModel<Page<RoleUrl>> getRoleUrlListByPage(@ModelAttribute UserInfoForToken userInfo,String useWayId, String urlName, int pageNum, int pageSize){
        return ResponseModel.sucess("",backRoleUrlService.getRoleUrlListByPage(pageNum,pageSize,useWayId,urlName));
    }*/

    @ApiOperation("查询全部url列表")
    @ApiImplicitParams({})
    @GetMapping("/getRoleUrlList")
    public ResponseModel getRoleUrlList(@ModelAttribute UserInfoForToken userInfo){
        //return ResponseModel.sucess("",MappingEntity2ModelCoverter.CONVERTERFROMROLEURLTOROLEURLMODEL(backRoleUrlService.getRoleUrlList()));
        //return ResponseModel.sucess("",backRoleUrlService.getAllRoleUrl());
        return ResponseModel.sucess("",backRoleUrlService.getAllRoleUrl());
    }

    /*@ApiOperation("查询全部url列表")
    @ApiImplicitParams({})
    @GetMapping("/getAllRoleUrlList")
    public ResponseModel getAllRoleUrlList(@ModelAttribute UserInfoForToken userInfo){
        return ResponseModel.sucess("",backRoleUrlService.getRoleUrlList());
    }*/

    /*@ApiOperation("查询角色对应url列表")
    @ApiImplicitParams({})
    @GetMapping("/getUrlByRoleId/{roleId}")
    public ResponseModel getUrlByRoleId(@ModelAttribute UserInfoForToken userInfo,@PathVariable String roleId){
        return ResponseModel.sucess("",backRoleUrlService.getUrlByRoleId(roleId));
    }*/

    @ApiOperation(value = "批量添加角色可以访问的url", notes = "")
    @ApiImplicitParams({})
    @PostMapping("/batchAddRoleUrl")
    public ResponseModel batchAddUrlRelation(@ModelAttribute UserInfoForToken userInfo,@RequestBody UrlModel urlModel){
        try {
            backRoleUrlService.batchInsertUrlRelation(userInfo,urlModel.getUrlRelations());
        }catch (Exception e){
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "根据id批量删除角色相关url", notes = "")
    @ApiImplicitParams({})
    @PostMapping("/batchDeleteRoleUrl")
    public ResponseModel batchDeleteRoleUrl(@ModelAttribute UserInfoForToken userInfo,@RequestBody UrlModel urlModel){
        try {
            backRoleUrlService.batchDeleteRoleUrl(userInfo,urlModel.getUrlRelations());
        }catch (Exception e){
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation("查询角色对应url列表")
    @ApiImplicitParams({})
    @GetMapping("/getAllRoleUrlByRoleId/{roleId}")
    public ResponseModel getAllRoleUrlByRoleId(@ModelAttribute UserInfoForToken userInfo,@PathVariable String roleId){
        //return ResponseModel.sucess("",backRoleUrlService.getAllRoleUrlModel(roleId));
        return ResponseModel.sucess("",backRoleUrlService.getUrlByRoleId(roleId));
    }

    @ApiOperation("查询角色对应模块列表")
    @ApiImplicitParams({})
    @GetMapping("/getModelAndStaticPageByRoleId/{roleId}")
    public ResponseModel getModelAndStaticPageByRoleId(@PathVariable String roleId){
        try {
            return ResponseModel.sucess("",backRoleUrlService.getModelAndStaticPageByRoleId(roleId));
        }catch (BackUserException e){
            return ResponseModel.fail("",e.getMessage());
        }
    }
    @ApiOperation("查询角色对应url列表")
    @ApiImplicitParams({})
    @GetMapping("/getModelAndStaticPageByRoleId")
    public ResponseModel getModelAndStaticPageByRoleId(@RequestParam String roleId,@RequestParam String pageId){
        try {
            return ResponseModel.sucess("",backRoleUrlService.getStaticPageAndRoleUrlByRoleId(roleId,pageId));
        }catch (BackUserException e){
            return ResponseModel.fail("",e.getMessage());
        }
    }
}
