package com.jichuangsi.school.timingservice.controller;

import com.jichuangsi.school.timingservice.constant.ResultCode;
import com.jichuangsi.school.timingservice.entity.Department;
import com.jichuangsi.school.timingservice.entity.Img;
import com.jichuangsi.school.timingservice.entity.People;
import com.jichuangsi.school.timingservice.entity.SQFlie;
import com.jichuangsi.school.timingservice.model.ResponseModel;
import com.jichuangsi.school.timingservice.model.SQFlieModel2;
import com.jichuangsi.school.timingservice.repository.IDepartmentRepository;
import com.jichuangsi.school.timingservice.service.PeopleService;
import com.jichuangsi.school.timingservice.service.SQService;
import com.jichuangsi.school.timingservice.utils.Byte2File;
import com.jichuangsi.school.timingservice.utils.ListUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/sq")
@CrossOrigin
@Api("申诉相关的api")
public class SQController {
    @Resource
    private IDepartmentRepository iDepartmentRepository;
    @Resource
    private SQService sqService;
    @Resource
    private PeopleService peopleService;

    @Value("${com.jichuangsi.school.file}")
    private String filePath;

    @ApiOperation(value = "申诉记录入口", notes = "")
    @PostMapping("/sqqq")
    public ResponseModel<String> SQ(@RequestParam String openId,@RequestParam String ruleId,@RequestParam String msg) throws IOException {
        ResponseModel<String> stringResponseModel = new ResponseModel<>();
        SQFlie sq = sqService.getSQ(openId, ruleId);
        if (sq!=null){
            stringResponseModel.setCode(ResultCode.SOS);
            return stringResponseModel;
        }
        String uuid=UUID.randomUUID().toString();
        sqService.saveFile(openId,uuid,ruleId,msg);

        stringResponseModel.setCode(ResultCode.SUCESS);
        stringResponseModel.setData(uuid);
        return stringResponseModel;
    }

    @ApiOperation(value = "申诉附件入口", notes = "")
    @PostMapping("/savefile")
    public ResponseModel<String> saveFile(@RequestParam MultipartFile file,@RequestParam String uuid) throws IOException {
        sqService.saveImg(file.getBytes(),uuid);
        ResponseModel<String> stringResponseModel = new ResponseModel<>();
        stringResponseModel.setCode(ResultCode.SUCESS);
        return stringResponseModel;
    }
    @ApiOperation(value = "申诉审核", notes = "")
    @PostMapping("/sssh")
    public ResponseModel<String> SSSH(@RequestBody SQFlie sqFlie){
        sqService.SSSH(sqFlie);
        ResponseModel<String> stringResponseModel = new ResponseModel<>();
        stringResponseModel.setMsg("ok");
        stringResponseModel.setCode(ResultCode.SUCESS);
        return stringResponseModel;
    }


