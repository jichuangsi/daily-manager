package com.jichuangsi.school.timingservice.controller;

import com.jichuangsi.school.timingservice.constant.ResultCode;
import com.jichuangsi.school.timingservice.entity.Rule;
import com.jichuangsi.school.timingservice.model.ResponseModel;
import com.jichuangsi.school.timingservice.service.RuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/rule")
@CrossOrigin
@Api("规则相关的api")
public class RuleController {
    @Resource
    private RuleService ruleService;

    @ApiOperation(value = "设置规则", notes = "")
    @PostMapping("/ruleset")
    public ResponseModel<String> ruleSet(@RequestParam Long time,@RequestParam String wifiName,@RequestParam String longitudeLatitude,@RequestParam String stuas,@RequestParam String wucha){
        ruleService.cleanFather();
        ruleService.insertRule(time,wifiName,longitudeLatitude,stuas,wucha);
        ResponseModel<String> stringResponseModel = new ResponseModel<>();
        stringResponseModel.setMsg("ok");
        return stringResponseModel;
    }

    @ApiOperation(value = "读取今日规则", notes = "")
    @PostMapping("/getrulelist")
    public ResponseModel<List<Rule>> getRuleList(){

        ResponseModel<List<Rule>> stringResponseModel = new ResponseModel<>();
        List<Rule> rulelist = ruleService.getRulelist();
        stringResponseModel.setData(rulelist);
        stringResponseModel.setCode(ResultCode.SUCESS);
        return stringResponseModel;
    }

    @ApiOperation(value = "根据openid修改今日规则", notes = "")
    @PostMapping("/updaterule")
    public ResponseModel<String> updateRule(@RequestBody Rule rule){
        try{
            ruleService.updateRule(rule);
            return ResponseModel.sucess("",ResultCode.SUCESS);
        }catch (Exception e){
            return ResponseModel.fail("", ResultCode.PARAM_ERR);
        }
    }

}
