package com.sound.service;

import java.util.List;
import java.util.Map;

import com.sound.util.FormData;

public interface TestappService{
	public List<Map<String, Object>> getTestappConf() throws Exception ;

	public int saveTestapp(FormData data) throws Exception ;
}
