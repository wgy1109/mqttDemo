package com.sound.util.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeviceMqttCallback implements MqttCallback {
	
	   Logger logger = LoggerFactory.getLogger(this.getClass());
	
	   public DeviceMqttCallback() {
	    }
	   
	   public DeviceMqttCallback(String ac) {
		   
	    }

	    @Override
	    public void connectionLost(Throwable cause) {
	        logger.info("connectionLost:" + cause.getMessage());
	    }

	    @Override
	    public void messageArrived(String topic, MqttMessage message) throws Exception {
	        logger.info("receive: [topic]:" + topic + "  [msg]:" + message.toString());
	    }

	    @Override
	    public void deliveryComplete(IMqttDeliveryToken token) {
	        logger.info("[isComplete]:" + token.isComplete() + "       " + token.getTopics());
	    }

}
