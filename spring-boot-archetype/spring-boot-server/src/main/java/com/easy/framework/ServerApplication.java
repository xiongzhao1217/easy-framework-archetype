package com.easy.framework;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Slf4j
@PropertySources(
		value = {
				@PropertySource(value = {"classpath:application.properties"}, encoding = "utf-8")
		}
)
@MapperScan("com.easy.framework.dao")
@SpringBootApplication
public class ServerApplication implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
		logger.info("应用启动完成!");
	}

	@Override
	public void run(ApplicationArguments args) {
	    // 可在项目启动后执行任务
		logger.info("ApplicationRunner...");
	}
}
