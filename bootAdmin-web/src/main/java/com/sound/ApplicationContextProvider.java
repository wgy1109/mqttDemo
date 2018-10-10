package com.sound;

import org.springframework.beans.BeansException;
import org.springframework.context.*;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextProvider implements ApplicationContextAware {
	 private static ApplicationContext context;

     private ApplicationContextProvider(){}

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public  static <T> T getBean(String name,Class<T> aClass){
        return context.getBean(name,aClass);
    }
}
