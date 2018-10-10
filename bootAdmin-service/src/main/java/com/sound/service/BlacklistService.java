package com.sound.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sound.entity.BlackObject;
import com.sound.util.FormData;
import com.sound.util.PageInfo;

public interface BlacklistService {

	public List<Map<String, Object>> getAllBlacklistPage(PageInfo page) throws Exception;
	
	public List<Map<String, Object>> checkPhone(FormData data) throws Exception;
	
	public List<Map<String, Object>> getAllBlacklist() throws Exception;
	
	public Map<String, Object> getBlacklistById(FormData data) throws Exception ;

	public int saveBlacklist(FormData data) throws Exception ;

	public int deleteBlacklist(FormData data) throws Exception ;

	public int updateBlacklist(FormData data) throws Exception ;
	
	public int 	insertBatchBlackList(List<Map<String, Object>> batchList) throws Exception;

	public List<Map<String, Object>> checkPhoneByTxt(Map<String,Object> map) throws Exception;

	public List<String> selAllSysBlackList(FormData map) throws Exception;

	public int insBlackListMap(Map<String, Object> map) throws Exception;

	public Map<String, Object> getDelSysBlacklistBymdn(FormData data) throws Exception ;
	
	public List<String> selBlackList(FormData map) throws Exception;
	
}
