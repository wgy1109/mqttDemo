package com.sound;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.web.client.RestTemplate;


@Configuration
public class AdminConfiguration implements SchedulingConfigurer{
	@Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory clientHttpRequestFactory) {
        return new RestTemplate(clientHttpRequestFactory);
    }

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(3600000);
        factory.setConnectTimeout(3600000);
        return factory;
    }
    
    @Bean
    public RestTemplate shortRestTemplate(ClientHttpRequestFactory shortClientHttpRequestFactory) {
        return new RestTemplate(shortClientHttpRequestFactory);
    }

    @Bean
    public ClientHttpRequestFactory shortClientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(5000);
        factory.setConnectTimeout(5000);
        return factory;
    }
    

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
    }

    private AtomicInteger scheduledThreadId = new AtomicInteger();

    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(20,r->new Thread(r,"scheduledPool-"+scheduledThreadId.addAndGet(1)));
    }

}
