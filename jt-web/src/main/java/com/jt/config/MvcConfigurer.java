package com.jt.config;

import com.jt.interceptor.UserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration						  //web.xml配置文件
public class MvcConfigurer implements WebMvcConfigurer{
	
	//开启匹配后缀型配置
	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {

		//开启后缀类型的匹配.  xxxx.html
		configurer.setUseSuffixPatternMatch(true);
	}

	@Autowired
	private UserInterceptor userInterceptor;

	//添加拦截器功能
	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		registry.addInterceptor(userInterceptor)
				.addPathPatterns("/cart/**","/order/**");
	}
}
