package com.sound.controller.base;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

//import com.sound.service.ServiceHelper;
//import com.sound.sms.common.PackageStatus;
import com.sound.util.*;

public class BaseController {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final long serialVersionUID = 6357869213649815390L;

//	@Autowired
//	public AdminConfig adminconfig;

	/**
	 * 得到FormData
	 */
	public FormData getFormData() {
		return new FormData(this.getRequest());
	}

	/**
	 * 得到request对象
	 */
	public HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();

		return request;
	}

	/**
	 * 得到分页列表的信息
	 */
//	public PageInfo getPage() {
//
//		return new PageInfo();
//	}

	public String getIpAddr() {
		HttpServletRequest request = getRequest();
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 获取客户端的Ip
	 * 
	 * @param request
	 * @return
	 */
	public String getClientIp(HttpServletRequest request) {
		if (request.getHeader("X-Forwarded-For") == null || "".equals(request.getHeader("X-Forwarded-For"))) {
			return request.getRemoteAddr();
		} else {
			return request.getHeader("X-Forwarded-For");
		}
	}

	/**
	 * 记录系统日志
	 * 
	 * @param map
	 *            FormData map = new FormData(); map.put("sp_id", 0); IP_ID
	 *            map.put("username", userName); 操作用户姓名 map.put("opearte_type",
	 *            Const.USER_LOGIN); 用户操作类型 Const内操作类型 map.put("opearte_result",
	 *            1); 用户操作结果 map.put("opearte_content", "登录成功"); 用户操作
	 *            map.put("createtime", new Date()); 用户操作时间 map.put("ip",
	 *            loginip); 用户IP
	 */
	/*public void insertOperalog(FormData map) throws Exception {
		if (map != null) {
			map.put("sp_id", 0);
			String name = this.getFormData().getString("username");
			if (name == null) {
				name = this.getFormData().getString("userName");
			}
			map.put("username", name);
			map.put("ip", getClientIp(this.getRequest()));
			map.put("createtime", new Date());
			System.out.println("this is stop , insertOperalog ");
		}

	}*/
	
}
