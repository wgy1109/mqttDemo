package com.sound.controller.smsExportLine;

import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import com.sound.service.ParameterService;
import com.sound.util.mqtt.MqttTest;

//import com.alibaba.fastjson.JSON;
//import com.sound.controller.sms.SmsController;
//import com.sound.entity.*;
//import com.sound.service.RedisService;
//import com.sound.service.SmsChannelWarningService;
//import com.sound.service.SmsExportLineServer;
//import com.sound.service.SmsService;
//import com.sound.service.SpMonitorService;
//import com.sound.service.SpinfoService;
//import com.sound.util.AdminConfig;
//import com.sound.util.Const;
//import com.sound.util.FormData;
//import com.sound.util.PageInfo;

@Controller
public class RunExportController  implements InitializingBean {
	
	@Autowired
	public ParameterService service;

	@Override
	public void afterPropertiesSet() throws Exception {
		// 异步对比数据库与redis信息
		Thread t = new Thread(new Runnable(){
            public void run(){  
        		try {
					MqttTest.mqttclient(service);
				} catch (URISyntaxException | InterruptedException e) {
					e.printStackTrace();
				}
            }}, "synchronizationWarning");  
        t.start();  
	}
	
	/*
	@Autowired
	SmsExportLineServer exportservice;
	@Autowired
	public AdminConfig adminconfig;
	
	@Autowired
	private SmsService service;
	@Autowired
	private SmsController Smscontroller;
	@Autowired
	private RedisService redisService;
	@Autowired
	private SpinfoService spinfoService;
	@Autowired
	private SmsChannelWarningService cwservice;
	@Autowired
	private SpMonitorService smservice;
	@Value("${scheduled_corn}")
	protected Boolean scheduled_corn;
	
	
	public static AtomicInteger asyncThreadId = new AtomicInteger();
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	ScheduledExecutorService executor = Executors.newScheduledThreadPool(1, r->{
		 return new Thread(r,"RunExportThread");
	 });  
	ExecutorService executors = Executors.newFixedThreadPool(4, r->{return new Thread(r,"examineSimilarReject-"+asyncThreadId.addAndGet(1));});
	
	public void afterPropertiesSet() throws Exception {
		executor.scheduleWithFixedDelay( new RunExportLineController(exportservice, adminconfig, spinfoService), 20000, 2000, TimeUnit.MILLISECONDS);
		// 异步对比数据库与redis信息
		if(scheduled_corn){
		Thread t = new Thread(new Runnable(){  		
            public void run(){  
            	synchronizationChannelWarning();
            	synchronizationSpinfoWarningToRedis();
            }}, "synchronizationWarning");  
        t.start();  
		}
	}

	@PreDestroy
	public void destroy() throws Exception {
		logger.info("项目关闭，关闭异步线程！");
		executor.shutdown();
		executors.shutdownNow();  
	}
	
	
*/}
