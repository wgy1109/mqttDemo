package com.sound.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sound.dao.DaoSupport;
import com.sound.service.tCodeService;
import com.sound.util.FormData;
import com.sound.util.PageInfo;

@Service("tCodeService")
@SuppressWarnings("unchecked")
@Transactional
public class tCodeServiceImpl implements tCodeService {

	@Resource(name = "daoSupport")
	private DaoSupport daoSupport;

	public List<Map<String, Object>> getAlltCodeByPage(PageInfo page)
			throws Exception {
		return (List<Map<String, Object>>) daoSupport.findForList(
				"tCodeMapper.getAlltCodeByPage", page);
	}

	public Map<String, Object> gettCodeInfoById(FormData data) throws Exception {
		return (Map<String, Object>) daoSupport.findForObject(
				"tCodeMapper.gettCodeInfoById", data);
	}

	public int savetCode(FormData data) throws Exception {
		Object id = data.get("ID");
		int result = 0;
		if (id == null || id.toString().equals("")) {
			result = (Integer) daoSupport.save("tCodeMapper.savetCode", data);
		} else {
			result = (Integer) daoSupport.update("tCodeMapper.updatetCode",
					data);
		}
		return result;
	}

	public int deletetCode(FormData data) throws Exception {
		int result = (Integer) daoSupport.delete("tCodeMapper.deletetCode",
				data);
		return result;
	}

}
