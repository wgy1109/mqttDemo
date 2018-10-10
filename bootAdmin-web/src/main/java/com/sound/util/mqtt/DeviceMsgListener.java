package com.sound.util.mqtt;

import java.util.Map;

import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.sound.controller.parameter.ParameterController;
import com.sound.service.ParameterService;
import com.sound.util.FormData;

@Controller
public class DeviceMsgListener implements Listener {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	public ParameterService service;
	
	public DeviceMsgListener() {
	}
	
	public DeviceMsgListener(ParameterService service) {
		this.service = service;
	}

	@Override
	public void onConnected() {
		logger.info("onConnected!");
	}

	@Override
	public void onDisconnected() {
		logger.info("onDisconnected!");
	}

	@Override
	public void onPublish(UTF8Buffer topic, Buffer body, Runnable ack) {
		logger.info("topic:"+topic.toString()+",body:"+body.toString()+", body.ascii:"+body.ascii().toString()+",ack:"+ack.toString());
		try {
			FormData formdata = new FormData();
			formdata.put("version", body.ascii().toString());
			Map<String, Object> map = service.getValueByVersion(formdata);
			if(map != null && map.containsKey("value_url") && map.get("value_url") != null) {
				MqttTest.pahomqtt(map.get("value_url").toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onFailure(Throwable value) {
		logger.info("onFailure!");
	}
	
}
