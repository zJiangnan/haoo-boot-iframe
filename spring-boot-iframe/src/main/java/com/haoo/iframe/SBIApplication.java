package com.haoo.iframe;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;

//有父类需要添加包扫描
@SpringBootApplication(scanBasePackages = {"com.haoo.iframe"})
//开启定时任务
@EnableScheduling
//扫描 mapper
@MapperScan("com.haoo.iframe.mybatis.**")
public class SBIApplication {

	public static void main(String[] args) {
		SpringApplication.run(SBIApplication.class, args);
	}

	/**
	 * 配置文件上传大小
	 */
	@Bean
	public MultipartConfigElement initMultipartConfig() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		//上传文件大小
		factory.setMaxFileSize(DataSize.parse("800MB"));
		//请求大小
		factory.setMaxRequestSize(DataSize.parse("800MB"));
		return factory.createMultipartConfig();
	}

}
