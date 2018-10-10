package com.sound.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//import com.alibaba.fastjson.JSON;
//import com.sound.sms.common.Constant;
//import com.sound.sms.redis.MessageTopic;
//import com.sound.sms.redis.RedisMessage;

@Component
public class FanoutMessageSender {
	
	/*@Autowired
	private RabbitTemplate rabbit;
	
	public <T extends RedisMessage> void sendMessage(T message){
		MessageTopic topic = message.getClass().getAnnotation(MessageTopic.class);
    	MessageProperties properties = new MessageProperties();
		properties.setHeader("topic", topic.value());
		Message msg = rabbit.getMessageConverter().toMessage(JSON.toJSONString(message), properties);
    	rabbit.convertAndSend("sms.fanout", null, msg);
	}*/
}
