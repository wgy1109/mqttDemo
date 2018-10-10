package com.sound;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.sound.util.mqtt.MqttTest;

//import com.sound.util.AdminConfig;

@Component
@ComponentScan
@EnableAutoConfiguration
@EnableScheduling
@EnableTransactionManagement
//@EnableConfigurationProperties({ AdminConfig.class })
@MapperScan(basePackages = "com.sound.dao")
public class Application extends SpringBootServletInitializer {
	private static final Logger log = LoggerFactory.getLogger(Application.class);
	public static final String version = "1.0";
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	public static void main(String[] args) throws Exception {
		log.info("bootAdmin 版本" + version + " 启动...");
		SpringApplication.run(Application.class, args);
	}
}