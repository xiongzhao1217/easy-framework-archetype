package com.easy.archetype;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * springboot 启动类
 *
 * <p>
 * 继承SpringBootServletInitializer类<br>
 * </p>
 *
 * @author xiongzhao
 */
@Slf4j
@EnableAsync
@EnableAspectJAutoProxy(exposeProxy = true)
@PropertySource(value = {"classpath:application.properties"}, encoding = "utf-8")
@MapperScan("com.easy.archetype.dao")
@ImportResource(locations={"classpath:conf/*.xml"})
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
		// 可在项目启动后执行任务
		logger.info("ApplicationRunner...");
	}
}
