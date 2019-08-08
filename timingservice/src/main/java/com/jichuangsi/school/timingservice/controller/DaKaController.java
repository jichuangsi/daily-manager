package com.jichuangsi.school.timingservice.controller;

import com.jichuangsi.school.timingservice.constant.ResultCode;
import com.jichuangsi.school.timingservice.entity.Department;
import com.jichuangsi.school.timingservice.entity.People;
import com.jichuangsi.school.timingservice.entity.Record;
import com.jichuangsi.school.timingservice.entity.Rule;
import com.jichuangsi.school.timingservice.model.*;
import com.jichuangsi.school.timingservice.repository.IDepartmentRepository;
import com.jichuangsi.school.timingservice.service.PeopleService;
import com.jichuangsi.school.timingservice.service.RecordService;
import com.jichuangsi.school.timingservice.service.RuleService;
import com.jichuangsi.school.timingservice.utils.TimeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/kq")
@CrossOrigin
@Api("考情相关的api")
public class DaKaController {
    @Resource
    private IDepartmentRepository iDepartmentRepository;

    @Resource
    private RecordService recordService;

    @Resource
    private RuleService ruleService;

    @Resource
    private PeopleService peopleService;


    @ApiOperation(value = "给参数打卡", notes = "")
    @PostMapping("/daka")
    public ResponseModel<List<String>> daKa(@RequestParam String wifiName,@RequestParam String longitudeLatitude,@RequestParam String openId,@RequestParam String ruleId){
        String wifi=ruleService.findWifi();
        String peopleName=peopleService.findPeopleName(openId);
        String lL=ruleService.findLL();
        Rule rule = ruleService.getRuleById(Integer.parseInt(ruleId));
        String stuas="0";

        String c="c0";
        String z="z0";
        String w="w0";
        String d="d0";
        StringBuilder sb = new StringBuilder();
        List<String> strings = new ArrayList<>();

        if (rule==null){
            rule=ruleService.getLastRule();
        }

        if (peopleName==""||peopleName==null){
            String s= "查无此人";
            ResponseModel<List<String>> listResponseModel = new ResponseModel<List<String>>();
            listResponseModel.setMsg(s);
            return listResponseModel;
        }
        if (wifi.equalsIgnoreCase(wifiName)){
            if (rule.getStuas().equalsIgnoreCase("1")&&rule.getTime()<=System.currentTimeMillis()){
                c="c";
                String s=" 迟到";
                sb.append(s);
                stuas="2";
            }

            if (rule.getStuas().equalsIgnoreCase("2")&&rule.getTime()>=System.currentTimeMillis()){
                z="z";
                String s=" 早退";
                sb.append(s);
                stuas="3";
            }

        }else {
            String s= " wifi不对";
            sb.append(s);
            w="w";

           if (!longitudeLatitude.equalsIgnoreCase(",")){
               String[] LL=lL.split(",");
               String[] ll=longitudeLatitude.split(",");
               double wd=Double.parseDouble(LL[0]);
               double jd=Double.parseDouble(LL[1]);
               double gwd=Double.parseDouble(ll[0]);
               double gjd=Double.parseDouble(ll[1]);

            if((gwd>=(wd-Double.parseDouble(rule.getWucha())*0.00001)&&gwd<=(wd+Double.parseDouble(rule.getWucha())*0.00001))&&gjd>=(jd-Double.parseDouble(rule.getWucha())*0.00001)&&gjd<=(jd+Double.parseDouble(rule.getWucha())*0.00001)){
                if (rule.getStuas().equalsIgnoreCase("1")&&rule.getTime()<=System.currentTimeMillis()){
                    c="c";
                    String s2=" 迟到";
                    sb.append(s2);
                    stuas="2";
                }

                if (rule.getStuas().equalsIgnoreCase("2")&&rule.getTime()>=System.currentTimeMillis()){
                    z="z";
                    String s3=" 早退";
                    sb.append(s3);
                    stuas="3";
                }
            }else {
                d="d";
                stuas="1";
            }
            }else {
                String s1=" 定位不对" ;
                sb.append(s1);
                d="d";
                stuas="1";
            }
        }
        recordService.daKa( peopleName ,wifiName,longitudeLatitude,stuas,openId,rule.getId().toString());
        strings.add(stuas);
        strings.add(c);
        strings.add(z);
        strings.add(w);
        strings.add(d);

        ResponseModel<List<String>> listResponseModel = new ResponseModel<List<String>>();
        listResponseModel.setMsg(sb.toString());
        listResponseModel.setData(strings);
        return listResponseModel;
    }

    @ApiOperation(value = "根据openid查询个人今日打卡记录", notes = "")
    @PostMapping("/getdakarecord")
    public ResponseModel<List<Record>> getDaKaRecord(@RequestParam String openId){

        ResponseModel<List<Record>> listResponseModel = new ResponseModel<>();
        listResponseModel.setData(recordService.findAllByOpenId(openId));
        return listResponseModel;
    }

