package com.jichuangsi.school.timingservice.controller;

import com.jichuangsi.school.timingservice.constant.ResultCode;
import com.jichuangsi.school.timingservice.entity.BackUser;
import com.jichuangsi.school.timingservice.entity.Department;
import com.jichuangsi.school.timingservice.entity.Overtimeleave;
import com.jichuangsi.school.timingservice.entity.People;
import com.jichuangsi.school.timingservice.model.OvertimeleaveModel;
import com.jichuangsi.school.timingservice.model.ResponseModel;
import com.jichuangsi.school.timingservice.model.UserInfoForToken;
import com.jichuangsi.school.timingservice.repository.IDepartmentRepository;
import com.jichuangsi.school.timingservice.repository.PeopleRepostitory;
import com.jichuangsi.school.timingservice.service.BackUserService;
import com.jichuangsi.school.timingservice.service.OLService;
import com.jichuangsi.school.timingservice.service.PeopleService;
import com.jichuangsi.school.timingservice.utils.ListUtils;
//import com.sun.istack.internal.Nullable;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.springframework.lang.Nullable;
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
    private IDepartmentRepository iDepartmentRepository;
    @Resource
    private OLService olService;
    @Resource
    private PeopleService peopleService;
    @Resource
    private BackUserService backUserService;
    @Resource
    private PeopleRepostitory peopleRepostitory;

    @ApiOperation(value = "加班请假申请", notes = "")
    @PostMapping("/ol")
    public ResponseModel<String> AOL(@RequestParam String openId, @RequestParam String stuas, @RequestParam String msg, @RequestParam long start, @RequestParam long end) {
        olService.AOL(openId, stuas, msg, start, end);
        ResponseModel<String> stringResponseModel = new ResponseModel<>();
        stringResponseModel.setMsg("ok");
        stringResponseModel.setCode(ResultCode.SUCESS);
        return stringResponseModel;
    }

    @ApiOperation(value = "加班请假审核", notes = "")
    @PostMapping("/olsh")
    public ResponseModel<String> AOLSH(@RequestBody Overtimeleave overtimeleave) {
        olService.AOLSH(overtimeleave);
        ResponseModel<String> stringResponseModel = new ResponseModel<>();
        stringResponseModel.setMsg("ok");
        stringResponseModel.setCode(ResultCode.SUCESS);
        return stringResponseModel;
    }
    @ApiOperation(value = "根据openid查加班请假列表", notes = "")
    @PostMapping("/getolrecord")
    public ResponseModel<List<Overtimeleave>> getOLList(@RequestParam String openId) {
        ResponseModel<List<Overtimeleave>> listResponseModel = new ResponseModel<>();
        List<Overtimeleave> olList = olService.getOLList(openId);
        listResponseModel.setData(olList);
        listResponseModel.setCode(ResultCode.SUCESS);
        return listResponseModel;
    }

    @ApiOperation(value = "加班请假列表", notes = "")
    @PostMapping("/getolrecord1")
    public ResponseModel<List<OvertimeleaveModel>> getOLList1(@ModelAttribute UserInfoForToken userInfoForToken, @RequestParam @Nullable String sttt, @RequestParam @Nullable String name, @RequestParam int pageSize, @RequestParam int pageNum) {
        List<People> peoples=null;
        try {
            BackUser user=backUserService.getBackUserById(userInfoForToken.getUserId());
            if (user.getRoleName().equals("M")||user.getRoleName().equals("院长")) {
                peoples=peopleService.findAllByStatus("0");
                List<String> p=new ArrayList<>();
                for (People ps:peoples) {
                    p.add(ps.getOpenId());
                }
                if (sttt.equalsIgnoreCase("1")) {
                    if (name == null || name.equalsIgnoreCase("")) {
                        ResponseModel<List<OvertimeleaveModel>> listResponseModel = new ResponseModel<>();
                        List<Overtimeleave> olList = olService.getOLList1(p);
                        List<OvertimeleaveModel> overtimeleaveModels = new ArrayList<>();
                        for (Overtimeleave overtimeleave : olList) {
                            OvertimeleaveModel overtimeleaveModel = new OvertimeleaveModel();
                            People people = peopleService.findPeople(overtimeleave.getOpenId());
                            Department byid = iDepartmentRepository.findByid(people.getDepartment());

                            overtimeleaveModel.setDepartment(byid.getDeptname());
                            overtimeleaveModel.setPeopleName(people.getPeopleName());
                            overtimeleaveModel.setJurisdiction(people.getJurisdiction());
                            overtimeleaveModel.setOvertimeleave(overtimeleave);
                            overtimeleaveModels.add(overtimeleaveModel);
                        }
                        if(overtimeleaveModels!=null&&overtimeleaveModels.size()!=0){
                            listResponseModel.setData(ListUtils.Pager(pageSize, pageNum, overtimeleaveModels));
                            listResponseModel.setPageSize(overtimeleaveModels.size());
                            listResponseModel.setPageNum(pageNum);
                            listResponseModel.setCode(ResultCode.SUCESS);
                        }
                        return listResponseModel;
                    } else {
                        List<People> allPeople = peopleService.findAllPeopleByStatus(name,"0");
                        List<OvertimeleaveModel> overtimeleaveModels = new ArrayList<>();
                        ResponseModel<List<OvertimeleaveModel>> listResponseModel = new ResponseModel<>();
                        for (People people : allPeople
                                ) {
                            List<Overtimeleave> olForOpenId2 = olService.getOLForOpenId1(people.getOpenId());
                            for (Overtimeleave overtimeleave : olForOpenId2
                                    ) {
                                OvertimeleaveModel overtimeleaveModel = new OvertimeleaveModel();
                                People people1 = peopleService.findPeople(overtimeleave.getOpenId());
                                Department byid = iDepartmentRepository.findByid(people1.getDepartment());

                                overtimeleaveModel.setDepartment(byid.getDeptname());
                                overtimeleaveModel.setPeopleName(people1.getPeopleName());
                                overtimeleaveModel.setJurisdiction(people1.getJurisdiction());
                                overtimeleaveModel.setOvertimeleave(overtimeleave);
                                overtimeleaveModels.add(overtimeleaveModel);
                            }
                        }
                        if(overtimeleaveModels!=null&&overtimeleaveModels.size()!=0){
                            listResponseModel.setData(ListUtils.Pager(pageSize, pageNum, overtimeleaveModels));
                            listResponseModel.setPageSize(overtimeleaveModels.size());
                            listResponseModel.setPageNum(pageNum);
                            listResponseModel.setCode(ResultCode.SUCESS);
                        }
                        return listResponseModel;
                    }
                } else if (sttt.equalsIgnoreCase("2")) {
                    if (name == null || name.equalsIgnoreCase("")) {

                        List<Overtimeleave> olList = olService.getOLList2(p);
                        List<OvertimeleaveModel> overtimeleaveModels = new ArrayList<>();

                        ResponseModel<List<OvertimeleaveModel>> listResponseModel = new ResponseModel<>();
                        for (Overtimeleave overtimeleave : olList) {
                            OvertimeleaveModel overtimeleaveModel = new OvertimeleaveModel();
                            People people = peopleService.findPeople(overtimeleave.getOpenId());
                            Department byid = iDepartmentRepository.findByid(people.getDepartment());

                            overtimeleaveModel.setDepartment(byid.getDeptname());
                            overtimeleaveModel.setPeopleName(people.getPeopleName());
                            overtimeleaveModel.setJurisdiction(people.getJurisdiction());
                            overtimeleaveModel.setOvertimeleave(overtimeleave);
                        }

                        if(overtimeleaveModels!=null&&overtimeleaveModels.size()!=0){
                            listResponseModel.setData(ListUtils.Pager(pageSize, pageNum, overtimeleaveModels));
                            listResponseModel.setPageSize(overtimeleaveModels.size());
                            listResponseModel.setPageNum(pageNum);
                            listResponseModel.setCode(ResultCode.SUCESS);
                        }
                        return listResponseModel;
                    } else {
                        List<People> allPeople = peopleService.findAllPeopleByStatus(name,"0");
                        List<OvertimeleaveModel> overtimeleaveModels = new ArrayList<>();
                        ResponseModel<List<OvertimeleaveModel>> listResponseModel = new ResponseModel<>();
                        for (People people : allPeople
                                ) {
                            List<Overtimeleave> olForOpenId2 = olService.getOLForOpenId2(people.getOpenId());
                            for (Overtimeleave overtimeleave : olForOpenId2
                                    ) {
                                OvertimeleaveModel overtimeleaveModel = new OvertimeleaveModel();
                                People people1 = peopleService.findPeople(overtimeleave.getOpenId());
                                Department byid = iDepartmentRepository.findByid(people1.getDepartment());

                                overtimeleaveModel.setDepartment(byid.getDeptname());
                                overtimeleaveModel.setPeopleName(people1.getPeopleName());
                                overtimeleaveModel.setJurisdiction(people1.getJurisdiction());
                                overtimeleaveModel.setOvertimeleave(overtimeleave);
                                overtimeleaveModels.add(overtimeleaveModel);
                            }
                        }
                        if(overtimeleaveModels!=null&&overtimeleaveModels.size()!=0){
                            listResponseModel.setData(ListUtils.Pager(pageSize, pageNum, overtimeleaveModels));
                            listResponseModel.setPageSize(overtimeleaveModels.size());
                            listResponseModel.setPageNum(pageNum);
                            listResponseModel.setCode(ResultCode.SUCESS);
                        }
                        return listResponseModel;
                    }
                } else {
                    if (name == null || name.equalsIgnoreCase("")) {
                        ResponseModel<List<OvertimeleaveModel>> listResponseModel = new ResponseModel<>();
                        List<Overtimeleave> olList = olService.getAll(p);

                        List<OvertimeleaveModel> overtimeleaveModels = new ArrayList<>();
                        for (Overtimeleave overtimeleave : olList) {
                            OvertimeleaveModel overtimeleaveModel = new OvertimeleaveModel();
                            People people = peopleService.findPeople(overtimeleave.getOpenId());
                            Department byid = iDepartmentRepository.findByid(people.getDepartment());
                            overtimeleaveModel.setDepartment(byid.getDeptname());
                            overtimeleaveModel.setPeopleName(people.getPeopleName());
                            overtimeleaveModel.setJurisdiction(people.getJurisdiction());

                            overtimeleaveModel.setOvertimeleave(overtimeleave);
                            overtimeleaveModels.add(overtimeleaveModel);
                        }

                        if(overtimeleaveModels!=null&&overtimeleaveModels.size()!=0){
                            listResponseModel.setData(ListUtils.Pager(pageSize, pageNum, overtimeleaveModels));
                            listResponseModel.setPageSize(overtimeleaveModels.size());
                            listResponseModel.setPageNum(pageNum);
                            listResponseModel.setCode(ResultCode.SUCESS);
                        }
                        return listResponseModel;
                    } else {
                        List<People> allPeople = peopleService.findAllPeopleByStatus(name,"0");
                        List<OvertimeleaveModel> overtimeleaveModels = new ArrayList<>();
                        ResponseModel<List<OvertimeleaveModel>> listResponseModel = new ResponseModel<>();
                        for (People people:allPeople
                                ) {
                            List<Overtimeleave> olForOpenId2 = olService.getAllOL(people.getOpenId());
                            for (Overtimeleave overtimeleave:olForOpenId2
                                    ) {
                                OvertimeleaveModel overtimeleaveModel = new OvertimeleaveModel();
                                People people1 = peopleService.findPeople(overtimeleave.getOpenId());
                                Department byid = iDepartmentRepository.findByid(people1.getDepartment());

                                overtimeleaveModel.setDepartment(byid.getDeptname());
                                overtimeleaveModel.setPeopleName(people1.getPeopleName());
                                overtimeleaveModel.setJurisdiction(people1.getJurisdiction());
                                overtimeleaveModel.setOvertimeleave(overtimeleave);
                                overtimeleaveModels.add(overtimeleaveModel);
                            }
                        }
                        if(overtimeleaveModels!=null&&overtimeleaveModels.size()!=0){
                            listResponseModel.setData(ListUtils.Pager(pageSize, pageNum, overtimeleaveModels));
                            listResponseModel.setPageSize(overtimeleaveModels.size());
                            listResponseModel.setPageNum(pageNum);
                            listResponseModel.setCode(ResultCode.SUCESS);
                        }
                        return listResponseModel;
                    }
                }
            }else if(user.getRoleName().equals("部长") ){
                peoples=peopleRepostitory.findAllByIsDeleteAndDepartmentAndJurisdictionLike("0",user.getDeptId(),"员工");
                List<String> p=new ArrayList<>();
                for (People ps:peoples) {
                    p.add(ps.getOpenId());
                }
                if (sttt.equalsIgnoreCase("1")) {
                    if (name == null || name.equalsIgnoreCase("")) {
                        ResponseModel<List<OvertimeleaveModel>> listResponseModel = new ResponseModel<>();
                        List<Overtimeleave> olList = olService.getOLList1(p);
                        List<OvertimeleaveModel> overtimeleaveModels = new ArrayList<>();
                        for (Overtimeleave overtimeleave : olList) {
                            OvertimeleaveModel overtimeleaveModel = new OvertimeleaveModel();
                            People people = peopleService.findPeople(overtimeleave.getOpenId());
                            Department byid = iDepartmentRepository.findByid(people.getDepartment());

                            overtimeleaveModel.setDepartment(byid.getDeptname());
                            overtimeleaveModel.setPeopleName(people.getPeopleName());
                            overtimeleaveModel.setJurisdiction(people.getJurisdiction());
                            overtimeleaveModel.setOvertimeleave(overtimeleave);
                            overtimeleaveModels.add(overtimeleaveModel);
                        }
                        if(overtimeleaveModels!=null&&overtimeleaveModels.size()!=0){
                            listResponseModel.setData(ListUtils.Pager(pageSize, pageNum, overtimeleaveModels));
                            listResponseModel.setPageSize(overtimeleaveModels.size());
                            listResponseModel.setPageNum(pageNum);
                            listResponseModel.setCode(ResultCode.SUCESS);
                        }
                        return listResponseModel;
                    } else {
                        List<People> allPeople = peopleRepostitory.findAllByPeopleNameLikeAndIsDeleteAndDepartmentAndJurisdiction(name,"0",user.getDeptId(),"员工");
                        List<OvertimeleaveModel> overtimeleaveModels = new ArrayList<>();
                        ResponseModel<List<OvertimeleaveModel>> listResponseModel = new ResponseModel<>();
                        for (People people : allPeople
                                ) {
                            List<Overtimeleave> olForOpenId2 = olService.getOLForOpenId1(people.getOpenId());
                            for (Overtimeleave overtimeleave : olForOpenId2
                                    ) {
                                OvertimeleaveModel overtimeleaveModel = new OvertimeleaveModel();
                                People people1 = peopleService.findPeople(overtimeleave.getOpenId());
                                Department byid = iDepartmentRepository.findByid(people1.getDepartment());

                                overtimeleaveModel.setDepartment(byid.getDeptname());
                                overtimeleaveModel.setPeopleName(people1.getPeopleName());
                                overtimeleaveModel.setJurisdiction(people1.getJurisdiction());
                                overtimeleaveModel.setOvertimeleave(overtimeleave);
                                overtimeleaveModels.add(overtimeleaveModel);
                            }
                        }
                        if(overtimeleaveModels!=null&&overtimeleaveModels.size()!=0){
                            listResponseModel.setData(ListUtils.Pager(pageSize, pageNum, overtimeleaveModels));
                            listResponseModel.setPageSize(overtimeleaveModels.size());
                            listResponseModel.setPageNum(pageNum);
                            listResponseModel.setCode(ResultCode.SUCESS);
                        }
                        return listResponseModel;
                    }
                } else if (sttt.equalsIgnoreCase("2")) {
                    if (name == null || name.equalsIgnoreCase("")) {

                        List<Overtimeleave> olList = olService.getOLList2(p);
                        List<OvertimeleaveModel> overtimeleaveModels = new ArrayList<>();

                        ResponseModel<List<OvertimeleaveModel>> listResponseModel = new ResponseModel<>();
                        for (Overtimeleave overtimeleave : olList) {
                            OvertimeleaveModel overtimeleaveModel = new OvertimeleaveModel();
                            People people = peopleService.findPeople(overtimeleave.getOpenId());
                            Department byid = iDepartmentRepository.findByid(people.getDepartment());

                            overtimeleaveModel.setDepartment(byid.getDeptname());
                            overtimeleaveModel.setPeopleName(people.getPeopleName());
                            overtimeleaveModel.setJurisdiction(people.getJurisdiction());
                            overtimeleaveModel.setOvertimeleave(overtimeleave);
                        }

                        if(overtimeleaveModels!=null&&overtimeleaveModels.size()!=0){
                            listResponseModel.setData(ListUtils.Pager(pageSize, pageNum, overtimeleaveModels));
                            listResponseModel.setPageSize(overtimeleaveModels.size());
                            listResponseModel.setPageNum(pageNum);
                            listResponseModel.setCode(ResultCode.SUCESS);
                        }
                        return listResponseModel;
                    } else {
                        List<People> allPeople = peopleRepostitory.findAllByPeopleNameLikeAndIsDeleteAndDepartmentAndJurisdiction(name,"0",user.getDeptId(),"员工");
                        List<OvertimeleaveModel> overtimeleaveModels = new ArrayList<>();
                        ResponseModel<List<OvertimeleaveModel>> listResponseModel = new ResponseModel<>();
                        for (People people : allPeople
                                ) {
                            List<Overtimeleave> olForOpenId2 = olService.getOLForOpenId2(people.getOpenId());
                            for (Overtimeleave overtimeleave : olForOpenId2
                                    ) {
                                OvertimeleaveModel overtimeleaveModel = new OvertimeleaveModel();
                                People people1 = peopleService.findPeople(overtimeleave.getOpenId());
                                Department byid = iDepartmentRepository.findByid(people1.getDepartment());

                                overtimeleaveModel.setDepartment(byid.getDeptname());
                                overtimeleaveModel.setPeopleName(people1.getPeopleName());
                                overtimeleaveModel.setJurisdiction(people1.getJurisdiction());
                                overtimeleaveModel.setOvertimeleave(overtimeleave);
                                overtimeleaveModels.add(overtimeleaveModel);
                            }
                        }
                        if(overtimeleaveModels!=null&&overtimeleaveModels.size()!=0){
                            listResponseModel.setData(ListUtils.Pager(pageSize, pageNum, overtimeleaveModels));
                            listResponseModel.setPageSize(overtimeleaveModels.size());
                            listResponseModel.setPageNum(pageNum);
                            listResponseModel.setCode(ResultCode.SUCESS);
                        }
                        return listResponseModel;
                    }
                } else {
                    if (name == null || name.equalsIgnoreCase("")) {
                        ResponseModel<List<OvertimeleaveModel>> listResponseModel = new ResponseModel<>();
                        List<Overtimeleave> olList = olService.getAll(p);

                        List<OvertimeleaveModel> overtimeleaveModels = new ArrayList<>();
                        for (Overtimeleave overtimeleave : olList) {
                            OvertimeleaveModel overtimeleaveModel = new OvertimeleaveModel();
                            People people = peopleService.findPeople(overtimeleave.getOpenId());
                            Department byid = iDepartmentRepository.findByid(people.getDepartment());
                            overtimeleaveModel.setDepartment(byid.getDeptname());
                            overtimeleaveModel.setPeopleName(people.getPeopleName());
                            overtimeleaveModel.setJurisdiction(people.getJurisdiction());

                            overtimeleaveModel.setOvertimeleave(overtimeleave);
                            overtimeleaveModels.add(overtimeleaveModel);
                        }

                        if(overtimeleaveModels!=null&&overtimeleaveModels.size()!=0){
                            listResponseModel.setData(ListUtils.Pager(pageSize, pageNum, overtimeleaveModels));
                            listResponseModel.setPageSize(overtimeleaveModels.size());
                            listResponseModel.setPageNum(pageNum);
                            listResponseModel.setCode(ResultCode.SUCESS);
                        }
                        return listResponseModel;
                    } else {
                        List<People> allPeople = peopleRepostitory.findAllByPeopleNameLikeAndIsDeleteAndDepartmentAndJurisdiction(name,"0",user.getDeptId(),"员工");
                        List<OvertimeleaveModel> overtimeleaveModels = new ArrayList<>();
                        ResponseModel<List<OvertimeleaveModel>> listResponseModel = new ResponseModel<>();
                        for (People people:allPeople
                                ) {
                            List<Overtimeleave> olForOpenId2 = olService.getAllOL(people.getOpenId());
                            for (Overtimeleave overtimeleave:olForOpenId2
                                    ) {
                                OvertimeleaveModel overtimeleaveModel = new OvertimeleaveModel();
                                People people1 = peopleService.findPeople(overtimeleave.getOpenId());
                                Department byid = iDepartmentRepository.findByid(people1.getDepartment());

                                overtimeleaveModel.setDepartment(byid.getDeptname());
                                overtimeleaveModel.setPeopleName(people1.getPeopleName());
                                overtimeleaveModel.setJurisdiction(people1.getJurisdiction());
                                overtimeleaveModel.setOvertimeleave(overtimeleave);
                                overtimeleaveModels.add(overtimeleaveModel);
                            }
                        }
                        if(overtimeleaveModels!=null&&overtimeleaveModels.size()!=0){
                            listResponseModel.setData(ListUtils.Pager(pageSize, pageNum, overtimeleaveModels));
                            listResponseModel.setPageSize(overtimeleaveModels.size());
                            listResponseModel.setPageNum(pageNum);
                            listResponseModel.setCode(ResultCode.SUCESS);
                        }
                        return listResponseModel;
                    }
                }
            }else if(user.getRoleName().equals("副院长") ){
                peoples=peopleRepostitory.findAllByIsDeleteAndDepartmentAndJurisdictionLike("0",user.getDeptId(),"部长");
                List<String> p=new ArrayList<>();
                for (People ps:peoples) {
                    p.add(ps.getOpenId());
                }
                if (sttt.equalsIgnoreCase("1")) {
                    if (name == null || name.equalsIgnoreCase("")) {
                        ResponseModel<List<OvertimeleaveModel>> listResponseModel = new ResponseModel<>();
                        List<Overtimeleave> olList = olService.getOLList1(p);
                        List<OvertimeleaveModel> overtimeleaveModels = new ArrayList<>();
                        for (Overtimeleave overtimeleave : olList) {
                            OvertimeleaveModel overtimeleaveModel = new OvertimeleaveModel();
                            People people = peopleService.findPeople(overtimeleave.getOpenId());
                            Department byid = iDepartmentRepository.findByid(people.getDepartment());

                            overtimeleaveModel.setDepartment(byid.getDeptname());
                            overtimeleaveModel.setPeopleName(people.getPeopleName());
                            overtimeleaveModel.setJurisdiction(people.getJurisdiction());
                            overtimeleaveModel.setOvertimeleave(overtimeleave);
                            overtimeleaveModels.add(overtimeleaveModel);
                        }
                        if(overtimeleaveModels!=null&&overtimeleaveModels.size()!=0){
                            listResponseModel.setData(ListUtils.Pager(pageSize, pageNum, overtimeleaveModels));
                            listResponseModel.setPageSize(overtimeleaveModels.size());
                            listResponseModel.setPageNum(pageNum);
                            listResponseModel.setCode(ResultCode.SUCESS);
                        }
                        return listResponseModel;
                    } else {
                        List<People> allPeople = peopleRepostitory.findAllByPeopleNameLikeAndIsDeleteAndDepartmentAndJurisdiction(name,"0",user.getDeptId(),"部长");
                        List<OvertimeleaveModel> overtimeleaveModels = new ArrayList<>();
                        ResponseModel<List<OvertimeleaveModel>> listResponseModel = new ResponseModel<>();
                        for (People people : allPeople
                                ) {
                            List<Overtimeleave> olForOpenId2 = olService.getOLForOpenId1(people.getOpenId());
                            for (Overtimeleave overtimeleave : olForOpenId2
                                    ) {
                                OvertimeleaveModel overtimeleaveModel = new OvertimeleaveModel();
                                People people1 = peopleService.findPeople(overtimeleave.getOpenId());
                                Department byid = iDepartmentRepository.findByid(people1.getDepartment());

                                overtimeleaveModel.setDepartment(byid.getDeptname());
                                overtimeleaveModel.setPeopleName(people1.getPeopleName());
                                overtimeleaveModel.setJurisdiction(people1.getJurisdiction());
                                overtimeleaveModel.setOvertimeleave(overtimeleave);
                                overtimeleaveModels.add(overtimeleaveModel);
                            }
                        }
                        if(overtimeleaveModels!=null&&overtimeleaveModels.size()!=0){
                            listResponseModel.setData(ListUtils.Pager(pageSize, pageNum, overtimeleaveModels));
                            listResponseModel.setPageSize(overtimeleaveModels.size());
                            listResponseModel.setPageNum(pageNum);
                            listResponseModel.setCode(ResultCode.SUCESS);
                        }
                        return listResponseModel;
                    }
                } else if (sttt.equalsIgnoreCase("2")) {
                    if (name == null || name.equalsIgnoreCase("")) {

                        List<Overtimeleave> olList = olService.getOLList2(p);
                        List<OvertimeleaveModel> overtimeleaveModels = new ArrayList<>();

                        ResponseModel<List<OvertimeleaveModel>> listResponseModel = new ResponseModel<>();
                        for (Overtimeleave overtimeleave : olList) {
                            OvertimeleaveModel overtimeleaveModel = new OvertimeleaveModel();
                            People people = peopleService.findPeople(overtimeleave.getOpenId());
                            Department byid = iDepartmentRepository.findByid(people.getDepartment());

                            overtimeleaveModel.setDepartment(byid.getDeptname());
                            overtimeleaveModel.setPeopleName(people.getPeopleName());
                            overtimeleaveModel.setJurisdiction(people.getJurisdiction());
                            overtimeleaveModel.setOvertimeleave(overtimeleave);
                        }

                        if(overtimeleaveModels!=null&&overtimeleaveModels.size()!=0){
                            listResponseModel.setData(ListUtils.Pager(pageSize, pageNum, overtimeleaveModels));
                            listResponseModel.setPageSize(overtimeleaveModels.size());
                            listResponseModel.setPageNum(pageNum);
                            listResponseModel.setCode(ResultCode.SUCESS);
                        }
                        return listResponseModel;
                    } else {
                        List<People> allPeople = peopleRepostitory.findAllByPeopleNameLikeAndIsDeleteAndDepartmentAndJurisdiction(name,"0",user.getDeptId(),"部长");
                        List<OvertimeleaveModel> overtimeleaveModels = new ArrayList<>();
                        ResponseModel<List<OvertimeleaveModel>> listResponseModel = new ResponseModel<>();
                        for (People people : allPeople
                                ) {
                            List<Overtimeleave> olForOpenId2 = olService.getOLForOpenId2(people.getOpenId());
                            for (Overtimeleave overtimeleave : olForOpenId2
                                    ) {
                                OvertimeleaveModel overtimeleaveModel = new OvertimeleaveModel();
                                People people1 = peopleService.findPeople(overtimeleave.getOpenId());
                                Department byid = iDepartmentRepository.findByid(people1.getDepartment());

                                overtimeleaveModel.setDepartment(byid.getDeptname());
                                overtimeleaveModel.setPeopleName(people1.getPeopleName());
                                overtimeleaveModel.setJurisdiction(people1.getJurisdiction());
                                overtimeleaveModel.setOvertimeleave(overtimeleave);
                                overtimeleaveModels.add(overtimeleaveModel);
                            }
                        }
                        if(overtimeleaveModels!=null&&overtimeleaveModels.size()!=0){
                            listResponseModel.setData(ListUtils.Pager(pageSize, pageNum, overtimeleaveModels));
                            listResponseModel.setPageSize(overtimeleaveModels.size());
                            listResponseModel.setPageNum(pageNum);
                            listResponseModel.setCode(ResultCode.SUCESS);
                        }
                        return listResponseModel;
                    }
                } else {
                    if (name == null || name.equalsIgnoreCase("")) {
                        ResponseModel<List<OvertimeleaveModel>> listResponseModel = new ResponseModel<>();
                        List<Overtimeleave> olList = olService.getAll(p);

                        List<OvertimeleaveModel> overtimeleaveModels = new ArrayList<>();
                        for (Overtimeleave overtimeleave : olList) {
                            OvertimeleaveModel overtimeleaveModel = new OvertimeleaveModel();
                            People people = peopleService.findPeople(overtimeleave.getOpenId());
                            Department byid = iDepartmentRepository.findByid(people.getDepartment());
                            overtimeleaveModel.setDepartment(byid.getDeptname());
                            overtimeleaveModel.setPeopleName(people.getPeopleName());
                            overtimeleaveModel.setJurisdiction(people.getJurisdiction());

                            overtimeleaveModel.setOvertimeleave(overtimeleave);
                            overtimeleaveModels.add(overtimeleaveModel);
                        }

                        if(overtimeleaveModels!=null&&overtimeleaveModels.size()!=0){
                            listResponseModel.setData(ListUtils.Pager(pageSize, pageNum, overtimeleaveModels));
                            listResponseModel.setPageSize(overtimeleaveModels.size());
                            listResponseModel.setPageNum(pageNum);
                            listResponseModel.setCode(ResultCode.SUCESS);
                        }
                        return listResponseModel;
                    } else {
                        List<People> allPeople = peopleRepostitory.findAllByPeopleNameLikeAndIsDeleteAndDepartmentAndJurisdiction(name,"0",user.getDeptId(),"部长");
                        List<OvertimeleaveModel> overtimeleaveModels = new ArrayList<>();
                        ResponseModel<List<OvertimeleaveModel>> listResponseModel = new ResponseModel<>();
                        for (People people:allPeople
                                ) {
                            List<Overtimeleave> olForOpenId2 = olService.getAllOL(people.getOpenId());
                            for (Overtimeleave overtimeleave:olForOpenId2
                                    ) {
                                OvertimeleaveModel overtimeleaveModel = new OvertimeleaveModel();
                                People people1 = peopleService.findPeople(overtimeleave.getOpenId());
                                Department byid = iDepartmentRepository.findByid(people1.getDepartment());

                                overtimeleaveModel.setDepartment(byid.getDeptname());
                                overtimeleaveModel.setPeopleName(people1.getPeopleName());
                                overtimeleaveModel.setJurisdiction(people1.getJurisdiction());
                                overtimeleaveModel.setOvertimeleave(overtimeleave);
                                overtimeleaveModels.add(overtimeleaveModel);
                            }
                        }
                        if(overtimeleaveModels!=null&&overtimeleaveModels.size()!=0){
                            listResponseModel.setData(ListUtils.Pager(pageSize, pageNum, overtimeleaveModels));
                            listResponseModel.setPageSize(overtimeleaveModels.size());
                            listResponseModel.setPageNum(pageNum);
                            listResponseModel.setCode(ResultCode.SUCESS);
                        }
                        return listResponseModel;
                    }
                }
            }else{//员工
                People pp=peopleRepostitory.findOneByOpenIdAndIsDelete(user.getWechat(),"0");
                List<String> p=new ArrayList<>();
                p.add(pp.getOpenId());
                if (sttt.equalsIgnoreCase("1")) {
                    if (name == null || name.equalsIgnoreCase("")) {
                        ResponseModel<List<OvertimeleaveModel>> listResponseModel = new ResponseModel<>();
                        List<Overtimeleave> olList = olService.getOLList1(p);
                        List<OvertimeleaveModel> overtimeleaveModels = new ArrayList<>();
                        for (Overtimeleave overtimeleave : olList) {
                            OvertimeleaveModel overtimeleaveModel = new OvertimeleaveModel();
                            People people = peopleService.findPeople(overtimeleave.getOpenId());
                            Department byid = iDepartmentRepository.findByid(people.getDepartment());

                            overtimeleaveModel.setDepartment(byid.getDeptname());
                            overtimeleaveModel.setPeopleName(people.getPeopleName());
                            overtimeleaveModel.setJurisdiction(people.getJurisdiction());
                            overtimeleaveModel.setOvertimeleave(overtimeleave);
                            overtimeleaveModels.add(overtimeleaveModel);
                        }
                        if(overtimeleaveModels!=null&&overtimeleaveModels.size()!=0){
                            listResponseModel.setData(ListUtils.Pager(pageSize, pageNum, overtimeleaveModels));
                            listResponseModel.setPageSize(overtimeleaveModels.size());
                            listResponseModel.setPageNum(pageNum);
                            listResponseModel.setCode(ResultCode.SUCESS);
                        }
                        return listResponseModel;
                    } else {
                        List<People> allPeople = peopleRepostitory.findAllByPeopleNameLikeAndIsDeleteAndDepartmentAndJurisdiction(name,"0",user.getDeptId(),"员工");
                        List<OvertimeleaveModel> overtimeleaveModels = new ArrayList<>();
                        ResponseModel<List<OvertimeleaveModel>> listResponseModel = new ResponseModel<>();
                        for (People people : allPeople
                                ) {
                            List<Overtimeleave> olForOpenId2 = olService.getOLForOpenId1(people.getOpenId());
                            for (Overtimeleave overtimeleave : olForOpenId2
                                    ) {
                                OvertimeleaveModel overtimeleaveModel = new OvertimeleaveModel();
                                People people1 = peopleService.findPeople(overtimeleave.getOpenId());
                                Department byid = iDepartmentRepository.findByid(people1.getDepartment());

                                overtimeleaveModel.setDepartment(byid.getDeptname());
                                overtimeleaveModel.setPeopleName(people1.getPeopleName());
                                overtimeleaveModel.setJurisdiction(people1.getJurisdiction());
                                overtimeleaveModel.setOvertimeleave(overtimeleave);
                                overtimeleaveModels.add(overtimeleaveModel);
                            }
                        }
                        if(overtimeleaveModels!=null&&overtimeleaveModels.size()!=0){
                            listResponseModel.setData(ListUtils.Pager(pageSize, pageNum, overtimeleaveModels));
                            listResponseModel.setPageSize(overtimeleaveModels.size());
                            listResponseModel.setPageNum(pageNum);
                            listResponseModel.setCode(ResultCode.SUCESS);
                        }
                        return listResponseModel;
                    }
                } else if (sttt.equalsIgnoreCase("2")) {
                    if (name == null || name.equalsIgnoreCase("")) {

                        List<Overtimeleave> olList = olService.getOLList2(p);
                        List<OvertimeleaveModel> overtimeleaveModels = new ArrayList<>();

                        ResponseModel<List<OvertimeleaveModel>> listResponseModel = new ResponseModel<>();
                        for (Overtimeleave overtimeleave : olList) {
                            OvertimeleaveModel overtimeleaveModel = new OvertimeleaveModel();
                            People people = peopleService.findPeople(overtimeleave.getOpenId());
                            Department byid = iDepartmentRepository.findByid(people.getDepartment());

                            overtimeleaveModel.setDepartment(byid.getDeptname());
                            overtimeleaveModel.setPeopleName(people.getPeopleName());
                            overtimeleaveModel.setJurisdiction(people.getJurisdiction());
                            overtimeleaveModel.setOvertimeleave(overtimeleave);
                        }

                        if(overtimeleaveModels!=null&&overtimeleaveModels.size()!=0){
                            listResponseModel.setData(ListUtils.Pager(pageSize, pageNum, overtimeleaveModels));
                            listResponseModel.setPageSize(overtimeleaveModels.size());
                            listResponseModel.setPageNum(pageNum);
                            listResponseModel.setCode(ResultCode.SUCESS);
                        }
                        return listResponseModel;
                    } else {
                        List<People> allPeople = peopleRepostitory.findAllByPeopleNameLikeAndIsDeleteAndDepartmentAndJurisdiction(name,"0",user.getDeptId(),"员工");
                        List<OvertimeleaveModel> overtimeleaveModels = new ArrayList<>();
                        ResponseModel<List<OvertimeleaveModel>> listResponseModel = new ResponseModel<>();
                        for (People people : allPeople
                                ) {
                            List<Overtimeleave> olForOpenId2 = olService.getOLForOpenId2(people.getOpenId());
                            for (Overtimeleave overtimeleave : olForOpenId2
                                    ) {
                                OvertimeleaveModel overtimeleaveModel = new OvertimeleaveModel();
                                People people1 = peopleService.findPeople(overtimeleave.getOpenId());
                                Department byid = iDepartmentRepository.findByid(people1.getDepartment());

                                overtimeleaveModel.setDepartment(byid.getDeptname());
                                overtimeleaveModel.setPeopleName(people1.getPeopleName());
                                overtimeleaveModel.setJurisdiction(people1.getJurisdiction());
                                overtimeleaveModel.setOvertimeleave(overtimeleave);
                                overtimeleaveModels.add(overtimeleaveModel);
                            }
                        }
                        if(overtimeleaveModels!=null&&overtimeleaveModels.size()!=0){
                            listResponseModel.setData(ListUtils.Pager(pageSize, pageNum, overtimeleaveModels));
                            listResponseModel.setPageSize(overtimeleaveModels.size());
                            listResponseModel.setPageNum(pageNum);
                            listResponseModel.setCode(ResultCode.SUCESS);
                        }
                        return listResponseModel;
                    }
                } else {
                    if (name == null || name.equalsIgnoreCase("")) {
                        ResponseModel<List<OvertimeleaveModel>> listResponseModel = new ResponseModel<>();
                        List<Overtimeleave> olList = olService.getAll(p);

                        List<OvertimeleaveModel> overtimeleaveModels = new ArrayList<>();
                        for (Overtimeleave overtimeleave : olList) {
                            OvertimeleaveModel overtimeleaveModel = new OvertimeleaveModel();
                            People people = peopleService.findPeople(overtimeleave.getOpenId());
                            Department byid = iDepartmentRepository.findByid(people.getDepartment());
                            overtimeleaveModel.setDepartment(byid.getDeptname());
                            overtimeleaveModel.setPeopleName(people.getPeopleName());
                            overtimeleaveModel.setJurisdiction(people.getJurisdiction());

                            overtimeleaveModel.setOvertimeleave(overtimeleave);
                            overtimeleaveModels.add(overtimeleaveModel);
                        }

                        if(overtimeleaveModels!=null&&overtimeleaveModels.size()!=0){
                            listResponseModel.setData(ListUtils.Pager(pageSize, pageNum, overtimeleaveModels));
                            listResponseModel.setPageSize(overtimeleaveModels.size());
                            listResponseModel.setPageNum(pageNum);
                            listResponseModel.setCode(ResultCode.SUCESS);
                        }
                        return listResponseModel;
                    } else {
                        List<People> allPeople = peopleRepostitory.findAllByPeopleNameLikeAndIsDeleteAndDepartmentAndJurisdiction(name,"0",user.getDeptId(),"员工");
                        List<OvertimeleaveModel> overtimeleaveModels = new ArrayList<>();
                        ResponseModel<List<OvertimeleaveModel>> listResponseModel = new ResponseModel<>();
                        for (People people:allPeople
                                ) {
                            List<Overtimeleave> olForOpenId2 = olService.getAllOL(people.getOpenId());
                            for (Overtimeleave overtimeleave:olForOpenId2
                                    ) {
                                OvertimeleaveModel overtimeleaveModel = new OvertimeleaveModel();
                                People people1 = peopleService.findPeople(overtimeleave.getOpenId());
                                Department byid = iDepartmentRepository.findByid(people1.getDepartment());

                                overtimeleaveModel.setDepartment(byid.getDeptname());
                                overtimeleaveModel.setPeopleName(people1.getPeopleName());
                                overtimeleaveModel.setJurisdiction(people1.getJurisdiction());
                                overtimeleaveModel.setOvertimeleave(overtimeleave);
                                overtimeleaveModels.add(overtimeleaveModel);
                            }
                        }
                        if(overtimeleaveModels!=null&&overtimeleaveModels.size()!=0){
                            listResponseModel.setData(ListUtils.Pager(pageSize, pageNum, overtimeleaveModels));
                            listResponseModel.setPageSize(overtimeleaveModels.size());
                            listResponseModel.setPageNum(pageNum);
                            listResponseModel.setCode(ResultCode.SUCESS);
                        }
                        return listResponseModel;
                    }
                }
            }
        }catch (Exception e){
            return ResponseModel.fail("", ResultCode.SYS_ERROR);
        }
    }



}

