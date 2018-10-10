package com.sound.controller.parameter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sound.controller.base.BaseController;
import com.sound.service.ParameterService;
import com.sound.util.*;

@Controller
@RequestMapping("parameter")
public class ParameterController extends BaseController{
	
	@Autowired
	public ParameterService service;
	
	@RequestMapping("load")
	public String load(PageInfo page, HttpServletRequest request) throws Exception {
		return "parameter";
	}
	
	@RequestMapping("getMessage")
	@ResponseBody
	public Map<String, Object> getMessage(PageInfo page, HttpServletRequest request) throws Exception {
		page.setFormData(getFormData());
		List<Map<String, Object>> data = service.getAlllistPage(page);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("page", page);
		result.put("data", data);
		return result;
	}
	
	@RequestMapping("getValueByid")
	@ResponseBody
	public Map<String, Object> getValueByid(HttpServletRequest request) throws Exception {
		Map<String, Object> data = service.getValueByid(getFormData());
		return data;
	}
	
	@RequestMapping("saveValue")
	@ResponseBody
	public Map<String, Object> saveValue(HttpServletRequest request) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		FormData formdata = getFormData();
		Map<String, Object> versionMap = service.getValueByVersion(formdata);
		if(versionMap != null && versionMap.containsKey("id")) {
			if(!versionMap.get("id").toString().equals(formdata.get("id"))) {
				result.put("code", "201");
				result.put("msg", "版本号已存在");
				return result;
			}
		}
		String fileurl = Const.FILEURL+formdata.get("version")+".xml";
		try {
			Dom4JXml.strChangeXML(formdata.get("value").toString(), fileurl);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", "201");
			result.put("msg", "xml文件错误，请输入正确的xml文件信息！");
			return result;
		}
		formdata.put("value_url", fileurl);
		int res = service.saveValue(formdata);
		if(res>0) {
			result.put("code", "200");
			result.put("msg", "成功！");
			return result;
		} else {
			result.put("code", "201");
			result.put("msg", "保存失败！");
			return result;
		}
	}
	
	@RequestMapping("deleteValue")
	@ResponseBody
	public String deleteValue(HttpServletRequest request) throws Exception {
		int res = service.deleteValue(getFormData());
		return res>0 ? "200" : "201";
	}
	
	/*public Map<String, Object> getValueByVersion(String version) throws Exception {
		FormData formdata = new FormData();
		formdata.put("version", version);
		Map<String, Object> data = service.getValueByVersion(formdata);
		return data;
	}*/
	
	
	
	
}
