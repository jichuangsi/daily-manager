package com.jichuangsi.school.timingservice.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jichuangsi.school.timingservice.constant.ResultCode;
import com.jichuangsi.school.timingservice.dao.mapper.StaffMapper;
import com.jichuangsi.school.timingservice.entity.*;
import com.jichuangsi.school.timingservice.model.DailyListModel;
import com.jichuangsi.school.timingservice.model.ResponseModel;
import com.jichuangsi.school.timingservice.model.StatisticsModel;
import com.jichuangsi.school.timingservice.model.UserInfoForToken;
import com.jichuangsi.school.timingservice.repository.IDepartmentRepository;
import com.jichuangsi.school.timingservice.repository.IStaffRepository;
import com.jichuangsi.school.timingservice.repository.PeopleRepostitory;
import com.jichuangsi.school.timingservice.repository.RecordRepostitory;
import com.jichuangsi.school.timingservice.service.BackRoleService;
import com.jichuangsi.school.timingservice.service.BackUserService;
import com.jichuangsi.school.timingservice.service.RecordService;
import com.jichuangsi.school.timingservice.service.RuleService;
import com.jichuangsi.school.timingservice.utils.ExportExcel;
import com.jichuangsi.school.timingservice.utils.PoiUtils;
import com.jichuangsi.school.timingservice.utils.TimeUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service

public class RecordServiceImpl implements RecordService {

    @Resource
    private RecordRepostitory recordRepostitory;
    @Resource
    private IDepartmentRepository departmentRepository;
    @Resource
    private RecordService recordService;
    @Resource
    private RuleService ruleService;
    @Resource
    private IStaffRepository staffRepository;
    @Resource
    private StaffMapper staffMapper;
    @Resource
    private BackUserService backUserService;
    @Resource
    private BackRoleService backRoleService;


    @Override
    public void daKa(String name, String wifiName, String longitudeLatitude, String stuas,String openId,String ruleId) {
        long nowTime =System.currentTimeMillis();
            recordRepostitory.insertRecord(longitudeLatitude,name, stuas,nowTime,wifiName,openId,ruleId);
//        Record record = new Record();
//        record.setLongitudeLatitude(longitudeLatitude);
//        record.setName(name);
//        record.setOpenId(openId);
//        record.setStuas("1");
//
//        recordRepostitory.save(record);
    }

    @Override
    public List<Record> findAllByOpenId(String openId) {

        return recordRepostitory.findAllByOpenIdAndTimeGreaterThanEqual(openId,TimeUtils.todayMorning());
    }

    @Override
    public List<Record> findPxcPeopleOpenid() {

        return recordRepostitory.findAllByTimeGreaterThanEqualOrderByTime(TimeUtils.todayMorning());
    }

    @Override
    public List<Record> findAllByOpenIdAndRuleIdAndStuas(String openId, String ruleId) {
        List<Record> list = recordRepostitory.findFirstByOpenIdAndRuleIdAndStuasOrOpenIdAndRuleIdAndStuasOrderByStuas(openId, ruleId, "0", "2");
        System.out.println(1);
        return list;
    }

    @Override
    public List<Record> findAllByOpenIdAndStuas(String openId) {
        return recordRepostitory.findAllByOpenIdAndStuas(openId,"0");
    }

    @Override
    public List<Record> findAllByOpenIdAndRuleIdAndStuas2(String openId, String ruleId) {
        return recordRepostitory.findFirstByOpenIdAndRuleIdAndStuasOrOpenIdAndRuleIdAndStuasOrderByStuas(openId, ruleId, "0", "3");
    }

    @Override
    public List<Record> findAllByOpenIdAndStuasAndTimeBetween(String openId, long timeStart, long timeEnd) {
        return recordRepostitory.findAllByOpenIdAndStuasAndTimeBetween(openId,"0",timeStart,timeEnd);
    }

    @Override
    public List<Record> findAllByOpenIdAndTimeBetween(String openId, long timeStart, long timeEnd) {
        return recordRepostitory.findAllByOpenIdAndTimeBetween(openId,timeStart,timeEnd);
    }

    @Override
    public List<Record> findAllByOpenIdAndStuasAndRuleIdAndTimeBetween(String openId, long timeStart, long timeEnd,String ruleId) {
        return recordRepostitory.findAllByOpenIdAndStuasAndTimeBetweenAndRuleId(openId,"0",timeStart,timeEnd,ruleId);
    }

    @Override
    public int countByOpendIdInAndStatusAndTime(List<String> openIds, long timeStart, long timeEnd, String status) {
        return recordRepostitory.countByOpenIdInAndStuasAndTimeBetween(openIds,status, timeStart,timeEnd);
    }

    /**
     * 分割日期（按月/周）
     * @param statisticsType
     * @param map
     * @return
     * @throws ParseException
     */
    private List<String> doDateByStatisticsType(String statisticsType,Map<String, Object> map) throws Exception {
        List<String> listWeekOrMonth = new ArrayList<String>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = (String)map.get("startDate");
        String endDate = (String)map.get("endDate");
        Date sDate = dateFormat.parse(startDate);
        Calendar sCalendar = Calendar.getInstance();
        sCalendar.setFirstDayOfWeek(Calendar.MONDAY);
        sCalendar.setTime(sDate);
        Date eDate = dateFormat.parse(endDate);
        Calendar eCalendar = Calendar.getInstance();
        eCalendar.setFirstDayOfWeek(Calendar.MONDAY);
        eCalendar.setTime(eDate);
        boolean bool =true;
        if(statisticsType.equals("week")){
            while(sCalendar.getTime().getTime()<eCalendar.getTime().getTime()){
                if(bool||sCalendar.get(Calendar.DAY_OF_WEEK)==2||sCalendar.get(Calendar.DAY_OF_WEEK)==1){
                    listWeekOrMonth.add(dateFormat.format(sCalendar.getTime()));
                    bool = false;
                }
                sCalendar.add(Calendar.DAY_OF_MONTH, 1);
            }
            listWeekOrMonth.add(dateFormat.format(eCalendar.getTime()));
            if(listWeekOrMonth.size()%2!=0){
                listWeekOrMonth.add(dateFormat.format(eCalendar.getTime()));
            }
        }else{
            while(sCalendar.getTime().getTime()<eCalendar.getTime().getTime()){
                if(bool||sCalendar.get(Calendar.DAY_OF_MONTH)==1||sCalendar.get(Calendar.DAY_OF_MONTH)==sCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)){
                    listWeekOrMonth.add(dateFormat.format(sCalendar.getTime()));
                    bool = false;
                }
                sCalendar.add(Calendar.DAY_OF_MONTH, 1);
            }
            listWeekOrMonth.add(dateFormat.format(eCalendar.getTime()));
            if(listWeekOrMonth.size()%2!=0){
                listWeekOrMonth.add(dateFormat.format(eCalendar.getTime()));
            }
        }

