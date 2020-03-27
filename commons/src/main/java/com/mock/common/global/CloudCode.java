package com.mock.common.global;

public class CloudCode {

	public static final int SUCCESS_CODE=10000;
	public static final String SUCCESS_MESSAGE="成功!";


	public static final int SYSTEM_EXCEPTION_CODE=10003;
	public static final String SYSTEM_EXCEPTION_MESSAGE="系统异常，请联系管理员查看!";

	public static final int NO_USER_CODE = 20000;
	public static final String NO_USER_CODE_MESSAGE="用户码不能为空!";

	public static final int NO_LOGIN_NAME=20001;
	public static final String NO_LOGIN_NAME_MESSAGE="登录名不能为空!";


	public static final int NO_LOGIN_PASSWORDS=20002;
	public static final String NO_LOGIN_PASSWORDS_MESSAGE="密码不能为空!";


	public static final int NO_PHONE_NO = 20003;
	public static final String NO_PHONE_NO_MESSAGE="手机号不能为空!";

	public static final int NO_CHECK_CODE = 20004;
	public static final String NO_CHECK_CODE_MESSAGE = "验证码不能为空!";



	public static final int USER_NOT_EXIST = 30000;
	public static final String USER_NOT_EXIST_MESSAGE="该用户不存在!";





	public static final int WRONG_PASSWORD = 40000;
	public static final String WRONG_PASSWORD_MESSAGE = "用户名不存在或密码错误!";

	public static final int WRONG_TOKEN = 40001;
	public static final String WRONG_TOKEN_MESSAGE = "token错误或过期";

	public static final int DIF_PWD=40002;
	public static final String DIF_PWD_MESSAGE = "2次密码输入不一致!";

	public static final int PERMISSION_DENIED = 40003;
	public static final String PERMISSION_DENIED_MESSAGE="权限不足";




	public static final int USER_EXIST = 50000;
	public static final String USER_EXIST_MESSAGE = "用户已存在!";


	public static final int NOT_SUPPORT_PHONE_NO = 60001;
	public static final String NOT_SUPPORT_PHONE_NO_MESSAGE = "手机号格式错误!";



	public static final int NOT_YOUR_MANIFEST = 70001 ;
	public static final String NOT_YOUR_MANIFEST_MESSAGE = "这不是你的接口清单!";

	public static final int SEND_2_MUCH = 70002 ;
	public static final String SEND_2_MUCH_MESSAGE = "短信请求太频繁啦～请稍后再试!";

	public static final int DIF_PASSWORD = 70003;
	public static final String DIF_PASSWORD_MESSAGE = "2次密码输入不一致!";

	public static final int WRONG_CHECK_CODE = 70004;
	public static final String WRONG_CHECK_CODE_MESSAGE = "验证码错误!";





}
