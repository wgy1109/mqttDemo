package com.sound.listener;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.sound.util.Const;

public class WebAppContextListener implements ServletContextListener {
	private final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
	private static Logger log = Logger.getLogger(WebAppContextListener.class);

	public void contextDestroyed(ServletContextEvent event) {
		if (scheduler.isShutdown()) {
			scheduler.shutdown();
		}
	}
	

	public void contextInitialized(ServletContextEvent event) {
		Const.WEB_APP_CONTEXT = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
		log.debug("========获取Spring WebApplicationContext");
		
	}
	
}