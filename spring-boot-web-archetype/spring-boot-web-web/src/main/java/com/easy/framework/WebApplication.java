package com.easy.framework;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/**
 * springboot 启动类
 *
 * <p>
 * 继承SpringBootServletInitializer类，用于在j-one中使用外部tomcat容器发布。<br>
 * 由于做了tomcat安全加固，项目需要使用tomcat启动，无法通过main方法启动。
 * </p>
 *
 * @author xiongzhao
 */
@Slf4j
@PropertySources(
		value = {
				@PropertySource(value = {"classpath:application.properties"}, encoding = "utf-8")
		}
)
@MapperScan("com.easy.framework.dao")
@SpringBootApplication
public class WebApplication extends SpringBootServletInitializer implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);
		logger.info("应用启动完成!");
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(this.getClass());
	}

	@Override
	public void run(ApplicationArguments args) {
		logger.info("ApplicationRunner 启动成功，执行项目启动后的初始化任务...");
	}
}