    @ApiOperation(value = "根据uuid查图片", notes = "")
    @PostMapping("/getimg")
    public ResponseModel<List<byte[]>> SQGetImg( @RequestParam String uuid) throws FileNotFoundException {

        //List<File> files = new ArrayList<>();
        List<byte[]> imgs=new ArrayList<>();
        List<Img> f=sqService.getImgList(uuid);
            for (Img img:f) {
                byte[] b=img.getImg();
                imgs.add(b);
                //files.add(Byte2File.getFile(img.getImg(),filePath,UUID.randomUUID().toString()+".jpg"));
            }
        ResponseModel<List<byte[]>> odel = new ResponseModel<>();
        odel.setData(imgs);
        odel.setCode(ResultCode.SUCESS);
        return odel;

    }
    @ApiOperation(value = "根据openid查询申诉记录", notes = "")
    @PostMapping("/getsqrecord")
    public ResponseModel<List<SQFlie>> SQGetFile(@RequestParam String openId)  {
        List<SQFlie> file = sqService.getFile(openId);
        ResponseModel<List<SQFlie>> odel = new ResponseModel<>();
        odel.setData(file);
        odel.setCode(ResultCode.SUCESS);
        return odel;
    }
    @ApiOperation(value = "查看审批申诉记录", notes = "")
    @PostMapping("/getAllUnapprovedSQ")
    public ResponseModel<List<SQFlieModel2>> SQAllUnapproved(@RequestParam @Nullable String name,@RequestParam @Nullable String sttt,@RequestParam int pageSize,@RequestParam int pageNum) {
        List<People> peoples=peopleService.findAllPeopleByStatus("","0");
        List<String> p=new ArrayList<>();
        for (People ps:peoples) {
            p.add(ps.getOpenId());
        }
        if (sttt.equalsIgnoreCase("1")) {
            if (name == null || name.equalsIgnoreCase("")) {
                List<SQFlie> file = sqService.getAllUnapproved(p);
                List<SQFlieModel2> sqFlieModel2s = new ArrayList<>();
                ResponseModel<List<SQFlieModel2>> odel = new ResponseModel<>();
                for (SQFlie sqFlie : file
                        ) {
                    SQFlieModel2 sqFlieModel2 = new SQFlieModel2();

                    People servicePeople = peopleService.findPeople(sqFlie.getOpenId());
                    Department byid = iDepartmentRepository.findByid(servicePeople.getDepartment());
                    servicePeople.setDepartment(byid.getDeptname());
                    sqFlieModel2.setPeople(servicePeople);
                    sqFlieModel2.setSqFlie(sqFlie);
                    sqFlieModel2s.add(sqFlieModel2);
                }
                if(sqFlieModel2s.size()!=0){
                    List pager = ListUtils.Pager(pageSize, pageNum, sqFlieModel2s);
                    odel.setPageNum(pageNum);
                    odel.setPageSize(sqFlieModel2s.size());
                    odel.setData(pager);
                    odel.setCode(ResultCode.SUCESS);
                }
                return odel;
            } else {
                List<People> allPeople = peopleService.findAllPeopleByStatus(name,"0");

                List<SQFlieModel2> sqFlieModel2s = new ArrayList<>();
                for (People people : allPeople
                        ) {
                    List<SQFlie> file = sqService.getUnapprovedSQForName(people.getOpenId());
                    for (SQFlie sqFlie : file
                            ) {
                        SQFlieModel2 sqFlieModel2 = new SQFlieModel2();

                        People servicePeople = peopleService.findPeople(sqFlie.getOpenId());
                        Department byid = iDepartmentRepository.findByid(servicePeople.getDepartment());
                        servicePeople.setDepartment(byid.getDeptname());
                        sqFlieModel2.setPeople(servicePeople);
                        sqFlieModel2.setSqFlie(sqFlie);
                        sqFlieModel2s.add(sqFlieModel2);
                    }

                }
                ResponseModel<List<SQFlieModel2>> odel = new ResponseModel<>();
                if(sqFlieModel2s.size()!=0){
                    List pager = ListUtils.Pager(pageSize, pageNum, sqFlieModel2s);
                    odel.setPageNum(pageNum);
                    odel.setPageSize(sqFlieModel2s.size());
                    odel.setData(pager);
                    odel.setCode(ResultCode.SUCESS);
                }
                return odel;
            }
        } else if (sttt.equalsIgnoreCase("2")) {
            if (name == null || name.equalsIgnoreCase("")) {
                List<SQFlie> file = sqService.getAllapproved(p);
                List<SQFlieModel2> sqFlieModel2s = new ArrayList<>();
                ResponseModel<List<SQFlieModel2>> odel = new ResponseModel<>();
                for (SQFlie sqFlie : file
                        ) {
                    SQFlieModel2 sqFlieModel2 = new SQFlieModel2();

                    People servicePeople = peopleService.findPeople(sqFlie.getOpenId());
                    Department byid = iDepartmentRepository.findByid(servicePeople.getDepartment());
                    servicePeople.setDepartment(byid.getDeptname());
                    sqFlieModel2.setPeople(servicePeople);

                    sqFlieModel2.setSqFlie(sqFlie);
                    sqFlieModel2s.add(sqFlieModel2);
                }
                if(sqFlieModel2s.size()!=0){
                    List pager = ListUtils.Pager(pageSize, pageNum, sqFlieModel2s);
                    odel.setPageNum(pageNum);
                    odel.setPageSize(sqFlieModel2s.size());
                    odel.setData(pager);
                    odel.setCode(ResultCode.SUCESS);
                }
                return odel;
            } else {
                List<People> allPeople = peopleService.findAllPeopleByStatus(name,"0");

                List<SQFlieModel2> sqFlieModel2s = new ArrayList<>();
                for (People people : allPeople
                        ) {
                    List<SQFlie> file = sqService.getapprovedSQForName(people.getOpenId());
                    for (SQFlie sqFlie : file
                            ) {
                        SQFlieModel2 sqFlieModel2 = new SQFlieModel2();

                        People servicePeople = peopleService.findPeople(sqFlie.getOpenId());
                        Department byid = iDepartmentRepository.findByid(servicePeople.getDepartment());
                        servicePeople.setDepartment(byid.getDeptname());
                        sqFlieModel2.setPeople(servicePeople);
                        sqFlieModel2.setSqFlie(sqFlie);
                        sqFlieModel2s.add(sqFlieModel2);
                    }

                }
                ResponseModel<List<SQFlieModel2>> odel = new ResponseModel<>();
                if(sqFlieModel2s.size()!=0){
                    List pager = ListUtils.Pager(pageSize, pageNum, sqFlieModel2s);
                    odel.setPageNum(pageNum);
                    odel.setPageSize(sqFlieModel2s.size());
                    odel.setData(pager);
                    odel.setCode(ResultCode.SUCESS);
                }
                return odel;
            }
        } else {
            if (name == null || name.equalsIgnoreCase("")) {
                List<SQFlie> file = sqService.getAllsq(p);
                List<SQFlieModel2> sqFlieModel2s = new ArrayList<>();
                ResponseModel<List<SQFlieModel2>> odel = new ResponseModel<>();
                for (SQFlie sqFlie : file
                        ) {
                    SQFlieModel2 sqFlieModel2 = new SQFlieModel2();

                    People servicePeople = peopleService.findPeople(sqFlie.getOpenId());
                    Department byid = iDepartmentRepository.findByid(servicePeople.getDepartment());
                    servicePeople.setDepartment(byid.getDeptname());
                    sqFlieModel2.setPeople(servicePeople);
                    sqFlieModel2.setSqFlie(sqFlie);
                    sqFlieModel2s.add(sqFlieModel2);
                }
                if(sqFlieModel2s.size()!=0){
                    List pager = ListUtils.Pager(pageSize, pageNum, sqFlieModel2s);
                    odel.setPageNum(pageNum);
                    odel.setPageSize(sqFlieModel2s.size());
                    odel.setData(pager);
                    odel.setCode(ResultCode.SUCESS);
                }
                return odel;
            }else {
                List<People> allPeople = peopleService.findAllPeopleByStatus(name,"0");

                List<SQFlieModel2> sqFlieModel2s = new ArrayList<>();
                for (People people : allPeople
                        ) {
                    List<SQFlie> file = sqService.getSQ2(people.getOpenId());
                    for (SQFlie sqFlie : file
                            ) {
                        SQFlieModel2 sqFlieModel2 = new SQFlieModel2();

                        People servicePeople = peopleService.findPeople(sqFlie.getOpenId());
                        Department byid = iDepartmentRepository.findByid(servicePeople.getDepartment());
                        servicePeople.setDepartment(byid.getDeptname());
                        sqFlieModel2.setPeople(servicePeople);
                        sqFlieModel2.setSqFlie(sqFlie);
                        sqFlieModel2s.add(sqFlieModel2);
                    }

                }
                ResponseModel<List<SQFlieModel2>> odel = new ResponseModel<>();
                if(sqFlieModel2s.size()!=0){
                    List pager = ListUtils.Pager(pageSize, pageNum, sqFlieModel2s);
                    odel.setPageNum(pageNum);
                    odel.setPageSize(sqFlieModel2s.size());
                    odel.setData(pager);
                    odel.setCode(ResultCode.SUCESS);
                }
                return odel;
            }
        }

    }
}

