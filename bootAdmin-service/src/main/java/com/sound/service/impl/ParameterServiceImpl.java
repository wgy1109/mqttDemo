package com.sound.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sound.dao.DaoSupport;
import com.sound.util.FormData;
import com.sound.service.ParameterService;
import com.sound.util.PageInfo;
import com.sound.util.Tools;

@Service("parameterService")
@Transactional
public class ParameterServiceImpl implements ParameterService {
	
	@Resource(name = "daoSupport")
	private DaoSupport daoSupport;

	@Override
	public List<Map<String, Object>> getAlllistPage(PageInfo page) throws Exception {
		return (List<Map<String, Object>>) daoSupport.findForList("ParameterMapper.getAlllistPage", page);
	}

	@Override
	public Map<String, Object> getValueByid(FormData form) throws Exception {
		return (Map<String, Object>) daoSupport.findForObject("ParameterMapper.getValueByid", form);
	}

	@Override
	public int saveValue(FormData form) throws Exception {
		if(form.containsKey("id") && Tools.notEmpty(form.getString("id"))){
			return (int) daoSupport.update("ParameterMapper.updateValue", form);
		} else {
			return (int) daoSupport.save("ParameterMapper.insertValue", form);
		}
	}

	@Override
	public int deleteValue(FormData form) throws Exception {
		return (int) daoSupport.delete("ParameterMapper.deleteValue", form);
	}
	
	@Override
	public Map<String, Object> getValueByVersion(FormData form) throws Exception {
		return (Map<String, Object>) daoSupport.findForObject("ParameterMapper.getValueByVersion", form);
	}

}
