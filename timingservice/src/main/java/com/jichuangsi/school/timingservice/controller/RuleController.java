package com.jichuangsi.school.timingservice.controller;

import com.jichuangsi.school.timingservice.constant.ResultCode;
import com.jichuangsi.school.timingservice.entity.Rule;
import com.jichuangsi.school.timingservice.entity.RuleFather;
import com.jichuangsi.school.timingservice.model.ResponseModel;
import com.jichuangsi.school.timingservice.model.RuleModel2;
import com.jichuangsi.school.timingservice.service.RuleService;
import com.jichuangsi.school.timingservice.utils.TimeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
//@RequestParam String timeString,@RequestParam String wifiName,@RequestParam String longitudeLatitude,@RequestParam String stuas,@RequestParam String wucha
    @ApiOperation(value = "设置规则", notes = "")
    @PostMapping("/ruleset")
    public ResponseModel<String> ruleSet(@RequestBody List<RuleModel2> rules){

        ruleService.cleanFather();
        for (RuleModel2 r2 :rules
             ) {
            long time = TimeUtils.gettime(r2.getTime());
            ruleService.insertRule(time,r2.getWifiName(),r2.getLongitudeLatitude(),r2.getStuas(),r2.getWucha());
        }
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

    @ApiOperation(value = "根据ruleid修改今日规则", notes = "")
    @PostMapping("/updaterule")
    public ResponseModel<String> updateRule(@RequestBody RuleModel2 rule){
        try{
            Rule rule1 = new Rule();
            rule1.setId(Integer.parseInt(rule.getId()));
            rule1.setLongitudeLatitude(rule.getLongitudeLatitude());
            rule1.setStuas(rule.getStuas());
            rule1.setTime(TimeUtils.gettime(rule.getTime())+TimeUtils.todayMorning());
            rule1.setWucha(rule.getWucha());
            rule1.setWifiName(rule.getWifiName());
            ruleService.updateRule(rule1);
            return ResponseModel.sucess("",ResultCode.SUCESS);
        }catch (Exception e){
            return ResponseModel.fail("", ResultCode.PARAM_ERR);
        }
    }
    @ApiOperation(value = "添加今日规则", notes = "")
    @PostMapping("/insertrule")
    public ResponseModel<String> insertRule(@RequestBody RuleModel2 rule ){
        try{
            Rule rule1 = new Rule();
            rule1.setLongitudeLatitude(rule.getLongitudeLatitude());
            rule1.setStuas(rule.getStuas());
            rule1.setTime(TimeUtils.gettime(rule.getTime())+TimeUtils.todayMorning());
            rule1.setWucha(rule.getWucha());
            rule1.setWifiName(rule.getWifiName());
            ruleService.insertRule(rule1);
            return ResponseModel.sucess("",ResultCode.SUCESS);
        }catch (Exception e){
            return ResponseModel.fail("", ResultCode.PARAM_ERR);
        }
    }

    @ApiOperation(value = "删除模板规则", notes = "")
    @PostMapping("/delrule")
    public ResponseModel<String> delRule(@RequestParam String ruleFatherId){
        try{
            ruleService.delRule(ruleFatherId);
            return ResponseModel.sucess("",ResultCode.SUCESS);
        }catch (Exception e){
            return ResponseModel.fail("", ResultCode.PARAM_ERR);
        }
    }
    @ApiOperation(value = "获取模板规则", notes = "")
    @PostMapping("/getrulefatherlist")
    public ResponseModel<List<RuleFather>> getRuleFatherList(){
        try{
            return ResponseModel.sucess("",ruleService.getRuleFatherList());
        }catch (Exception e){
            return ResponseModel.fail("", ResultCode.PARAM_ERR);
        }
    }

    @ApiOperation(value = "启停模板规则", notes = "")
    @PostMapping("/rulefatherstopandstart")
    public ResponseModel<String> ruleFatherStopAndStart(@RequestBody RuleFather ruleFather){
        try{
            ruleService.ruleFatherStopAndStart(ruleFather);
            return ResponseModel.sucess("","ok");
        }catch (Exception e){
            return ResponseModel.fail("", ResultCode.PARAM_ERR);
        }
    }
}
