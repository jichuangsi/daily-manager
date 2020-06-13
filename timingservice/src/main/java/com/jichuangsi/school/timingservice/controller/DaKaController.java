package com.jichuangsi.school.timingservice.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.util.StringUtil;
import com.jichuangsi.school.timingservice.constant.ResultCode;
import com.jichuangsi.school.timingservice.dao.mapper.StaffMapper;
import com.jichuangsi.school.timingservice.entity.*;
import com.jichuangsi.school.timingservice.model.*;
import com.jichuangsi.school.timingservice.repository.IDepartmentRepository;
import com.jichuangsi.school.timingservice.repository.IRoleRepository;
import com.jichuangsi.school.timingservice.service.*;
import com.jichuangsi.school.timingservice.utils.ListUtils;
import com.jichuangsi.school.timingservice.utils.PoiUtils;
import com.jichuangsi.school.timingservice.utils.TimeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
@RequestMapping("/kq")
@CrossOrigin
@Api("考情相关的api")
public class DaKaController {
    @Resource
    private IDepartmentRepository iDepartmentRepository;
    @Resource
    private IRoleRepository roleRepository;
    @Resource
    private BackRoleService backRoleService;
    @Resource
    private BackUserService backUserService;

    @Resource
    private RecordService recordService;

    @Resource
    private RuleService ruleService;

    @Resource
    private PeopleService peopleService;

    @Resource
    private StaffMapper staffMapper;

