package com.sound.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sound.dao.DaoSupport;
import com.sound.service.BlacklistService;
import com.sound.util.FormData;
import com.sound.util.PageInfo;

@Service("blacklistService")
@SuppressWarnings("unchecked")
@Transactional
public class BlacklistServiceImpl implements BlacklistService {
	@Resource(name = "daoSupport")
	private DaoSupport daoSupport;

	public List<Map<String, Object>> getAllBlacklistPage(PageInfo page) throws Exception {
		Map<String, Object> countAll = (Map<String, Object>) daoSupport
				.findForObject("BlacklistMapper.getAllBlackcount", page);
		page.setTotalSize(Integer.parseInt(countAll.get("count").toString()));
		return (List<Map<String, Object>>) daoSupport.findForList("BlacklistMapper.getAllBlacklistp", page);
	}

	public List<Map<String, Object>> checkPhone(FormData data) throws Exception {
		return (List<Map<String, Object>>) daoSupport.findForList("BlacklistMapper.checkPhone", data);
	}

	public List<Map<String, Object>> getAllBlacklist() throws Exception {
		return (List<Map<String, Object>>) daoSupport.findForList("BlacklistMapper.getAllBlacklist", null);
	}

	public Map<String, Object> getBlacklistById(FormData data) throws Exception {
		Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(data.get("ID").toString());
        if( !isNum.matches() ){
        	return new HashMap<String, Object>();
        }
		return (Map<String, Object>) daoSupport.findForObject("BlacklistMapper.getBlacklistById", data);
	}

	public int saveBlacklist(FormData data) throws Exception {
		Object id = data.get("ID");
		int result = 0;
		if (id == null || id.toString().equals("")) {
			result = (Integer) daoSupport.save("BlacklistMapper.saveBlacklist", data);
		} else {
			result = (Integer) daoSupport.update("BlacklistMapper.updateBlacklist", data);
		}
		return result;
	}

	public int deleteBlacklist(FormData data) throws Exception {
		int result = (Integer) daoSupport.update("BlacklistMapper.deleteBlacklist", data);
		return result;
	}

	public int updateBlacklist(FormData data) throws Exception {
		int result = (Integer) daoSupport.update("BlacklistMapper.updateBlacklist", data);
		return result;
	}

	@Override
	public int insertBatchBlackList(List<Map<String, Object>> list) throws Exception {
		int result = (Integer) daoSupport.save("BlacklistMapper.insertBatchBlackList", list);
		return result;

	}

	public List<Map<String, Object>> checkPhoneByTxt(Map<String, Object> data) throws Exception {
		return (List<Map<String, Object>>) daoSupport.findForList("BlacklistMapper.checkPhoneByTxt", data);
	}

	@Override
	public List<String> selAllSysBlackList(FormData map) throws Exception {
		return (List<String>) daoSupport.findForList("BlacklistMapper.selAllSysBlackList", map);
	}

	@Override
	public int insBlackListMap(Map<String, Object> map) throws Exception {
		return (int) daoSupport.save("BlacklistMapper.insBlackListMap", map);
	}

	@Override
	public Map<String, Object> getDelSysBlacklistBymdn(FormData data) throws Exception {
		return (Map<String, Object>) daoSupport.findForObject("BlacklistMapper.getDelSysBlacklistBymdn", data);
	}

	@Override
	public List<String> selBlackList(FormData map) throws Exception {
		return (List<String>) daoSupport.findForList("BlacklistMapper.selBlackList", map);
	}

}
