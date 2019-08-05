package com.jichuangsi.school.timingservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.jichuangsi.school.timingservice.config.HttpHeaderConfig;
import com.jichuangsi.school.timingservice.constant.ResultCode;
import com.jichuangsi.school.timingservice.exception.StaffHttpException;
import com.jichuangsi.school.timingservice.service.IHttpService;
import com.jichuangsi.school.timingservice.utils.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class HttpServiceImpl implements IHttpService {

    @Value("${com.jichuangsi.school.wx.host}")
    private String getWXHost;
    @Value("${com.jichuangsi.school.wx.getToken.path}")
    private String getTokenPath;
    @Value("${com.jichuangsi.school.wx.getToken.method}")
    private String getTokenMethod;
    @Value("${com.jichuangsi.school.wx.appid}")
    private String appId;
    @Value("${com.jichuangsi.school.wx.appSecret}")
    private String appSecret;
    @Value("${com.jichuangsi.school.wx.getUserInfo.path}")
    private String getUserPath;
    @Value("${com.jichuangsi.school.wx.getUserInfo.method}")
    private String getUserMethod;/*
    @Value("${com.jichuangsi.school.wx.sendMessage.path}")
    private String getSendMessagePath;
    @Value("${com.jichuangsi.school.wx.sendMessage.method}")
    private String getSendMessageMethod;*/

    @Resource
    private HttpHeaderConfig httpHeaderConfig;

    @Override
    public String findWxTokenModel(String code) throws StaffHttpException {
        Map<String,String> headers = httpHeaderConfig.getheaders();
        Map<String,String> querys = new HashMap<String,String>();
        querys.put("appid",appId);
        querys.put("secret",appSecret);
        querys.put("js_code",code);
        querys.put("grant_type","authorization_code");
        HttpResponse response = null;
        try {
            response = HttpUtils.doGet(getWXHost,getTokenPath,getTokenMethod,headers,querys);
            String result = EntityUtils.toString(response.getEntity());
            return result;
        } catch (Exception e) {
            throw new StaffHttpException(ResultCode.HTTP_IO_MSG);
        }
    }

/*    @Override
<<<<<<< Updated upstream
    public String findWxTokenModel2() throws ParentHttpException {
        Map<String,String> headers = httpHeaderConfig.getheaders();
        Map<String,String> querys = new HashMap<String,String>();
        querys.put("appid",appId);
        querys.put("secret",appSecret);
        querys.put("grant_type","client_credential");
        HttpResponse response = null;
        try {
            response = HttpUtils.doGet(getTokenHost,"/cgi-bin/token",getTokenMethod,headers,querys);
            String result = EntityUtils.toString(response.getEntity());
            return result;
        } catch (Exception e) {
            throw new ParentHttpException(ResultCode.HTTP_IO_MSG);
        }
    }*/


    public String findWxUserInfo(String token, String openId/*,String code*/) throws StaffHttpException {
        Map<String,String> headers = httpHeaderConfig.getheaders();
        Map<String,String> querys = new HashMap<String,String>();
        querys.put("access_token",token);
     /*   querys.put("appid",appId);
        querys.put("secret",appSecret);
        querys.put("grant_type","authorization_code");
        querys.put("code",code);*/
        querys.put("openid",openId);
        querys.put("lang","zh_CN");
        HttpResponse response = null;
        try {
            response = HttpUtils.doGet(getWXHost,getUserPath,getUserMethod,headers,querys);
            String result = new String(EntityUtils.toString(response.getEntity()).getBytes("ISO-8859-1"),"UTF-8");
            return result;
        } catch (Exception e) {
            throw new StaffHttpException(ResultCode.HTTP_IO_MSG);
        }
    }

    @Override
    public String getWxTokenModel() throws StaffHttpException {
        Map<String,String> headers = httpHeaderConfig.getheaders();
        Map<String,String> querys = new HashMap<String,String>();
        querys.put("appid",appId);
        querys.put("secret",appSecret);
        querys.put("grant_type","client_credentia");
        HttpResponse response = null;
        try {
            response = HttpUtils.doGet(getWXHost,"/cgi-bin/token",getTokenMethod,headers,querys);
            String result = EntityUtils.toString(response.getEntity());
            return result;
        } catch (Exception e) {
            throw new StaffHttpException(ResultCode.HTTP_IO_MSG);
        }
    }

    /*@Override
    public String senMsg() throws StaffHttpException {
        Map<String,String> headers = httpHeaderConfig.getheaders();
        Map<String,String> querys = new HashMap<String,String>();
        querys.put("access_token",getWxTokenModel());
        // 构造数据
        Map<String, Object> filterParams = new HashMap<>();
        filterParams.put("is_to_all", true);
        Map<String, Object> textParams = new HashMap<>();
        filterParams.put("content", "测试数据");
        Map<String, Object> date = new HashMap<>();
        date.put("filter", filterParams);
        date.put("text", textParams);
        date.put("msgtype", "text");
        String datestring=JSON.toJSONString(date);
        HttpResponse response = null;
        System.out.print(datestring);
        try {
            response = HttpUtils.doPost(getWXHost,getSendMessagePath,getSendMessageMethod,headers,querys,datestring);
            String result = EntityUtils.toString(response.getEntity());
            return result;
        } catch (Exception e) {
            throw new StaffHttpException(ResultCode.HTTP_IO_MSG);
        }
    }*/

/*    @Override
    public String findWxUserInfo2(String token,String openId) throws ParentHttpException{
        //"https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN"
        Map<String,String> headers = httpHeaderConfig.getheaders();
        Map<String,String> querys = new HashMap<String,String>();
        querys.put("access_token",token);
        querys.put("openid",openId);
        querys.put("lang","zh_CN");
        HttpResponse response = null;
        try {
            response = HttpUtils.doGet(getWXHost,"/cgi-bin/user/info",getTokenMethod,headers,querys);
            String result = EntityUtils.toString(response.getEntity());
            return result;
        } catch (Exception e) {
            throw new ParentHttpException(ResultCode.HTTP_IO_MSG);
        }
    }*/
}
