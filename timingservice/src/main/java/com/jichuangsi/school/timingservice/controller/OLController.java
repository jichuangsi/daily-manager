package com.jichuangsi.school.timingservice.controller;

import com.jichuangsi.school.timingservice.constant.ResultCode;
import com.jichuangsi.school.timingservice.entity.Overtimeleave;
import com.jichuangsi.school.timingservice.entity.People;
import com.jichuangsi.school.timingservice.model.ResponseModel;
import com.jichuangsi.school.timingservice.service.OLService;
import com.jichuangsi.school.timingservice.service.PeopleService;
import com.jichuangsi.school.timingservice.utils.ListUtils;
import com.sun.istack.internal.Nullable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/ol")
@CrossOrigin
@Api("加班请假相关的api")
public class OLController {
    @Resource
    private OLService olService;
    @Resource
    private PeopleService peopleService;

    @ApiOperation(value = "加班请假申请", notes = "")
    @PostMapping("/ol")
    public ResponseModel<String> AOL(@RequestParam String openId ,@RequestParam String stuas,@RequestParam String msg,@RequestParam long start,@RequestParam long end){
        olService.AOL(openId ,stuas,msg,start,end);
        ResponseModel<String> stringResponseModel = new ResponseModel<>();
        stringResponseModel.setMsg("ok");
        stringResponseModel.setCode(ResultCode.SUCESS);
        return stringResponseModel;
    }

    @ApiOperation(value = "根据openid查加班请假列表", notes = "")
    @PostMapping("/getolrecord")
    public ResponseModel<List<Overtimeleave>> getOLList(@RequestParam String openId){
        ResponseModel<List<Overtimeleave>> listResponseModel = new ResponseModel<>();
        List<Overtimeleave> olList = olService.getOLList(openId);
        listResponseModel.setData(olList);
        listResponseModel.setCode(ResultCode.SUCESS);
        return listResponseModel ;
    }

    @ApiOperation(value = "未审核加班请假列表", notes = "")
    @PostMapping("/getolrecord1")
    public ResponseModel<List<Overtimeleave>> getOLList1(){
        ResponseModel<List<Overtimeleave>> listResponseModel = new ResponseModel<>();
        List<Overtimeleave> olList = olService.getOLList1();
        listResponseModel.setData(olList);
        listResponseModel.setCode(ResultCode.SUCESS);
        return listResponseModel ;
    }

    @ApiOperation(value = "已审核查加班请假列表", notes = "")
    @PostMapping("/getolrecord2")
    public ResponseModel<List<Overtimeleave>> getOLList2(){
        ResponseModel<List<Overtimeleave>> listResponseModel = new ResponseModel<>();
        List<Overtimeleave> olList = olService.getOLList2();
        listResponseModel.setData(olList);
        listResponseModel.setCode(ResultCode.SUCESS);
        return listResponseModel ;
    }
    @ApiOperation(value = "根据名字未审核加班请假列表", notes = "")
    @PostMapping("/getolrecord1name")
    public ResponseModel<List<Overtimeleave>> getOLList1name(@RequestParam @Nullable String name, @RequestParam int pageSize, @RequestParam int pageNum){
        List<People> allPeople = peopleService.findAllPeople(name);
        List<Overtimeleave> overtimeleaves = new ArrayList<>();
        for (People people:allPeople
                ) {
            List<Overtimeleave> olForOpenId2 = olService.getOLForOpenId1(people.getOpenId());
            for (Overtimeleave overtimeleave:olForOpenId2
                    ) {
                overtimeleaves.add(overtimeleave);
            }
        }
        ResponseModel<List<Overtimeleave>> listResponseModel = new ResponseModel<>();

        listResponseModel.setData(ListUtils.Pager(pageSize,pageNum,overtimeleaves));
        listResponseModel.setCode(ResultCode.SUCESS);
        return listResponseModel ;
    }

    @ApiOperation(value = "根据名字已审核查加班请假列表", notes = "")
    @PostMapping("/getolrecord2name")
    public ResponseModel<List<Overtimeleave>> getOLList2name(@RequestParam @Nullable String name,@RequestParam int pageSize,@RequestParam int pageNum){
        List<People> allPeople = peopleService.findAllPeople(name);
        List<Overtimeleave> overtimeleaves = new ArrayList<>();
        for (People people:allPeople
                ) {
            List<Overtimeleave> olForOpenId2 = olService.getOLForOpenId2(people.getOpenId());
            for (Overtimeleave overtimeleave:olForOpenId2
                 ) {
                overtimeleaves.add(overtimeleave);
            }
        }
        ResponseModel<List<Overtimeleave>> listResponseModel = new ResponseModel<>();

        listResponseModel.setData(ListUtils.Pager(pageSize,pageNum,overtimeleaves));
        listResponseModel.setCode(ResultCode.SUCESS);

        return listResponseModel ;
    }
}
