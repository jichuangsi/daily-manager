package com.jichuangsi.school.timingservice.constant;

public class ResultCode {
	public static final String SUCESS = "0010";//成功
	public static final String PARAM_MISS = "0020";//缺少参数
	public static final String PARAM_ERR = "0021";//参数不正确
	public static final String TOKEN_MISS = "0030";//缺少token
	public static final String TOKEN_CHECK_ERR = "0031";//token校验异常
	public static final String SYS_ERROR = "0050";//系统内部异常
	public static final String SYS_BUSY = "0051";//熔断
	public static final String SOS = "007";//重复申诉

	public static final String SUCESS_MSG = "成功";
	public static final String PARAM_MISS_MSG = "缺少参数";
	public static final String PARAM_ERR_MSG = "参数不正确";
	public static final String TOKEN_MISS_MSG = "缺少token";
	public static final String TOKEN_CHECK_ERR_MSG = "token校验异常";
	public static final String SYS_ERROR_MSG = "系统繁忙";
	public static final String SYS_BUSY_MSG = "系统繁忙";

	public static final String SELECT_NULL_MSG = "查无此信息";
	public static final String NO_ACCESS = "无权访问";

	public static final String HTTP_IO_MSG = "http请求失败";

	public static final String OPENDID_NOFIND = "1";
	public static final String OPENDID_ISEXIST_MSG= "2";
	public final static String ACCOUNT_NOTEXIST_MSG = "账号不存在，或者账户密码错误";
	public final static String ACCOUNT_ISEXIST_MSG = "账号已存在";
	public final static String ACCOUNT_ISEXIST_MSG2 = "工号已存在";
	public static final String ROLE_STAFF_EXIST = "该角色仍有员工";
	public static final String DEPT_STAFF_EXIST = "该部门仍有员工";
	public static final String DEPT_BACKUSER_EXIST = "该部门仍有后台角色";
	public static final String ROLE_BACKUSER_EXIST = "该角色仍有后台角色";
	public static final String ROLE_URL_EXIST = "请先将该角色的权限移除";
	public final static String PWD_NOT_MSG = "两次密码错误";
	public final static String DEPT_ISEXIST_MSG = "部门已存在";
}