    @ApiOperation(value = "今日排行榜", notes = "")
    @PostMapping("/getdakapaixingbang")
    public ResponseModel<List<RecordModel>> getDaKaPxb(){
        List<Record> records=recordService.findPxcPeopleOpenid();
        List<Record> pxb=new ArrayList<>();
        for (Record r:records
             ) {
                if (r.getStuas().equalsIgnoreCase("0")&&pxb.size()==0){
                    pxb.add(r);
                }
                if (pxb.size()!=0&&r.getStuas().equalsIgnoreCase("0")){
                    int n=0;
                    for (Record record:pxb
                         ) {
                        boolean b = record.getOpenId() == r.getOpenId();
                        if (!b){
                            n++;
                        }
                    }
                    if (n==pxb.size()){
                        pxb.add(r);
                    }
                }
                if (pxb.size()==10){
                    break;
                }
            }
        List<RecordModel> people = new ArrayList<>();
        for (Record r:pxb
             ) {
            People people1 = peopleService.findPeople(r.getOpenId());
            RecordModel recordModel = new RecordModel();
            recordModel.setPeople(people1);
            recordModel.setTime(r.getTime());
            people.add(recordModel);
        }
        ResponseModel<List<RecordModel>> listResponseModel = new ResponseModel<>();
        listResponseModel.setData(people);
        listResponseModel.setCode(ResultCode.SUCESS);
        return listResponseModel;
    }

    @ApiOperation(value = "个人读取今日规则含状态", notes = "")
    @PostMapping("/getrulelist")
    public ResponseModel<List<RuleModel>> getRuleListStuas(@RequestParam String openId){
        try {

            ResponseModel<List<RuleModel>> stringResponseModel = new ResponseModel<>();
            List<RuleModel> rulelist = new ArrayList<>();
            List<Rule> rules = ruleService.getRulelist();
            for (Rule r:rules
                    ) {
                if(r.getStuas().equalsIgnoreCase("1")){
                    RuleModel ruleModel = new RuleModel();
                    List<Record> stuas = recordService.findAllByOpenIdAndRuleIdAndStuas(openId, r.getId().toString());
                    if (stuas!=null){
                        for (Record r789 :stuas
                                ) {
                            if (r789.getStuas().equalsIgnoreCase("2")){
                                ruleModel.setStuas2(r789.getStuas());
                            }
                            if (r789.getStuas().equalsIgnoreCase("0")){
                                ruleModel.setStuas2(r789.getStuas());
                                break;
                            }
                        }
                    }
                    ruleModel.setStuas(r.getStuas());
                    ruleModel.setId(r.getId());
                    ruleModel.setLongitudeLatitude(r.getLongitudeLatitude());
                    ruleModel.setTime(r.getTime());
                    ruleModel.setWifiName(r.getWifiName());
                    rulelist.add(ruleModel);
                }else {
                    RuleModel ruleModel = new RuleModel();
                    List<Record> stuas = recordService.findAllByOpenIdAndRuleIdAndStuas2(openId, r.getId().toString());
                    if (stuas!=null){
                        for (Record r789 :stuas
                                ) {
                            if (r789.getStuas().equalsIgnoreCase("3")){
                                ruleModel.setStuas2(r789.getStuas());
                            }
                            if (r789.getStuas().equalsIgnoreCase("0")){
                                ruleModel.setStuas2(r789.getStuas());
                                break;
                            }
                        }
                    }
                    ruleModel.setStuas(r.getStuas());
                    ruleModel.setId(r.getId());
                    ruleModel.setLongitudeLatitude(r.getLongitudeLatitude());
                    ruleModel.setTime(r.getTime());
                    ruleModel.setWifiName(r.getWifiName());
                    rulelist.add(ruleModel);
                }
            }
            stringResponseModel.setData(rulelist);
            stringResponseModel.setCode(ResultCode.SUCESS);
            return stringResponseModel;
        }catch (Exception e){
            return ResponseModel.fail("");
        }


    }


