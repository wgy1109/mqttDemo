package com.sound.service;

import java.util.List;
import java.util.Map;

import com.sound.util.FormData;
import com.sound.util.PageInfo;

public interface tCodeService{

	public List<Map<String, Object>> getAlltCodeByPage (PageInfo page )throws Exception;
	
	public Map<String, Object> gettCodeInfoById (FormData data) throws Exception;
	
	public int savetCode (FormData data) throws Exception ;
	
	public int deletetCode(FormData data) throws Exception;
	
}