    @Value("${com.jichuangsi.school.file}")
    private String filePath;


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
            }else {
                String s1=" 定位不对" ;
                sb.append(s1);
                d="d";
                stuas="1";
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
    public ResponseModel<List<ReportFormModel>> getGRBB(@ModelAttribute UserInfoForToken userInfoForToken,@RequestParam String timeStart, @RequestParam String timeEnd, @RequestParam @Nullable String name,@RequestParam @Nullable String deptId,@RequestParam int pageSize ,@RequestParam @Nullable int pageNum){

        if (name==null||name.equalsIgnoreCase("")){
            try{
                BackUser user=backUserService.getBackUserById(userInfoForToken.getUserId());
                Role role=roleRepository.findByid(user.getRoleId());
                List<People> peopleList=null;
                List<ReportFormModel> models = new ArrayList<>();
                ResponseModel<List<ReportFormModel>> listResponseModel=new ResponseModel<>();
                if (user.getRoleName().equals("M") || user.getRoleName().equals("院长")){
                    peopleList = peopleService.findAll();
                }else if(user.getRoleName().equals("部长")){
                    peopleList = peopleService.findForD(user.getDeptId());
                }else if(user.getRoleName().equals("副院长")){
                    if(backRoleService.getRoleDepartment(user.getId()).size()==0){
                        peopleList = new ArrayList<>();
                    }else{
                        peopleList = peopleService.findForD(deptId);
                    }
                }else {
                    People people = peopleService.findPeople(user.getWechat());
                    ReportFormModel rModel = new ReportFormModel();
                    List<Record> allByOpenIdAndStuas = recordService.findAllByOpenIdAndStuasAndTimeBetween(people.getOpenId(),TimeUtils.startTime(timeStart),TimeUtils.endTime(timeEnd));
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
                if(peopleList!=null && peopleList.size()!=0){
                    List<Rule> ruleList = ruleService.getRuleForTime(TimeUtils.startTime(timeStart), TimeUtils.endTime(timeEnd));
                    for ( People people:peopleList
                            ) {
                        ReportFormModel rModel = new ReportFormModel();
                        long t1=TimeUtils.startTime(timeStart);
                        long t2=TimeUtils.startTime(timeEnd);
                        List<Record> allByOpenIdAndStuas = recordService.findAllByOpenIdAndStuasAndTimeBetween(people.getOpenId(),TimeUtils.startTime(timeStart),TimeUtils.endTime(timeEnd));
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
                }
                if (models!=null && models.size()!=0){
                    listResponseModel.setData(ListUtils.Pager(pageSize,pageNum,models));
                    listResponseModel.setPageSize(models.size());
                    listResponseModel.setPageNum(pageNum);
                    listResponseModel.setCode(ResultCode.SUCESS);
                }else{
                    listResponseModel.setData(models);
                    listResponseModel.setPageSize(models.size());
                    listResponseModel.setPageNum(pageNum);
                    listResponseModel.setCode(ResultCode.SUCESS);
                }
                return listResponseModel;
            }catch (Exception e){
                return ResponseModel.fail("",ResultCode.SYS_ERROR);
            }
        }else {
            try{
                BackUser user=backUserService.getBackUserById(userInfoForToken.getUserId());
                Role role=roleRepository.findByid(user.getRoleId());
                List<People> peopleList=null;
                List<ReportFormModel> models = new ArrayList<>();
                ResponseModel<List<ReportFormModel>> listResponseModel=new ResponseModel<>();
                if (user.getRoleName().equals("M") || user.getRoleName().equals("院长")){
                    peopleList = peopleService.findAllPeople(name);
                }else if(user.getRoleName().equals("部长")){
                    peopleList = peopleService.findAllPeople(name,user.getDeptId());
                }else if(user.getRoleName().equals("副院长")){
                    if(backRoleService.getRoleDepartment(user.getId()).size()==0){
                        peopleList = new ArrayList<>();
                    }else {
                        peopleList = peopleService.findAllPeople(name,deptId);
                    }
                }else {
                    People people = peopleService.findPeople(user.getWechat());
                    ReportFormModel rModel = new ReportFormModel();
                    List<Record> allByOpenIdAndStuas = recordService.findAllByOpenIdAndStuasAndTimeBetween(people.getOpenId(),TimeUtils.startTime(timeStart),TimeUtils.endTime(timeEnd));
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
                if(peopleList!=null && peopleList.size()!=0){
                    for ( People people:peopleList
                            ) {
                        ReportFormModel rModel = new ReportFormModel();
                        List<Record> allByOpenIdAndStuas = recordService.findAllByOpenIdAndStuasAndTimeBetween(people.getOpenId(),TimeUtils.startTime(timeStart),TimeUtils.endTime(timeEnd));
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
                }
                if (models!=null && models.size()!=0){
                    listResponseModel.setData(ListUtils.Pager(pageSize,pageNum,models));
                    listResponseModel.setPageSize(models.size());
                    listResponseModel.setPageNum(pageNum);
                    listResponseModel.setCode(ResultCode.SUCESS);
                }else{
                    listResponseModel.setData(models);
                    listResponseModel.setPageSize(models.size());
                    listResponseModel.setPageNum(pageNum);
                    listResponseModel.setCode(ResultCode.SUCESS);
                }
                return listResponseModel;
            }catch (Exception e){
                return ResponseModel.fail("",ResultCode.SYS_ERROR);
            }
        }
    }

    @ApiOperation(value = "今日报表", notes = "")
    @PostMapping("/getTDBB")
    public ResponseModel<List<ReportFormModel2>> getTDBB(@ModelAttribute UserInfoForToken userInfoForToken,@RequestParam String timeStart, @RequestParam String timeEnd,@RequestParam(required = false) String name,@RequestParam @Nullable String deptId,@RequestParam int pageSize ,@RequestParam @Nullable int pageNum){
       try {
           BackUser user=backUserService.getBackUserById(userInfoForToken.getUserId());
           if (user.getRoleName().equals("M")||user.getRoleName().equals("院长")) {
               try {
                   List<People> peopleList=null;
                   if(StringUtil.isEmpty(name)||name.equalsIgnoreCase("")){
                       peopleList = peopleService.findAll();
                   }else{
                       peopleList = peopleService.findAllPeople(name);
                   }
                   ResponseModel<List<ReportFormModel2>> listResponseModel=new ResponseModel<>();
                   List<ReportFormModel2> models = new ArrayList<>();
                   //根据日期查询规则
                   List<Rule> rulelistForTime=ruleService.getRuleForTime(TimeUtils.startTime(timeStart),TimeUtils.endTime(timeEnd));
                   for (People people : peopleList) {
                       Department byid = iDepartmentRepository.findByid(people.getDepartment());
                       for (Rule rule:rulelistForTime) {
                           ReportFormModel2 model2List=staffMapper.findAllByOpenIdAndRuleId(people.getOpenId(),rule.getId().toString());
                           if(model2List==null){
                               ReportFormModel2 rModel = new ReportFormModel2();
                               rModel.setStuas(rule.getStuas());
                               rModel.setStuas2("4");
                               rModel.setTime(rule.getTime());
                               rModel.setDepartment(byid.getDeptname());
                               rModel.setJurisdiction(people.getJurisdiction());
                               rModel.setPeopleName(people.getPeopleName());
                               models.add(rModel);
                           }else {
                               models.add(model2List);
                           }
                       }
                   }

                   if (models!=null && models.size()!=0){
                       listResponseModel.setData(ListUtils.Pager(pageSize,pageNum,models));
                       listResponseModel.setPageSize(models.size());
                       listResponseModel.setPageNum(pageNum);
                       listResponseModel.setCode(ResultCode.SUCESS);
                   }else{
                       listResponseModel.setData(models);
                       listResponseModel.setPageSize(models.size());
                       listResponseModel.setPageNum(pageNum);
                       listResponseModel.setCode(ResultCode.SUCESS);
                   }
                   return listResponseModel;
               } catch (Exception e) {
                   return ResponseModel.fail("", ResultCode.SYS_ERROR);
               }
           } else if (user.getRoleName().equals("部长") || user.getRoleName().equals("副院长")){
               try {
                   List<People> peopleList =null;
                   if(StringUtil.isEmpty(name)||name.equalsIgnoreCase("")){
                       peopleList = peopleService.findForD(deptId);
                   }else{
                       peopleList = peopleService.findAllPeople(name,deptId);
                   }
                   if(user.getRoleName().equals("副院长")){
                       if(backRoleService.getRoleDepartment(userInfoForToken.getUserId()).size()==0)
                           peopleList=new ArrayList<>();
                   }
                   ResponseModel<List<ReportFormModel2>> listResponseModel=new ResponseModel<>();
                   List<ReportFormModel2> models = new ArrayList<>();
                   //根据日期查询规则
                   List<Rule> rulelistForTime=ruleService.getRuleForTime(TimeUtils.startTime(timeStart),TimeUtils.endTime(timeEnd));
                   for (People people : peopleList) {
                       Department byid = iDepartmentRepository.findByid(people.getDepartment());
                       for (Rule rule:rulelistForTime) {
                           ReportFormModel2 model2List=staffMapper.findAllByOpenIdAndRuleId(people.getOpenId(),rule.getId().toString());
                           if(model2List==null){
                               ReportFormModel2 rModel = new ReportFormModel2();
                               rModel.setStuas(rule.getStuas());
                               rModel.setStuas2("4");
                               rModel.setTime(rule.getTime());
                               rModel.setDepartment(byid.getDeptname());
                               rModel.setJurisdiction(people.getJurisdiction());
                               rModel.setPeopleName(people.getPeopleName());
                               models.add(rModel);
                           }else {
                               models.add(model2List);
                           }
                       }
                   }

                   if (models!=null && models.size()!=0){
                       listResponseModel.setData(ListUtils.Pager(pageSize,pageNum,models));
                       listResponseModel.setPageSize(models.size());
                       listResponseModel.setPageNum(pageNum);
                       listResponseModel.setCode(ResultCode.SUCESS);
                   }else{
                       listResponseModel.setData(models);
                       listResponseModel.setPageSize(models.size());
                       listResponseModel.setPageNum(pageNum);
                       listResponseModel.setCode(ResultCode.SUCESS);
                   }
                   return listResponseModel;
               } catch (Exception e) {
                   return ResponseModel.fail("", ResultCode.SYS_ERROR);
               }
           }else {
               try {
                   People people = peopleService.findPeople(user.getWechat());
                   ResponseModel<List<ReportFormModel2>> listResponseModel=new ResponseModel<>();
                   List<ReportFormModel2> models = new ArrayList<>();
                   //根据日期查询规则
                   List<Rule> rulelistForTime=ruleService.getRuleForTime(TimeUtils.startTime(timeStart),TimeUtils.endTime(timeEnd));
                   Department byid = iDepartmentRepository.findByid(people.getDepartment());
                   for (Rule rule:rulelistForTime) {
                       ReportFormModel2 model2List=staffMapper.findAllByOpenIdAndRuleId(people.getOpenId(),rule.getId().toString());
                       if(model2List==null){
                           ReportFormModel2 rModel = new ReportFormModel2();
                           rModel.setStuas(rule.getStuas());
                           rModel.setStuas2("4");
                           rModel.setTime(rule.getTime());
                           rModel.setDepartment(byid.getDeptname());
                           rModel.setJurisdiction(people.getJurisdiction());
                           rModel.setPeopleName(people.getPeopleName());
                           models.add(rModel);
                       }else {
                           models.add(model2List);
                       }
                   }
                   if (models!=null && models.size()!=0){
                       listResponseModel.setData(ListUtils.Pager(pageSize,pageNum,models));
                       listResponseModel.setPageSize(models.size());
                       listResponseModel.setPageNum(pageNum);
                       listResponseModel.setCode(ResultCode.SUCESS);
                   }else{
                       listResponseModel.setData(models);
                       listResponseModel.setPageSize(models.size());
                       listResponseModel.setPageNum(pageNum);
                       listResponseModel.setCode(ResultCode.SUCESS);
                   }
                   return listResponseModel;
               } catch (Exception e) {
                   return ResponseModel.fail("", ResultCode.SYS_ERROR);
               }
           }
       }catch (Exception e){
           return ResponseModel.fail("", ResultCode.SYS_ERROR);
       }

    }

    @ApiOperation(value = "导出今日报表", notes = "")
    @PostMapping("/ExportTDBB")
    public ResponseModel ExportTDBB(@ModelAttribute UserInfoForToken userInfoForToken,@RequestParam String timeStart, @RequestParam String timeEnd,@RequestParam(required = false) String name,@RequestParam @Nullable String deptId){
        try {
            BackUser user=backUserService.getBackUserById(userInfoForToken.getUserId());
            if (user.getRoleName().equals("M")||user.getRoleName().equals("院长")) {
                try {
                    List<People> peopleList=null;
                    if(StringUtil.isEmpty(name)||name.equalsIgnoreCase("")){
                        peopleList = peopleService.findAll();
                    }else{
                        peopleList = peopleService.findAllPeople(name);
                    }
                    ResponseModel<List<ReportFormModel2>> listResponseModel=new ResponseModel<>();
                    List<ReportFormModel2> models = new ArrayList<>();
                    //根据日期查询规则
                    List<Rule> rulelistForTime=ruleService.getRuleForTime(TimeUtils.startTime(timeStart),TimeUtils.endTime(timeEnd));
                    for (People people : peopleList) {
                        Department byid = iDepartmentRepository.findByid(people.getDepartment());
                        for (Rule rule:rulelistForTime) {
                            ReportFormModel2 model2List=staffMapper.findAllByOpenIdAndRuleId(people.getOpenId(),rule.getId().toString());
                            if(model2List==null){
                                ReportFormModel2 rModel = new ReportFormModel2();
                                rModel.setStuas(rule.getStuas());
                                rModel.setStuas2("4");
                                rModel.setTime(rule.getTime());
                                rModel.setDepartment(byid.getDeptname());
                                rModel.setJurisdiction(people.getJurisdiction());
                                rModel.setPeopleName(people.getPeopleName());
                                models.add(rModel);
                            }else {
                                models.add(model2List);
                            }
                        }
                    }
                    return ResponseModel.sucess("",models);
                } catch (Exception e) {
                    return ResponseModel.fail("", ResultCode.SYS_ERROR);
                }
            } else if (user.getRoleName().equals("部长") || user.getRoleName().equals("副院长")){
                try {
                    List<People> peopleList =null;
                    if(StringUtil.isEmpty(name)||name.equalsIgnoreCase("")){
                        peopleList = peopleService.findForD(deptId);
                    }else{
                        peopleList = peopleService.findAllPeople(name,deptId);
                    }
                    if(user.getRoleName().equals("副院长")){
                        if(backRoleService.getRoleDepartment(userInfoForToken.getUserId()).size()==0)
                            peopleList=new ArrayList<>();
                    }
                    ResponseModel<List<ReportFormModel2>> listResponseModel=new ResponseModel<>();
                    List<ReportFormModel2> models = new ArrayList<>();
                    //根据日期查询规则
                    List<Rule> rulelistForTime=ruleService.getRuleForTime(TimeUtils.startTime(timeStart),TimeUtils.endTime(timeEnd));
                    for (People people : peopleList) {
                        Department byid = iDepartmentRepository.findByid(people.getDepartment());
                        for (Rule rule:rulelistForTime) {
                            ReportFormModel2 model2List=staffMapper.findAllByOpenIdAndRuleId(people.getOpenId(),rule.getId().toString());
                            if(model2List==null){
                                ReportFormModel2 rModel = new ReportFormModel2();
                                rModel.setStuas(rule.getStuas());
                                rModel.setStuas2("4");
                                rModel.setTime(rule.getTime());
                                rModel.setDepartment(byid.getDeptname());
                                rModel.setJurisdiction(people.getJurisdiction());
                                rModel.setPeopleName(people.getPeopleName());
                                models.add(rModel);
                            }else {
                                models.add(model2List);
                            }
                        }
                    }
                    return ResponseModel.sucess("",models);
                } catch (Exception e) {
                    return ResponseModel.fail("", ResultCode.SYS_ERROR);
                }
            }else {
                try {
                    People people = peopleService.findPeople(user.getWechat());
                    ResponseModel<List<ReportFormModel2>> listResponseModel=new ResponseModel<>();
                    List<ReportFormModel2> models = new ArrayList<>();
                    //根据日期查询规则
                    List<Rule> rulelistForTime=ruleService.getRuleForTime(TimeUtils.startTime(timeStart),TimeUtils.endTime(timeEnd));
                    Department byid = iDepartmentRepository.findByid(people.getDepartment());
                    for (Rule rule:rulelistForTime) {
                        ReportFormModel2 model2List=staffMapper.findAllByOpenIdAndRuleId(people.getOpenId(),rule.getId().toString());
                        if(model2List==null){
                            ReportFormModel2 rModel = new ReportFormModel2();
                            rModel.setStuas(rule.getStuas());
                            rModel.setStuas2("4");
                            rModel.setTime(rule.getTime());
                            rModel.setDepartment(byid.getDeptname());
                            rModel.setJurisdiction(people.getJurisdiction());
                            rModel.setPeopleName(people.getPeopleName());
                            models.add(rModel);
                        }else {
                            models.add(model2List);
                        }
                    }
                    return ResponseModel.sucess("",models);
                } catch (Exception e) {
                    return ResponseModel.fail("", ResultCode.SYS_ERROR);
                }
            }
        }catch (Exception e){
            return ResponseModel.fail("", ResultCode.SYS_ERROR);
        }

    }

    @ApiOperation(value = "考勤列表", notes = "")
    @PostMapping("/getDailyList")
    public ResponseModel<List<ReportFormModel2>> getAllBB(@ModelAttribute UserInfoForToken userInfoForToken,@RequestParam(required = false) String name,@RequestParam @Nullable String dpid,@RequestParam String timeStart, @RequestParam String timeEnd,@RequestParam int pageSize ,@RequestParam @Nullable int pageNum){
        try {
            BackUser user=backUserService.getBackUserById(userInfoForToken.getUserId());
            if (user.getRoleName().equals("M")||user.getRoleName().equals("院长")) {
                try {
                    ResponseModel<List<ReportFormModel2>> listResponseModel=new ResponseModel<>();
                    List<People> peopleList=null;
                    if(StringUtil.isEmpty(name)||name.equalsIgnoreCase("")){
                        peopleList = peopleService.findAll();
                    }else{
                        peopleList = peopleService.findAllPeople(name);
                    }
                    //ResponseModel<List<ReportFormModel2>> listResponseModel=new ResponseModel<>();
                    List<ReportFormModel2> models = new ArrayList<>();
                    for (People people : peopleList
                            ) {
                        //List<Record> allByOpenIdAndStuas = recordService.findAllByOpenIdAndTimeBetween(people.getOpenId(),TimeUtils.startTime(timeStart),TimeUtils.endTime(timeEnd));
                        List<ReportFormModel2> model2List=staffMapper.findAllByTimeAndOpenId(people.getOpenId(),TimeUtils.startTime(timeStart),TimeUtils.endTime(timeEnd));
                        for (ReportFormModel2 item:model2List){
                            models.add(item);
                        }
                        /*for (Record record:allByOpenIdAndStuas) {
                            ReportFormModel2 rModel = new ReportFormModel2();
                            Rule rule =ruleService.getRuleById(Integer.valueOf(record.getRuleId()));
                            rModel.setStuas(rule.getStuas());
                            rModel.setStuas2(record.getStuas());
                            rModel.setTime(rule.getTime());
                            rModel.setChockinTime(record.getTime());
                            Department byid = iDepartmentRepository.findByid(people.getDepartment());
                            rModel.setDepartment(byid.getDeptname());
                            rModel.setJurisdiction(people.getJurisdiction());
                            rModel.setPeopleName(people.getPeopleName());
                            models.add(rModel);
                        }*/
                    }
                    if (models!=null && models.size()!=0){
                        listResponseModel.setData(ListUtils.Pager(pageSize,pageNum,models));
                        listResponseModel.setPageSize(models.size());
                        listResponseModel.setPageNum(pageNum);
                        listResponseModel.setCode(ResultCode.SUCESS);
                    }
                    return listResponseModel;
                } catch (Exception e) {
                    return ResponseModel.fail("", ResultCode.SYS_ERROR);
                }
            } else if (user.getRoleName().equals("部长") || user.getRoleName().equals("副院长")){
                try {
                    ResponseModel<List<ReportFormModel2>> listResponseModel=new ResponseModel<>();
                    List<People> peopleList =null;
                    if(StringUtil.isEmpty(name)||name.equalsIgnoreCase("")){
                        peopleList = peopleService.findForD(dpid);
                    }else{
                        peopleList = peopleService.findAllPeople(name,dpid);
                    }
                    if(user.getRoleName().equals("副院长")){
                        if(backRoleService.getRoleDepartment(userInfoForToken.getUserId()).size()==0)
                            peopleList=new ArrayList<>();
                    }
                    List<ReportFormModel2> models = new ArrayList<>();
                    for (People people : peopleList
                            ) {
                        List<ReportFormModel2> model2List=staffMapper.findAllByTimeAndOpenId(people.getOpenId(),TimeUtils.startTime(timeStart),TimeUtils.endTime(timeEnd));
                        for (ReportFormModel2 item:model2List){
                            models.add(item);
                        }
                       /* List<Record> allByOpenIdAndStuas = recordService.findAllByOpenIdAndTimeBetween(people.getOpenId(),TimeUtils.startTime(timeStart),TimeUtils.endTime(timeEnd));
                        for (Record record:allByOpenIdAndStuas) {
                            ReportFormModel2 rModel = new ReportFormModel2();
                            Rule rule =ruleService.getRuleById(Integer.valueOf(record.getRuleId()));
                            rModel.setStuas(rule.getStuas());
                            rModel.setStuas2(record.getStuas());
                            rModel.setTime(rule.getTime());
                            rModel.setChockinTime(record.getTime());
                            Department byid = iDepartmentRepository.findByid(people.getDepartment());
                            rModel.setDepartment(byid.getDeptname());
                            rModel.setJurisdiction(people.getJurisdiction());
                            rModel.setPeopleName(people.getPeopleName());
                            models.add(rModel);
                        }*/
                    }
                    if (models!=null && models.size()!=0){
                        listResponseModel.setData(ListUtils.Pager(pageSize,pageNum,models));
                        listResponseModel.setPageSize(models.size());
                        listResponseModel.setPageNum(pageNum);
                        listResponseModel.setCode(ResultCode.SUCESS);
                    }
                    return listResponseModel;
                } catch (Exception e) {
                    return ResponseModel.fail("", ResultCode.SYS_ERROR);
                }
            }else {
                try {
                    People people = peopleService.findPeople(user.getWechat());
                    ResponseModel<List<ReportFormModel2>> listResponseModel=new ResponseModel<>();
                    List<ReportFormModel2> models = new ArrayList<>();
                    /*List<Record> allByOpenIdAndStuas = recordService.findAllByOpenIdAndTimeBetween(people.getOpenId(),TimeUtils.startTime(timeStart),TimeUtils.endTime(timeEnd));
                    for (Record record:allByOpenIdAndStuas) {
                        ReportFormModel2 rModel = new ReportFormModel2();
                        Rule rule = ruleService.getRuleById(Integer.valueOf(record.getRuleId()));
                        rModel.setStuas(rule.getStuas());
                        rModel.setStuas2(record.getStuas());
                        rModel.setTime(rule.getTime());
                        rModel.setChockinTime(record.getTime());
                        Department byid = iDepartmentRepository.findByid(people.getDepartment());
                        rModel.setDepartment(byid.getDeptname());
                        rModel.setJurisdiction(people.getJurisdiction());
                        rModel.setPeopleName(people.getPeopleName());
                        models.add(rModel);
                    }*/
                    List<ReportFormModel2> model2List=staffMapper.findAllByTimeAndOpenId(people.getOpenId(),TimeUtils.startTime(timeStart),TimeUtils.endTime(timeEnd));
                    for (ReportFormModel2 item:model2List){
                        models.add(item);
                    }
                    if (models!=null && models.size()!=0){
                        listResponseModel.setData(ListUtils.Pager(pageSize,pageNum,models));
                        listResponseModel.setPageSize(models.size());
                        listResponseModel.setPageNum(pageNum);
                        listResponseModel.setCode(ResultCode.SUCESS);
                    }
                    return listResponseModel;
                } catch (Exception e) {
                    return ResponseModel.fail("", ResultCode.SYS_ERROR);
                }
            }
        }catch (Exception e){
                return ResponseModel.fail("", ResultCode.SYS_ERROR);
        }
    }

    @ApiOperation(value = "导出考勤列表", notes = "")
    @PostMapping("/importDailyList")
    public ResponseModel importList(@ModelAttribute UserInfoForToken userInfoForToken,@RequestParam(required = false) String name,@RequestParam @Nullable String dpid,@RequestParam String timeStart, @RequestParam String timeEnd)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    {
        try {
            BackUser user=backUserService.getBackUserById(userInfoForToken.getUserId());
            if (user.getRoleName().equals("M")||user.getRoleName().equals("院长")) {
                try {
                    List<People> peopleList=null;
                    if(StringUtil.isEmpty(name)||name.equalsIgnoreCase("")){
                        peopleList = peopleService.findAll();
                    }else{
                        peopleList = peopleService.findAllPeople(name);
                    }
                    List<ReportFormModel2> models = new ArrayList<>();
                    for (People people : peopleList) {
                       /* List<Record> allByOpenIdAndStuas = recordService.findAllByOpenIdAndTimeBetween(people.getOpenId(),TimeUtils.startTime(timeStart),TimeUtils.endTime(timeEnd));
                        for (Record record:allByOpenIdAndStuas) {
                            ReportFormModel2 rModel = new ReportFormModel2();
                            Rule rule =ruleService.getRuleById(Integer.valueOf(record.getRuleId()));
                            rModel.setStuas(rule.getStuas());
                            rModel.setStuas2(record.getStuas());
                            rModel.setTime(rule.getTime());
                            rModel.setChockinTime(record.getTime());
                            Department byid = iDepartmentRepository.findByid(people.getDepartment());
                            rModel.setDepartment(byid.getDeptname());
                            rModel.setJurisdiction(people.getJurisdiction());
                            rModel.setPeopleName(people.getPeopleName());
                            models.add(rModel);
                        }*/
                       long s=TimeUtils.startTime(timeStart);
                       long e=TimeUtils.endTime(timeEnd);
                        List<ReportFormModel2> model2List=staffMapper.findAllByTimeAndOpenId(people.getOpenId(),TimeUtils.startTime(timeStart),TimeUtils.endTime(timeEnd));
                        for (ReportFormModel2 item:model2List){
                            models.add(item);
                        }
                    }
                    /*List<String> title=new ArrayList<>();
                    title.add("序号");
                    title.add("姓名");
                    title.add("部门");
                    title.add("职位");
                    title.add("考勤");
                    title.add("打卡时间");
                    title.add("考勤时间");
                    title.add("状态");
                    List<String> zd=new ArrayList<>();
                    for (int i=0;i<=models.size();i++){
                        zd.add(i+"");
                    }
                    PoiUtils.createExcel(filePath,"考勤记录.xls",title,zd,JSON.parseArray(JSON.toJSONString(models)));*/
                    return ResponseModel.sucess("",models);
                } catch (Exception e) {
                    return ResponseModel.fail("", ResultCode.SYS_ERROR);
                }
            } else if (user.getRoleName().equals("部长") || user.getRoleName().equals("副院长")){
                try {
                    List<People> peopleList =null;
                    if(StringUtil.isEmpty(name)||name.equalsIgnoreCase("")){
                        peopleList = peopleService.findForD(dpid);
                    }else{
                        peopleList = peopleService.findAllPeople(name,dpid);
                    }
                    if(user.getRoleName().equals("副院长")){
                        if(backRoleService.getRoleDepartment(userInfoForToken.getUserId()).size()==0)
                            peopleList=new ArrayList<>();
                    }
                    List<ReportFormModel2> models = new ArrayList<>();
                    for (People people : peopleList
                            ) {
                        /*List<Record> allByOpenIdAndStuas = recordService.findAllByOpenIdAndTimeBetween(people.getOpenId(),TimeUtils.startTime(timeStart),TimeUtils.endTime(timeEnd));
                        for (Record record:allByOpenIdAndStuas) {
                            ReportFormModel2 rModel = new ReportFormModel2();
                            Rule rule =ruleService.getRuleById(Integer.valueOf(record.getRuleId()));
                            rModel.setStuas(rule.getStuas());
                            rModel.setStuas2(record.getStuas());
                            rModel.setTime(rule.getTime());
                            rModel.setChockinTime(record.getTime());
                            Department byid = iDepartmentRepository.findByid(people.getDepartment());
                            rModel.setDepartment(byid.getDeptname());
                            rModel.setJurisdiction(people.getJurisdiction());
                            rModel.setPeopleName(people.getPeopleName());
                            models.add(rModel);
                        }*/
                        List<ReportFormModel2> model2List=staffMapper.findAllByTimeAndOpenId(people.getOpenId(),TimeUtils.startTime(timeStart),TimeUtils.endTime(timeEnd));
                        for (ReportFormModel2 item:model2List){
                            models.add(item);
                        }
                    }
                    return ResponseModel.sucess("",models);
                } catch (Exception e) {
                    return ResponseModel.fail("", ResultCode.SYS_ERROR);
                }
            }else {
                try {
                    People people = peopleService.findPeople(user.getWechat());
                    List<ReportFormModel2> models = new ArrayList<>();
                    /*List<Record> allByOpenIdAndStuas = recordService.findAllByOpenIdAndTimeBetween(people.getOpenId(),TimeUtils.startTime(timeStart),TimeUtils.endTime(timeEnd));
                    for (Record record:allByOpenIdAndStuas) {
                        ReportFormModel2 rModel = new ReportFormModel2();
                        Rule rule = ruleService.getRuleById(Integer.valueOf(record.getRuleId()));
                        rModel.setStuas(rule.getStuas());
                        rModel.setStuas2(record.getStuas());
                        rModel.setTime(rule.getTime());
                        rModel.setChockinTime(record.getTime());
                        Department byid = iDepartmentRepository.findByid(people.getDepartment());
                        rModel.setDepartment(byid.getDeptname());
                        rModel.setJurisdiction(people.getJurisdiction());
                        rModel.setPeopleName(people.getPeopleName());
                        models.add(rModel);
                    }*/
                    List<ReportFormModel2> model2List=staffMapper.findAllByTimeAndOpenId(people.getOpenId(),TimeUtils.startTime(timeStart),TimeUtils.endTime(timeEnd));
                    for (ReportFormModel2 item:model2List){
                        models.add(item);
                    }
                    return ResponseModel.sucess("",models);
                } catch (Exception e) {
                    return ResponseModel.fail("", ResultCode.SYS_ERROR);
                }
            }
        }catch (Exception e){
            return ResponseModel.fail("", ResultCode.SYS_ERROR);
        }
    }

    @ApiOperation(value = "导出统计列表(按月)", notes = "")
    @PostMapping("/importStatisticsByMonth")
    public ResponseModel importStatisticsByMonth(@ModelAttribute UserInfoForToken userInfoForToken,@RequestParam(required = false) String name,@RequestParam @Nullable String dpid,@RequestParam String timeStart, @RequestParam String timeEnd) {
        return ResponseModel.sucess("",recordService.getStatisticsByMonth(userInfoForToken,dpid, timeStart, timeEnd));
    }

    @ApiOperation(value = "导出统计列表(按周)", notes = "")
    @PostMapping("/importStatisticsByWeek")
    public ResponseModel importStatisticsByWeek(@ModelAttribute UserInfoForToken userInfoForToken,@RequestParam(required = false) String name,@RequestParam @Nullable String dpid,@RequestParam String timeStart, @RequestParam String timeEnd) {
        return ResponseModel.sucess("",recordService.getStatisticsByWeek(userInfoForToken,dpid, timeStart, timeEnd));
    }

    @ApiOperation(value = "后台导出统计列表(按月)", notes = "")
    @PostMapping("/BackImportStatisticsByMonth")
    public ResponseModel BackImportStatisticsByMonth(@ModelAttribute UserInfoForToken userInfoForToken,@RequestParam(required = false) String name,@RequestParam @Nullable String dpid,@RequestParam String timeStart, @RequestParam String timeEnd) {
        return ResponseModel.sucess("",recordService.backGetStatisticsByMonth(userInfoForToken,dpid, timeStart, timeEnd));
    }

    @ApiOperation(value = "后台导出统计列表(按周)", notes = "")
    @PostMapping("/BackImportStatisticsByWeek")
    public ResponseModel BackImportStatisticsByWeek(@ModelAttribute UserInfoForToken userInfoForToken,@RequestParam(required = false) String name,@RequestParam @Nullable String dpid,@RequestParam String timeStart, @RequestParam String timeEnd) {
        return ResponseModel.sucess("",recordService.backGetStatisticsByWeek(userInfoForToken,dpid, timeStart, timeEnd));
    }
}
