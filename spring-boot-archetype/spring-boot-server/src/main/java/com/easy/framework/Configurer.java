package com.easy.framework;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 配置申明
 *
 * <p>
 * 包含配置:
 * <br>1. 线程池，对线程池进行申明，定义全局线程池。
 * 包含注解:
 * <br>1. @EnableAsync注解，通过开启异步注解，可在需要异步执行的方法上添加 @Async("taskExecutor") 实现异步执行，其中 taskExecutor 即为配置的线程池名称。
 * <br>2. @EnableAspectJAutoProxy注解，开启切面注解，ump监控等需要依赖该注解。
 * </p>
 *
 * @author xiongzhao
 */
@Configuration
@EnableAsync
@EnableAspectJAutoProxy(exposeProxy = true)
@ImportResource(locations={"classpath:conf/*.xml"})
@Slf4j
public class Configurer {

    /**
     * 申明全局线程池
     * @return
     */
    @Bean("taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(30);
        executor.setQueueCapacity(200);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("taskExecutor-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }
}
