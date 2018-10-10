package com.sound.util;

import org.springframework.context.ApplicationContext;

public class Const {
	
	// xml 文件保存地址
	public static final String FILEURL = "/usr/local/XMLfile/";
	
//	public static final String FILEURL = "D:/test/";

	public static final String SESSION_SP_USER = "sessionSpUser";
	public static final String SESSION_SECURITY_CODE = "sessionSecCode";
	// public static final String SESSION_USER = "sessionUserService";
	public static final String SESSION_MENU_LIST = "sessionMenu";
	public static final String SESSION_USER_RIGHTS = "sessionUserRights";
	public static final String SESSION_ROLE_RIGHTS = "sessionRoleRights";

	public static final String SESSION_MENU_SON = "sessionMenuSon";
	public static final String SESSION_MENU_USER_LIST = "sessionMenuUserList";
	public static final String SESSION_MENU_USER_THREE_CLASS_LIST = "sessionMenuUserThreeClassList";
	// 用户权限下的URL
	public static final String SESSION_AUTH_MENU_STR = "sessionAuthMenuStr";

	// 不对匹配该值的访问路径拦截（正则）
	public static final String NO_INTERCEPTOR_PATH = ".*/((login)|(logout)|(code)|(loginTimeout)|(reLoginpage)|(loginAgain)).*";
	// 该值会在web容器启动时由WebAppContextListener初始化
	public static ApplicationContext WEB_APP_CONTEXT = null;
	public static String mailServerHost = "smtp.exmail.qq.com";
	public static final String MailServerPort = "25";
	public static String mailAddress = "mail@163.com";  //邮箱
	public static String mailpassword = "pwd";

	public static final String URL = "/usr/local/images/target/";
	
	// 通道 属性
	public static final int Allnetwork = 1;
	public static final int mobile = 2;
	public static final int unicom = 3;
	public static final int telecom = 4;

	// --------------------------字符串参数 begin
	public static final String MESSAGEEXAMINE = "短信审核"; // 用于从MQ取出来的短信加入到内容审核的标题
	public static final String SUPPORTTYPE = "1";
	public static final String SALETYPE = "2";

	// --------------------------字符串参数 end

	public final static String EXCHANGE_NAME = "sms.direct";

	public static String[] letterArray = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
			"P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

}
