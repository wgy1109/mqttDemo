package com.sound.util.mqtt;

import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;

import com.sound.service.ParameterService;

public class MqttTest {
	
	public static void main(String[] args) {
		try {
//			pahomqtt("112");
//			mqttclient(null);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	// 向mqtt服务器发送消息，msg：消息内容
	 public static void pahomqtt(String msg) throws MqttException, InterruptedException {
	        Config config = new Config();
	        config.initpub();
	        MemoryPersistence persistence = new MemoryPersistence();

	        MqttConnectOptions options = new MqttConnectOptions();
	        options.setUserName(config.getAc());
	        options.setPassword(config.getPw().toCharArray());
	        options.setCleanSession(true);
	        options.setConnectionTimeout(10);

	        MqttClient client = new MqttClient(config.getHost(), config.getClientId(), persistence);
	        client.setCallback(new DeviceMqttCallback(config.getAc()));
	        client.connect(options);
	        client.subscribe(config.getTopic());

	        MqttTopic topic = client.getTopic(config.getTopic());
	        MqttMessage message = new MqttMessage(new byte[0]);    // 清空之前的 retain 消息
	        message.setRetained(true);
	        byte[] utf8 = msg.getBytes(StandardCharsets.UTF_8); 
	        message.setPayload(utf8);
	        topic.publish(message);
	        Thread.sleep(2000);
	        client.disconnect();
	        client.close();
	    }
	 
	 // 监控mqtt服务器，
	 public static void mqttclient(ParameterService service) throws URISyntaxException, InterruptedException {
	        Config config = new Config();
	        config.initsub();

	        MQTT mqtt = new MQTT();
	        mqtt.setHost(config.getHost());
	        mqtt.setClientId(UUID.randomUUID().toString().replace("-",""));
	        mqtt.setUserName(config.getAc());
	        mqtt.setPassword(config.getPw());
	        mqtt.setClientId(config.getClientId());
	        CallbackConnection connection = mqtt.callbackConnection();
	        connection.connect(new Callback<Void>() {
	            @Override
	            public void onSuccess(Void value) {
	                System.out.println("连接成功");
	                Topic topic = new Topic(config.getTopic(), QoS.AT_MOST_ONCE);
	                connection.subscribe(new Topic[]{topic}, new Callback<byte[]>() {
	                    @Override
	                    public void onSuccess(byte[] value) {
	                        System.out.println("订阅成功");
	                    }

	                    @Override
	                    public void onFailure(Throwable value) {
	                        System.out.println("订阅失败");
	                    }
	                });
	            }

	            @Override
	            public void onFailure(Throwable value) {
	                System.out.println("连接失败");
	                value.printStackTrace();
	            }
	        });
	        connection.listener(new DeviceMsgListener(service));
//	        Thread.sleep(1000 * 30);   // 防止瞬间主线程关闭导致连接被关闭
	    }

}
