package com.sound.service;

import java.util.List;
import java.util.Map;

import com.sound.util.FormData;
import com.sound.util.PageInfo;

public interface ParameterService {
	
	public List<Map<String, Object>> getAlllistPage(PageInfo page) throws Exception;
	
	public Map<String, Object> getValueByid(FormData form) throws Exception;
	
	public int saveValue(FormData form) throws Exception;
	
	public int deleteValue(FormData form) throws Exception;
	
	public Map<String, Object> getValueByVersion(FormData form) throws Exception;
}
