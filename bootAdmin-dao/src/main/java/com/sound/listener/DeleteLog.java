package com.sound.listener;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sound.util.FormData;


public class DeleteLog implements Runnable{
	public Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void run() {
		log.info("delete log start");
		FormData formdata = new FormData();
		formdata.put("deltime", new Date());
		log.info("delete log end");
	}
	
}
