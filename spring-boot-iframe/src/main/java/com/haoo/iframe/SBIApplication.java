package com.haoo.iframe;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

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


}