    @ApiOperation(value = "做报表", notes = "")
    @PostMapping("/getBB")
    public ResponseModel<List<ReportFormModel>> getGRBB(@RequestParam String timeStart, @RequestParam String timeEnd, @RequestParam @Nullable String name){
        if (name==null||name.equalsIgnoreCase("")){
            try{
                List<People> peopleList = peopleService.findAll();
                List<ReportFormModel> models = new ArrayList<>();
                for ( People people:peopleList
                        ) {
                    ReportFormModel rModel = new ReportFormModel();
                    List<Record> allByOpenIdAndStuas = recordService.findAllByOpenIdAndStuasAndTimeBetween(people.getOpenId(),TimeUtils.startTime(timeStart),TimeUtils.startTime(timeEnd));
                    List<Rule> ruleForTime = ruleService.getRuleForTime(TimeUtils.startTime(timeStart), TimeUtils.endTime(timeEnd));
//                    int time = (int) (timeEnd - timeStart) / (24 * 60 * 60);
                    int qq=ruleForTime.size()-allByOpenIdAndStuas.size();
                    rModel.setQq(qq);
                    rModel.setKq(allByOpenIdAndStuas.size());

                    Department byid = iDepartmentRepository.findByid(people.getDepartment());
                    rModel.setDepartment(byid.getDeptname());
                    rModel.setJurisdiction(people.getJurisdiction());
                    rModel.setPeopleName(people.getPeopleName());
                    models.add(rModel);
                }
                return ResponseModel.sucess("",models);
            }catch (Exception e){
                return ResponseModel.fail("",ResultCode.SYS_ERROR);
            }
        }else {
            try{
                List<People> peopleList = peopleService.findAllPeople(name);
                List<ReportFormModel> models = new ArrayList<>();
                for ( People people:peopleList
                        ) {
                    ReportFormModel rModel = new ReportFormModel();
                    List<Record> allByOpenIdAndStuas = recordService.findAllByOpenIdAndStuasAndTimeBetween(people.getOpenId(),TimeUtils.startTime(timeStart),TimeUtils.startTime(timeEnd));
                    List<Rule> ruleForTime = ruleService.getRuleForTime(TimeUtils.startTime(timeStart), TimeUtils.endTime(timeEnd));
//                    int time = (int) (timeEnd - timeStart) / (24 * 60 * 60);
                    int qq=ruleForTime.size()-allByOpenIdAndStuas.size();
                    rModel.setQq(qq);
                    rModel.setKq(allByOpenIdAndStuas.size());
                    Department byid = iDepartmentRepository.findByid(people.getDepartment());
                    rModel.setDepartment(byid.getDeptname());
                    rModel.setJurisdiction(people.getJurisdiction());
                    rModel.setPeopleName(people.getPeopleName());
                    models.add(rModel);
                }
                return ResponseModel.sucess("",models);
            }catch (Exception e){
                return ResponseModel.fail("",ResultCode.SYS_ERROR);
            }
        }
    }

    @ApiOperation(value = "今日报表", notes = "")
    @PostMapping("/getTDBB")
    public ResponseModel<List<ReportFormModel2>> getTDBB(){
        try{
            List<People> peopleList = peopleService.findAll();
            List<ReportFormModel2> models = new ArrayList<>();
            for ( People people:peopleList
                    ) {

                List<Record> allByOpenIdAndStuas = recordService.findAllByOpenIdAndStuas(people.getOpenId());

                List<Rule> rulelistForTime = ruleService.getRulelistForTime();
                if (allByOpenIdAndStuas.size()==rulelistForTime.size()){
                    for (Rule rule :rulelistForTime
                         ) {
                        ReportFormModel2 rModel = new ReportFormModel2();
                        rModel.setStuas(rule.getStuas());
                        rModel.setStuas2("0");
                        rModel.setTime(rule.getTime());


                        Department byid = iDepartmentRepository.findByid(people.getDepartment());
                        rModel.setDepartment(byid.getDeptname());
                        rModel.setJurisdiction(people.getJurisdiction());
                        rModel.setPeopleName(people.getPeopleName());
                        models.add(rModel);
                    }
                }else {
                    for (Rule rule:rulelistForTime
                         ) {
                        int i=0;
                        for (Record record :allByOpenIdAndStuas
                             ) {
                            if (rule.getTime()==record.getTime()){
                                    i++;
                            }
                        }
                        if (i==0){
                            ReportFormModel2 rModel = new ReportFormModel2();
                            rModel.setStuas(rule.getStuas());
                            rModel.setStuas2("1");
                            rModel.setTime(rule.getTime());
                            Department byid = iDepartmentRepository.findByid(people.getDepartment());
                            rModel.setDepartment(byid.getDeptname());
                            rModel.setJurisdiction(people.getJurisdiction());
                            rModel.setPeopleName(people.getPeopleName());
                            models.add(rModel);
                        } else {
                            ReportFormModel2 rModel = new ReportFormModel2();
                            rModel.setStuas(rule.getStuas());
                            rModel.setStuas2("0");
                            rModel.setTime(rule.getTime());
                            Department byid = iDepartmentRepository.findByid(people.getDepartment());
                            rModel.setDepartment(byid.getDeptname());
                            rModel.setJurisdiction(people.getJurisdiction());
                            rModel.setPeopleName(people.getPeopleName());
                            models.add(rModel);
                        }
                    }
                }
//                rModel.setDepartment(people.getDepartment());
//                rModel.setJurisdiction(people.getJurisdiction());
//                rModel.setPeopleName(people.getPeopleName());
//                models.add(rModel);
            }
            return ResponseModel.sucess("",models);
        }catch (Exception e){
            return ResponseModel.fail("",ResultCode.SYS_ERROR);
        }
    }
}
