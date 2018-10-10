package com.sound.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sound.dao.DaoSupport;
import com.sound.service.TestappService;
import com.sound.util.FormData;

@Service("testappService")
@SuppressWarnings("unchecked")
@Transactional
public class TestappServiceImpl implements TestappService {
	@Resource(name = "daoSupport")
	private DaoSupport daoSupport;

	public List<Map<String, Object>> getTestappConf() throws Exception {
		return ((List<Map<String, Object>>) daoSupport.findForList(
				"TestappMapper.getTestappConf", null));
	}

	public int saveTestapp(FormData data) throws Exception {
		int result = (Integer) daoSupport.update("TestappMapper.updateTestapp",
				data);
		return result;
	}
}
