package com.sound.config;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MVCConfig  extends WebMvcConfigurerAdapter{

	/**
	 * 拦截器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		/*registry.addInterceptor(new LoginHandlerInterceptor()).addPathPatterns("/**");
		registry.addInterceptor(new UserRightsHandlerInterceptor()).addPathPatterns("/**");*/
	}

	/**
	 *  静态资源映射，用于项目直接访问静态资源文件
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**").addResourceLocations("/static/");
		super.addResourceHandlers(registry);
	}
	/**
	 * 文件上传
	 * @return
	 */
	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setMaxFileSize("20480kb");
		factory.setMaxRequestSize("20480kb");
		return factory.createMultipartConfig();
	}
	/**
	 * 异常处理
	 * @return
	 */
	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer () {
		return new EmbeddedServletContainerCustomizer() {
			
			@Override
			public void customize(ConfigurableEmbeddedServletContainer container) {
				container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/WEB-INF/jsp/common/error404.jsp"));
				container.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR,"/WEB-INF/jsp/common/error.jsp"));
			}
		};
	}

}
