package com.jichuangsi.school.timingservice.service;

import com.jichuangsi.school.timingservice.exception.StaffHttpException;

public interface IHttpService {

    String findWxTokenModel(String code) throws StaffHttpException;

/*
<<<<<<< Updated upstream
    String findWxTokenModel2() throws ParentHttpException;
*/

/*
    String findWxUserInfo(String token,String openId,String code) throws ParentHttpException;
*/

/*    String findWxUserInfo2(String token,String openId) throws ParentHttpException;
=======*/
    String findWxUserInfo(String token, String openId/*,String code*/) throws StaffHttpException;

    String getWxTokenModel() throws StaffHttpException;

//    String senMsg() throws StaffHttpException;
}