        return listWeekOrMonth;
    }

    @Override
    public DailyListModel getStatisticsByMonth(UserInfoForToken userInfoForToken,String dpid, String timeStart, String timeEnd){
        try {
            BackUser user=backUserService.getBackUserById(userInfoForToken.getUserId());
            Map<String,Object> time=new HashMap<>() ;
            time.put("startDate",timeStart);
            time.put("endDate",timeEnd);
            Map<String,Object> statistics = new HashMap<>();//统计
            //按月统计
            List<String> listMonth=doDateByStatisticsType("",time);
            if (user.getRoleName().equals("M")||user.getRoleName().equals("院长")) {
                List<Department> departments=departmentRepository.findAll();//全部部门
                int j=1;
                for(int i=0;i<listMonth.size();i+=2){//分割后的日期
                    for (Department d:departments) {//部门
                        List<Staff> staff=staffRepository.findAllByDepartment(d);
                        //List<Rule> rulelistForTime=ruleService.getRuleForTime(TimeUtils.startTime(listMonth.get(i)),TimeUtils.endTime(listMonth.get(i+1)));//根据日期查询规则
                        List<Rule> rulelistForTime1=ruleService.getRuleForTime(TimeUtils.startTime(listMonth.get(i)),TimeUtils.endTime(listMonth.get(i+1)),"1");//根据日期查询规则上班
                        StatisticsModel model=new StatisticsModel();//统计模型
                        model.setDeptName(d.getDeptname());
                        model.setShangRule(rulelistForTime1.size());
                        model.setPeopleCount(staff.size());
                        int count1=0;
                        if(rulelistForTime1.size()!=0){
                            List<Integer> ruleids=new ArrayList<>();
                            for (Rule r:rulelistForTime1){
                                ruleids.add(r.getId());
                            }
                            List<Record> monthShangCount=staffMapper.getRuleIdAndOpenId(ruleids,d.getId());//上班打卡人数
                            List<Record> monthShangYichangCount=staffMapper.getRuleIdAndOpenIdAndStatus(ruleids,d.getId());//上班打卡异常人数
                            model.setShangkao(monthShangCount.size());
                            //count1=rulelistForTime1.size()*staff.size()-monthShangCount.size();
                            model.setShangLostKao(rulelistForTime1.size()*staff.size()-monthShangCount.size());
                            model.setYichang(monthShangYichangCount.size());
                        }

                        //下班
                        List<Rule> rulelistForTime2=ruleService.getRuleForTime(TimeUtils.startTime(listMonth.get(i)),TimeUtils.endTime(listMonth.get(i+1)),"2");//根据日期查询规则下班
                        model.setXiaRule(rulelistForTime2.size());
                        int count2=0;
                        if(rulelistForTime2.size()!=0){
                            List<Integer> ruleids2=new ArrayList<>();
                            for (Rule r:rulelistForTime2){
                                ruleids2.add(r.getId());
                            }
                            List<Record> monthXiaCount=staffMapper.getRuleIdAndOpenId(ruleids2,d.getId());//下班打卡人数
                            List<Record> monthXiaYichangCount=staffMapper.getRuleIdAndOpenIdAndStatus(ruleids2,d.getId());//上班打卡异常人数
                            //count2=rulelistForTime2.size()*staff.size()-monthXiaCount.size();
                            model.setXiakao(monthXiaCount.size());
                            model.setXiaLostKao(rulelistForTime2.size()*staff.size()-monthXiaCount.size());
                            model.setYichang(monthXiaYichangCount.size());
                        }
                        statistics.put(d.getDeptname()+"第"+j+"月考勤统计",model);
                    }
                    j++;
                }
                DailyListModel model=new DailyListModel();
                model.setStatistics(statistics);
                return model;
            }else if (user.getRoleName().equals("部长") || user.getRoleName().equals("副院长")){
                Department departments=departmentRepository.findByid(user.getDeptId());//查询部长相关部门
                int j=1;
                if(user.getRoleName().equals("副院长")){
                    if(!StringUtils.isEmpty(dpid)){
                        departments=departmentRepository.findByid(dpid);//查询部长相关部门
                    }
                }
                for(int i=0;i<listMonth.size();i+=2){//分割
                    List<Staff> staff=staffRepository.findAllByDepartment(departments);
                    //List<Rule> rulelistForTime=ruleService.getRuleForTime(TimeUtils.startTime(listMonth.get(i)),TimeUtils.endTime(listMonth.get(i+1)));//根据日期查询规则
                    List<Rule> rulelistForTime1=ruleService.getRuleForTime(TimeUtils.startTime(listMonth.get(i)),TimeUtils.endTime(listMonth.get(i+1)),"1");//根据日期查询规则上班
                    StatisticsModel model=new StatisticsModel();//统计模型
                    model.setDeptName(departments.getDeptname());
                    model.setShangRule(rulelistForTime1.size());
                    model.setPeopleCount(staff.size());
                    int count1=0;
                    if(rulelistForTime1.size()!=0){
                        List<Integer> ruleids=new ArrayList<>();
                        for (Rule r:rulelistForTime1){
                            ruleids.add(r.getId());
                        }
                        List<Record> monthShangCount=staffMapper.getRuleIdAndOpenId(ruleids,departments.getId());//上班打卡人数
                        List<Record> monthShangYichangCount=staffMapper.getRuleIdAndOpenIdAndStatus(ruleids,departments.getId());//上班打卡异常人数
                        model.setShangkao(monthShangCount.size());
                        //count1=rulelistForTime1.size()*staff.size()-monthShangCount.size();
                        model.setShangLostKao(rulelistForTime1.size()*staff.size()-monthShangCount.size());
                        model.setYichang(monthShangYichangCount.size());
                    }

                    //下班
                    List<Rule> rulelistForTime2=ruleService.getRuleForTime(TimeUtils.startTime(listMonth.get(i)),TimeUtils.endTime(listMonth.get(i+1)),"2");//根据日期查询规则下班
                    model.setXiaRule(rulelistForTime2.size());
                    int count2=0;
                    if(rulelistForTime2.size()!=0){
                        List<Integer> ruleids2=new ArrayList<>();
                        for (Rule r:rulelistForTime2){
                            ruleids2.add(r.getId());
                        }
                        List<Record> monthXiaCount=staffMapper.getRuleIdAndOpenId(ruleids2,departments.getId());//下班打卡人数
                        List<Record> monthXiaYichangCount=staffMapper.getRuleIdAndOpenIdAndStatus(ruleids2,departments.getId());//上班打卡异常人数
                        //count2=rulelistForTime2.size()*staff.size()-monthXiaCount.size();
                        model.setXiakao(monthXiaCount.size());
                        model.setXiaLostKao(rulelistForTime2.size()*staff.size()-monthXiaCount.size());
                        model.setYichang(monthXiaYichangCount.size());
                    }
                    statistics.put(departments.getDeptname()+"第"+j+"月考勤统计",model);
                }
            }
            DailyListModel model=new DailyListModel();
            model.setStatistics(statistics);
            return model;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
        /*List<String> opIds=new ArrayList<>();
        for (People people : peopleList) {
            opIds.add(people.getOpenId());
        }
        //按月统计
        List<String> listMonth=doDateByStatisticsType("",time);
        Map<String,Object> statistics = new HashMap<>();//统计
        int j=1;
                    *//*for(int i=0;i<listMonth.size();i+=2){
                        int monthLateCount=0;//迟到
                        int monthEarlyCount=0;//早退
                        monthLateCount+=recordService.countByOpendIdInAndStatusAndTime(opIds,TimeUtils.startTime(listMonth.get(i)),TimeUtils.endTime(timeEnd),"2");
                        monthEarlyCount+=recordService.countByOpendIdInAndStatusAndTime(opIds,TimeUtils.startTime(listMonth.get(i)),TimeUtils.endTime(timeEnd),"3");
                        statistics.put("第"+j+"月迟到统计",monthLateCount);
                        statistics.put("第"+j+"月早退统计",monthEarlyCount);
                        j++;
                    }*//*
        for (People people : peopleList) {
            for(int i=0;i<listMonth.size();i+=2){
                int monthLateCount=0;//迟到
                int monthEarlyCount=0;//早退
                monthLateCount+=recordService.countByOpendIdInAndStatusAndTime(opIds,TimeUtils.startTime(listMonth.get(i)),TimeUtils.endTime(timeEnd),"2");
                monthEarlyCount+=recordService.countByOpendIdInAndStatusAndTime(opIds,TimeUtils.startTime(listMonth.get(i)),TimeUtils.endTime(timeEnd),"3");
                statistics.put("第"+j+"月迟到统计",monthLateCount);
                statistics.put("第"+j+"月早退统计",monthEarlyCount);
                j++;
            }
        }

        // 按周统计
        List<String> listWeek=doDateByStatisticsType("week",time);
        int k=1;
        for(int i=0;i<listWeek.size();i+=2){
            int weekLateCount=0;//迟到
            int weekEarlyCount=0;//早退
            weekLateCount+=recordService.countByOpendIdInAndStatusAndTime(opIds,TimeUtils.startTime(listWeek.get(i)),TimeUtils.endTime(timeEnd),"2");
            weekEarlyCount+=recordService.countByOpendIdInAndStatusAndTime(opIds,TimeUtils.startTime(listWeek.get(i)),TimeUtils.endTime(timeEnd),"3");
            statistics.put("第"+j+"周迟到统计",weekLateCount);
            statistics.put("第"+j+"周早退统计",weekEarlyCount);
            j++;
        }

                   *//*List<String> title=new ArrayList<>();
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
                    PoiUtils.createExcel(filePath,"考勤记录.xls",title,zd,JSON.parseArray(JSON.toJSONString(models)));*//*
        DailyListModel model=new DailyListModel();
        model.setModel2s(models);
        model.setStatistics(statistics);
        return null;*/
    }

    @Override
    public DailyListModel getStatisticsByWeek(UserInfoForToken userInfoForToken, String dpid, String timeStart, String timeEnd) {
        try {
            BackUser user=backUserService.getBackUserById(userInfoForToken.getUserId());
            Map<String,Object> time=new HashMap<>() ;
            time.put("startDate",timeStart);
            time.put("endDate",timeEnd);
            Map<String,Object> statistics = new HashMap<>();//统计
            //按周统计
            List<String> listWeek=doDateByStatisticsType("week",time);
            if (user.getRoleName().equals("M")||user.getRoleName().equals("院长")) {
                List<Department> departments=departmentRepository.findAll();//全部部门
                int j=1;
                for(int i=0;i<listWeek.size();i+=2){//分割后的日期
                    for (Department d:departments) {//部门
                        List<Staff> staff=staffRepository.findAllByDepartment(d);
                        //List<Rule> rulelistForTime=ruleService.getRuleForTime(TimeUtils.startTime(listMonth.get(i)),TimeUtils.endTime(listMonth.get(i+1)));//根据日期查询规则
                        List<Rule> rulelistForTime1=ruleService.getRuleForTime(TimeUtils.startTime(listWeek.get(i)),TimeUtils.endTime(listWeek.get(i+1)),"1");//根据日期查询规则上班
                        StatisticsModel model=new StatisticsModel();//统计模型
                        model.setDeptName(d.getDeptname());
                        model.setShangRule(rulelistForTime1.size());
                        model.setPeopleCount(staff.size());
                        int count1=0;
                        if(rulelistForTime1.size()!=0){
                            List<Integer> ruleids=new ArrayList<>();
                            for (Rule r:rulelistForTime1){
                                ruleids.add(r.getId());
                            }
                            List<Record> monthShangCount=staffMapper.getRuleIdAndOpenId(ruleids,d.getId());//上班打卡人数
                            List<Record> monthShangYichangCount=staffMapper.getRuleIdAndOpenIdAndStatus(ruleids,d.getId());//上班打卡异常人数
                            model.setShangkao(monthShangCount.size());
                            //count1=rulelistForTime1.size()*staff.size()-monthShangCount.size();
                            model.setShangLostKao(rulelistForTime1.size()*staff.size()-monthShangCount.size());
                            model.setYichang(monthShangYichangCount.size());
                        }

                        //下班
                        List<Rule> rulelistForTime2=ruleService.getRuleForTime(TimeUtils.startTime(listWeek.get(i)),TimeUtils.endTime(listWeek.get(i+1)),"2");//根据日期查询规则下班
                        model.setXiaRule(rulelistForTime2.size());
                        int count2=0;
                        if(rulelistForTime2.size()!=0){
                            List<Integer> ruleids2=new ArrayList<>();
                            for (Rule r:rulelistForTime2){
                                ruleids2.add(r.getId());
                            }
                            List<Record> monthXiaCount=staffMapper.getRuleIdAndOpenId(ruleids2,d.getId());//下班打卡人数
                            List<Record> monthXiaYichangCount=staffMapper.getRuleIdAndOpenIdAndStatus(ruleids2,d.getId());//上班打卡异常人数
                            //count2=rulelistForTime2.size()*staff.size()-monthXiaCount.size();
                            model.setXiakao(monthXiaCount.size());
                            model.setXiaLostKao(rulelistForTime2.size()*staff.size()-monthXiaCount.size());
                            model.setYichang(monthXiaYichangCount.size());
                        }
                        statistics.put(d.getDeptname()+"第"+j+"周考勤统计",model);
                    }
                    j++;
                }
                DailyListModel model=new DailyListModel();
                model.setStatistics(statistics);
                return model;
            }else if (user.getRoleName().equals("部长") || user.getRoleName().equals("副院长")){
                Department departments=departmentRepository.findByid(user.getDeptId());//查询部长相关部门
                int j=1;
                if(user.getRoleName().equals("副院长")){
                    if(!StringUtils.isEmpty(dpid)){
                        departments=departmentRepository.findByid(dpid);//查询部长相关部门
                    }
                }
                for(int i=0;i<listWeek.size();i+=2){//分割
                    List<Staff> staff=staffRepository.findAllByDepartment(departments);
                    //List<Rule> rulelistForTime=ruleService.getRuleForTime(TimeUtils.startTime(listMonth.get(i)),TimeUtils.endTime(listMonth.get(i+1)));//根据日期查询规则
                    List<Rule> rulelistForTime1=ruleService.getRuleForTime(TimeUtils.startTime(listWeek.get(i)),TimeUtils.endTime(listWeek.get(i+1)),"1");//根据日期查询规则上班
                    StatisticsModel model=new StatisticsModel();//统计模型
                    model.setDeptName(departments.getDeptname());
                    model.setShangRule(rulelistForTime1.size());
                    model.setPeopleCount(staff.size());
                    int count1=0;
                    if(rulelistForTime1.size()!=0){
                        List<Integer> ruleids=new ArrayList<>();
                        for (Rule r:rulelistForTime1){
                            ruleids.add(r.getId());
                        }
                        List<Record> monthShangCount=staffMapper.getRuleIdAndOpenId(ruleids,departments.getId());//上班打卡人数
                        List<Record> monthShangYichangCount=staffMapper.getRuleIdAndOpenIdAndStatus(ruleids,departments.getId());//上班打卡异常人数
                        model.setShangkao(monthShangCount.size());
                        //count1=rulelistForTime1.size()*staff.size()-monthShangCount.size();
                        model.setShangLostKao(rulelistForTime1.size()*staff.size()-monthShangCount.size());
                        model.setYichang(monthShangYichangCount.size());
                    }

                    //下班
                    List<Rule> rulelistForTime2=ruleService.getRuleForTime(TimeUtils.startTime(listWeek.get(i)),TimeUtils.endTime(listWeek.get(i+1)),"2");//根据日期查询规则下班
                    model.setXiaRule(rulelistForTime2.size());
                    int count2=0;
                    if(rulelistForTime2.size()!=0){
                        List<Integer> ruleids2=new ArrayList<>();
                        for (Rule r:rulelistForTime2){
                            ruleids2.add(r.getId());
                        }
                        List<Record> monthXiaCount=staffMapper.getRuleIdAndOpenId(ruleids2,departments.getId());//下班打卡人数
                        List<Record> monthXiaYichangCount=staffMapper.getRuleIdAndOpenIdAndStatus(ruleids2,departments.getId());//上班打卡异常人数
                        //count2=rulelistForTime2.size()*staff.size()-monthXiaCount.size();
                        model.setXiakao(monthXiaCount.size());
                        model.setXiaLostKao(rulelistForTime2.size()*staff.size()-monthXiaCount.size());
                        model.setYichang(monthXiaYichangCount.size());
                    }
                    statistics.put(departments.getDeptname()+"第"+j+"周考勤统计",model);
                }
            }
            DailyListModel model=new DailyListModel();
            model.setStatistics(statistics);
            return model;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     *根据时间范围获得季度集
     * @return
     */
    private static List<String> getRangeSet_Q(String beginDate,String endDate){
        /*      Date1.after(Date2),当Date1大于Date2时，返回TRUE，当小于等于时，返回false；
          Date1.before(Date2)，当Date1小于Date2时，返回TRUE，当大于等于时，返回false；
          如果业务数据存在相等的时候，而且相等时也需要做相应的业务判断或处理时，你需要使用：！Date1.after(Date2);*/
        List<String> rangeSet =null;
        SimpleDateFormat sdf = null;
        Date begin_date = null;
        Date end_date = null;
        String[] numStr =null;
        String Q=null;
        rangeSet = new java.util.ArrayList<String>();
        sdf = new SimpleDateFormat("yyyy-MM");
        try {
            begin_date = sdf.parse(beginDate);//定义起始日期
            end_date = sdf.parse(endDate);//定义结束日期
        } catch (ParseException e) {
            System.out.println("时间转化异常，请检查你的时间格式是否为yyyy-MM或yyyy-MM-dd");
        }
        Calendar dd = Calendar.getInstance();//定义日期实例
        dd.setTime(begin_date);//设置日期起始时间
        while(!dd.getTime().after(end_date)){//判断是否到结束日期
            numStr=  sdf.format(dd.getTime()).split("-",0);
            Q = getQuarter(Integer.valueOf(numStr[1]))+"";
            System.out.println(numStr[0].toString()+"年"+numStr[1].toString()+"月"+"为"+numStr[0].toString()+"年第"+Q+"季");
            rangeSet.add(Q);
            dd.add(Calendar.MONTH, 1);//进行当前日期月份加1
        }
        return rangeSet;
    }

    /**
     * 根据月获得季度
     * @param month  月
     * @return  季度
     */
    private static int getQuarter(int month) {
        if(month == 1 || month == 2 || month == 3){
            return 1;
        }else if(month == 4 || month == 5 || month == 6){
            return  2;
        }else if(month == 7 || month == 8 || month == 9){
            return 3;
        }else{
            return 4;
        }
    }


    /**
     * 后台按月导出考勤信息
     * @param userInfoForToken
     * @param dpid
     * @param timeStart
     * @param timeEnd
     * @return
     */
    public DailyListModel backGetStatisticsByMonth(UserInfoForToken userInfoForToken,String dpid, String timeStart, String timeEnd){
        try {
            BackUser user=backUserService.getBackUserById("40282b816cacb930016cacb939bd0000");
            Map<String,Object> time=new HashMap<>() ;
            time.put("startDate",timeStart);
            time.put("endDate",timeEnd);
            Map<String,Object> statistics = new HashMap<>();//统计
            List<StatisticsModel> sm=new ArrayList<>();
            //按月统计
            List<String> listMonth=doDateByStatisticsType("",time);
            if (user.getRoleName().equals("M")||user.getRoleName().equals("院长")) {
                List<Department> departments=departmentRepository.findAll();//全部部门
                int j=1;
                for(int i=0;i<listMonth.size();i+=2){//分割后的日期
                    for (Department d:departments) {//部门
                        List<Staff> staff=staffRepository.findAllByDepartment(d);
                        //List<Rule> rulelistForTime=ruleService.getRuleForTime(TimeUtils.startTime(listMonth.get(i)),TimeUtils.endTime(listMonth.get(i+1)));//根据日期查询规则
                        List<Rule> rulelistForTime1=ruleService.getRuleForTime(TimeUtils.startTime(listMonth.get(i)),TimeUtils.endTime(listMonth.get(i+1)),"1");//根据日期查询规则上班
                        StatisticsModel model=new StatisticsModel();//统计模型
                        model.setDeptName(d.getDeptname());
                        model.setShangRule(rulelistForTime1.size());
                        model.setPeopleCount(staff.size());
                        int count1=0;
                        if(rulelistForTime1.size()!=0){
                            List<Integer> ruleids=new ArrayList<>();
                            for (Rule r:rulelistForTime1){
                                ruleids.add(r.getId());
                            }
                            List<Record> monthShangCount=staffMapper.getRuleIdAndOpenId(ruleids,d.getId());//上班打卡人数
                            List<Record> monthShangYichangCount=staffMapper.getRuleIdAndOpenIdAndStatus(ruleids,d.getId());//上班打卡异常人数
                            model.setShangkao(monthShangCount.size());
                            //count1=rulelistForTime1.size()*staff.size()-monthShangCount.size();
                            model.setShangLostKao(rulelistForTime1.size()*staff.size()-monthShangCount.size());
                            model.setYichang(monthShangYichangCount.size());
                        }

                        //下班
                        List<Rule> rulelistForTime2=ruleService.getRuleForTime(TimeUtils.startTime(listMonth.get(i)),TimeUtils.endTime(listMonth.get(i+1)),"2");//根据日期查询规则下班
                        model.setXiaRule(rulelistForTime2.size());
                        int count2=0;
                        if(rulelistForTime2.size()!=0){
                            List<Integer> ruleids2=new ArrayList<>();
                            for (Rule r:rulelistForTime2){
                                ruleids2.add(r.getId());
                            }
                            List<Record> monthXiaCount=staffMapper.getRuleIdAndOpenId(ruleids2,d.getId());//下班打卡人数
                            List<Record> monthXiaYichangCount=staffMapper.getRuleIdAndOpenIdAndStatus(ruleids2,d.getId());//上班打卡异常人数
                            //count2=rulelistForTime2.size()*staff.size()-monthXiaCount.size();
                            model.setXiakao(monthXiaCount.size());
                            model.setXiaLostKao(rulelistForTime2.size()*staff.size()-monthXiaCount.size());
                            model.setYichang(monthXiaYichangCount.size());
                        }
                        model.setHeader(d.getDeptname()+"第"+j+"月考勤统计");
                        sm.add(model);
                        statistics.put(d.getDeptname()+"第"+j+"月考勤统计",model);
                    }
                    j++;
                }
               /*List<String> rowsName = new ArrayList<>();
                Collections.addAll(rowsName,"序号", "部门", "部门总人数", "上班规则", "下班规则",
                        "上班考勤人数（包含正常打卡，迟到，早退，考勤异常）", "下班考勤人数（包含正常打卡，迟到，早退，考勤异常）",
                        "上班缺勤人数（统计当天没有打卡的人数）", "下班缺勤人数（统计当天没有打卡的人数）" ,"考勤异常人数");
                List<String> title=new ArrayList<>();
                title.add("考勤统计");
                List<String> zd=new ArrayList<>();
                for (int i=0;i<sm.size();i++){
                    zd.add(i+"");
                }
                PoiUtils.createExcel("D:/downLoad1/","考勤统计（按月）.xls",rowsName,zd,JSONArray.parseArray(JSONObject.toJSONString(sm)));*/
                // 定义表的标题
                String title = "员工考勤报表一览";
                //定义表的列名
                String[] rowsName = new String[] { "名称", "部门", "部门总人数", "上班规则", "下班规则",
                        "上班考勤次数", "下班考勤次数", "上班缺勤次数", "下班缺勤次数" ,"考勤异常次数"};
                //定义表的内容
                List<Object[]> dataList = new ArrayList<Object[]>();
                Object[] objs = null;
                for (int i = 0; i < sm.size(); i++) {
                    StatisticsModel s = sm.get(i);
                    objs = new Object[rowsName.length];
                    objs[0] = s.getHeader();
                    objs[1] = s.getDeptName();
                    objs[2] = s.getPeopleCount();
                    objs[3] = s.getShangRule();
                    objs[4] = s.getXiaRule();
                    /*SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String date = df.format(per.getJobtime());*/
                    objs[5] = s.getShangkao();
                    objs[6] = s.getXiakao();
                    objs[7] = s.getShangLostKao();
                    objs[8] = s.getXiaLostKao();
                    objs[9] = s.getYichang();
                    dataList.add(objs);
                }
                // 创建ExportExcel对象
                ExportExcel ex = new ExportExcel(title, rowsName, dataList);

                ex.export();


                DailyListModel model=new DailyListModel();
                model.setStatistics(statistics);
                return model;
            }else if (user.getRoleName().equals("部长") || user.getRoleName().equals("副院长")){
                Department departments=departmentRepository.findByid(user.getDeptId());//查询部长相关部门
                int j=1;
                if(user.getRoleName().equals("副院长")){
                    if(!StringUtils.isEmpty(dpid)){
                        departments=departmentRepository.findByid(dpid);//查询部长相关部门
                    }
                }
                for(int i=0;i<listMonth.size();i+=2){//分割
                    List<Staff> staff=staffRepository.findAllByDepartment(departments);
                    //List<Rule> rulelistForTime=ruleService.getRuleForTime(TimeUtils.startTime(listMonth.get(i)),TimeUtils.endTime(listMonth.get(i+1)));//根据日期查询规则
                    List<Rule> rulelistForTime1=ruleService.getRuleForTime(TimeUtils.startTime(listMonth.get(i)),TimeUtils.endTime(listMonth.get(i+1)),"1");//根据日期查询规则上班
                    StatisticsModel model=new StatisticsModel();//统计模型
                    model.setDeptName(departments.getDeptname());
                    model.setShangRule(rulelistForTime1.size());
                    model.setPeopleCount(staff.size());
                    int count1=0;
                    if(rulelistForTime1.size()!=0){
                        List<Integer> ruleids=new ArrayList<>();
                        for (Rule r:rulelistForTime1){
                            ruleids.add(r.getId());
                        }
                        List<Record> monthShangCount=staffMapper.getRuleIdAndOpenId(ruleids,departments.getId());//上班打卡人数
                        List<Record> monthShangYichangCount=staffMapper.getRuleIdAndOpenIdAndStatus(ruleids,departments.getId());//上班打卡异常人数
                        model.setShangkao(monthShangCount.size());
                        //count1=rulelistForTime1.size()*staff.size()-monthShangCount.size();
                        model.setShangLostKao(rulelistForTime1.size()*staff.size()-monthShangCount.size());
                        model.setYichang(monthShangYichangCount.size());
                    }

                    //下班
                    List<Rule> rulelistForTime2=ruleService.getRuleForTime(TimeUtils.startTime(listMonth.get(i)),TimeUtils.endTime(listMonth.get(i+1)),"2");//根据日期查询规则下班
                    model.setXiaRule(rulelistForTime2.size());
                    int count2=0;
                    if(rulelistForTime2.size()!=0){
                        List<Integer> ruleids2=new ArrayList<>();
                        for (Rule r:rulelistForTime2){
                            ruleids2.add(r.getId());
                        }
                        List<Record> monthXiaCount=staffMapper.getRuleIdAndOpenId(ruleids2,departments.getId());//下班打卡人数
                        List<Record> monthXiaYichangCount=staffMapper.getRuleIdAndOpenIdAndStatus(ruleids2,departments.getId());//上班打卡异常人数
                        //count2=rulelistForTime2.size()*staff.size()-monthXiaCount.size();
                        model.setXiakao(monthXiaCount.size());
                        model.setXiaLostKao(rulelistForTime2.size()*staff.size()-monthXiaCount.size());
                        model.setYichang(monthXiaYichangCount.size());
                    }
                    statistics.put(departments.getDeptname()+"第"+j+"月考勤统计",model);
                    model.setHeader(departments.getDeptname()+"第"+j+"月考勤统计");
                    sm.add(model);
                }
            }
            // 定义表的标题
            String title = "员工考勤报表一览";
            //定义表的列名
            String[] rowsName = new String[] { "名称", "部门", "部门总人数", "上班规则", "下班规则",
                    "上班考勤次数", "下班考勤次数", "上班缺勤次数", "下班缺勤次数" ,"考勤异常次数"};
            //定义表的内容
            List<Object[]> dataList = new ArrayList<Object[]>();
            Object[] objs = null;
            for (int i = 0; i < sm.size(); i++) {
                StatisticsModel s = sm.get(i);
                objs = new Object[rowsName.length];
                objs[0] = s.getHeader();
                objs[1] = s.getDeptName();
                objs[2] = s.getPeopleCount();
                objs[3] = s.getShangRule();
                objs[4] = s.getXiaRule();
                    /*SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String date = df.format(per.getJobtime());*/
                objs[5] = s.getShangkao();
                objs[6] = s.getXiakao();
                objs[7] = s.getShangLostKao();
                objs[8] = s.getXiaLostKao();
                objs[9] = s.getYichang();
                dataList.add(objs);
            }
            // 创建ExportExcel对象
            ExportExcel ex = new ExportExcel(title, rowsName, dataList);

            ex.export();
            DailyListModel model=new DailyListModel();
            model.setStatistics(statistics);
            return model;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public DailyListModel backGetStatisticsByWeek(UserInfoForToken userInfoForToken, String dpid, String timeStart, String timeEnd) {
        try {
            BackUser user=backUserService.getBackUserById("40282b816cacb930016cacb939bd0000");
            Map<String,Object> time=new HashMap<>() ;
            time.put("startDate",timeStart);
            time.put("endDate",timeEnd);
            Map<String,Object> statistics = new HashMap<>();//统计
            List<StatisticsModel> sm=new ArrayList<>();
            //按周统计
            List<String> listWeek=doDateByStatisticsType("week",time);
            if (user.getRoleName().equals("M")||user.getRoleName().equals("院长")) {
                List<Department> departments=departmentRepository.findAll();//全部部门
                int j=1;
                for(int i=0;i<listWeek.size();i+=2){//分割后的日期
                    for (Department d:departments) {//部门
                        List<Staff> staff=staffRepository.findAllByDepartment(d);
                        //List<Rule> rulelistForTime=ruleService.getRuleForTime(TimeUtils.startTime(listMonth.get(i)),TimeUtils.endTime(listMonth.get(i+1)));//根据日期查询规则
                        List<Rule> rulelistForTime1=ruleService.getRuleForTime(TimeUtils.startTime(listWeek.get(i)),TimeUtils.endTime(listWeek.get(i+1)),"1");//根据日期查询规则上班
                        StatisticsModel model=new StatisticsModel();//统计模型
                        model.setDeptName(d.getDeptname());
                        model.setShangRule(rulelistForTime1.size());
                        model.setPeopleCount(staff.size());
                        int count1=0;
                        if(rulelistForTime1.size()!=0){
                            List<Integer> ruleids=new ArrayList<>();
                            for (Rule r:rulelistForTime1){
                                ruleids.add(r.getId());
                            }
                            List<Record> monthShangCount=staffMapper.getRuleIdAndOpenId(ruleids,d.getId());//上班打卡人数
                            List<Record> monthShangYichangCount=staffMapper.getRuleIdAndOpenIdAndStatus(ruleids,d.getId());//上班打卡异常人数
                            model.setShangkao(monthShangCount.size());
                            //count1=rulelistForTime1.size()*staff.size()-monthShangCount.size();
                            model.setShangLostKao(rulelistForTime1.size()*staff.size()-monthShangCount.size());
                            model.setYichang(monthShangYichangCount.size());
                        }

                        //下班
                        List<Rule> rulelistForTime2=ruleService.getRuleForTime(TimeUtils.startTime(listWeek.get(i)),TimeUtils.endTime(listWeek.get(i+1)),"2");//根据日期查询规则下班
                        model.setXiaRule(rulelistForTime2.size());
                        int count2=0;
                        if(rulelistForTime2.size()!=0){
                            List<Integer> ruleids2=new ArrayList<>();
                            for (Rule r:rulelistForTime2){
                                ruleids2.add(r.getId());
                            }
                            List<Record> monthXiaCount=staffMapper.getRuleIdAndOpenId(ruleids2,d.getId());//下班打卡人数
                            List<Record> monthXiaYichangCount=staffMapper.getRuleIdAndOpenIdAndStatus(ruleids2,d.getId());//上班打卡异常人数
                            //count2=rulelistForTime2.size()*staff.size()-monthXiaCount.size();
                            model.setXiakao(monthXiaCount.size());
                            model.setXiaLostKao(rulelistForTime2.size()*staff.size()-monthXiaCount.size());
                            model.setYichang(monthXiaYichangCount.size());
                        }
                        statistics.put(d.getDeptname()+"第"+j+"周考勤统计",model);
                        model.setHeader(d.getDeptname()+"第"+j+"周考勤统计");
                        sm.add(model);
                    }
                    j++;
                }
                // 定义表的标题
                String title = "员工考勤报表一览";
                //定义表的列名
                String[] rowsName = new String[] { "名称", "部门", "部门总人数", "上班规则", "下班规则",
                        "上班考勤次数", "下班考勤次数", "上班缺勤次数", "下班缺勤次数" ,"考勤异常次数"};
                //定义表的内容
                List<Object[]> dataList = new ArrayList<Object[]>();
                Object[] objs = null;
                for (int i = 0; i < sm.size(); i++) {
                    StatisticsModel s = sm.get(i);
                    objs = new Object[rowsName.length];
                    objs[0] = s.getHeader();
                    objs[1] = s.getDeptName();
                    objs[2] = s.getPeopleCount();
                    objs[3] = s.getShangRule();
                    objs[4] = s.getXiaRule();
                    /*SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String date = df.format(per.getJobtime());*/
                    objs[5] = s.getShangkao();
                    objs[6] = s.getXiakao();
                    objs[7] = s.getShangLostKao();
                    objs[8] = s.getXiaLostKao();
                    objs[9] = s.getYichang();
                    dataList.add(objs);
                }
                // 创建ExportExcel对象
                ExportExcel ex = new ExportExcel(title, rowsName, dataList);

                ex.export();

                DailyListModel model=new DailyListModel();
                model.setStatistics(statistics);
                return model;
            }else if (user.getRoleName().equals("部长") || user.getRoleName().equals("副院长")){
                Department departments=departmentRepository.findByid(user.getDeptId());//查询部长相关部门
                int j=1;
                if(user.getRoleName().equals("副院长")){
                    if(!StringUtils.isEmpty(dpid)){
                        departments=departmentRepository.findByid(dpid);//查询部长相关部门
                    }
                }
                for(int i=0;i<listWeek.size();i+=2){//分割
                    List<Staff> staff=staffRepository.findAllByDepartment(departments);
                    //List<Rule> rulelistForTime=ruleService.getRuleForTime(TimeUtils.startTime(listMonth.get(i)),TimeUtils.endTime(listMonth.get(i+1)));//根据日期查询规则
                    List<Rule> rulelistForTime1=ruleService.getRuleForTime(TimeUtils.startTime(listWeek.get(i)),TimeUtils.endTime(listWeek.get(i+1)),"1");//根据日期查询规则上班
                    StatisticsModel model=new StatisticsModel();//统计模型
                    model.setDeptName(departments.getDeptname());
                    model.setShangRule(rulelistForTime1.size());
                    model.setPeopleCount(staff.size());
                    int count1=0;
                    if(rulelistForTime1.size()!=0){
                        List<Integer> ruleids=new ArrayList<>();
                        for (Rule r:rulelistForTime1){
                            ruleids.add(r.getId());
                        }
                        List<Record> monthShangCount=staffMapper.getRuleIdAndOpenId(ruleids,departments.getId());//上班打卡人数
                        List<Record> monthShangYichangCount=staffMapper.getRuleIdAndOpenIdAndStatus(ruleids,departments.getId());//上班打卡异常人数
                        model.setShangkao(monthShangCount.size());
                        //count1=rulelistForTime1.size()*staff.size()-monthShangCount.size();
                        model.setShangLostKao(rulelistForTime1.size()*staff.size()-monthShangCount.size());
                        model.setYichang(monthShangYichangCount.size());
                    }

                    //下班
                    List<Rule> rulelistForTime2=ruleService.getRuleForTime(TimeUtils.startTime(listWeek.get(i)),TimeUtils.endTime(listWeek.get(i+1)),"2");//根据日期查询规则下班
                    model.setXiaRule(rulelistForTime2.size());
                    int count2=0;
                    if(rulelistForTime2.size()!=0){
                        List<Integer> ruleids2=new ArrayList<>();
                        for (Rule r:rulelistForTime2){
                            ruleids2.add(r.getId());
                        }
                        List<Record> monthXiaCount=staffMapper.getRuleIdAndOpenId(ruleids2,departments.getId());//下班打卡人数
                        List<Record> monthXiaYichangCount=staffMapper.getRuleIdAndOpenIdAndStatus(ruleids2,departments.getId());//上班打卡异常人数
                        //count2=rulelistForTime2.size()*staff.size()-monthXiaCount.size();
                        model.setXiakao(monthXiaCount.size());
                        model.setXiaLostKao(rulelistForTime2.size()*staff.size()-monthXiaCount.size());
                        model.setYichang(monthXiaYichangCount.size());
                    }
                    statistics.put(departments.getDeptname()+"第"+j+"周考勤统计",model);
                    model.setHeader(departments.getDeptname()+"第"+j+"周考勤统计");
                    sm.add(model);
                }
            }
            // 定义表的标题
            String title = "员工考勤报表一览";
            //定义表的列名
            String[] rowsName = new String[] { "名称", "部门", "部门总人数", "上班规则", "下班规则",
                    "上班考勤次数", "下班考勤次数", "上班缺勤次数", "下班缺勤次数" ,"考勤异常次数"};
            //定义表的内容
            List<Object[]> dataList = new ArrayList<Object[]>();
            Object[] objs = null;
            for (int i = 0; i < sm.size(); i++) {
                StatisticsModel s = sm.get(i);
                objs = new Object[rowsName.length];
                objs[0] = s.getHeader();
                objs[1] = s.getDeptName();
                objs[2] = s.getPeopleCount();
                objs[3] = s.getShangRule();
                objs[4] = s.getXiaRule();
                    /*SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String date = df.format(per.getJobtime());*/
                objs[5] = s.getShangkao();
                objs[6] = s.getXiakao();
                objs[7] = s.getShangLostKao();
                objs[8] = s.getXiaLostKao();
                objs[9] = s.getYichang();
                dataList.add(objs);
            }
            // 创建ExportExcel对象
            ExportExcel ex = new ExportExcel(title, rowsName, dataList);

            ex.export();

            DailyListModel model=new DailyListModel();
            model.setStatistics(statistics);
            return model;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
