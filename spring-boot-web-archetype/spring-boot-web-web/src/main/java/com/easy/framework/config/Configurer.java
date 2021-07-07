package com.easy.framework.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.easy.framework.core.domain.http.response.ApiResult;
import com.easy.framework.core.enums.HttpResultEnum;
import com.easy.framework.core.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 配置申明
 *
 * <p>
 * 包含配置:
 * <br>1. FastJson转换器，接口出参使用fastjson作为序列化转换器，并自动将日期格式转换为字符串格式 yyyy-MM-dd HH:mm:ss，可根据需要变更 FastJsonConfig 相关配置。
 * <br>2. 统一异常处理，一般情况下Controller、service、manager层都不需要进行异常处理，由 spring ExceptionResolver 进行统一异常处理，当然，我们需要定义好自定义异常以及code码枚举。
 * <br>3. 线程池，对线程池进行申明，定义全局线程池。
 * <br>4. 单点登录拦截器。
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
public class Configurer implements WebMvcConfigurer {

    /**
     * api接口出参使用阿里 FastJson 作为JSON MessageConverter
     * @param converters {@link HttpMessageConverter} 列表
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.removeIf(converter -> converter instanceof MappingJackson2HttpMessageConverter || converter instanceof GsonHttpMessageConverter);
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();
        config.setDateFormat("yyyy-MM-dd HH:mm:ss");
        config.setSerializerFeatures(
                // 日期转换
                SerializerFeature.WriteDateUseDateFormat,
                // key转为string
                SerializerFeature.WriteNonStringKeyAsString,
                // 避免循环引用
                SerializerFeature.DisableCircularReferenceDetect);
        converter.setFastJsonConfig(config);
        converter.setDefaultCharset(Charset.forName("UTF-8"));
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        supportedMediaTypes.add(MediaType.APPLICATION_ATOM_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
        supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
        supportedMediaTypes.add(MediaType.APPLICATION_PDF);
        supportedMediaTypes.add(MediaType.APPLICATION_RSS_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_XHTML_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_XML);
        supportedMediaTypes.add(MediaType.IMAGE_GIF);
        supportedMediaTypes.add(MediaType.IMAGE_JPEG);
        supportedMediaTypes.add(MediaType.IMAGE_PNG);
        supportedMediaTypes.add(MediaType.TEXT_EVENT_STREAM);
        supportedMediaTypes.add(MediaType.TEXT_HTML);
        supportedMediaTypes.add(MediaType.TEXT_MARKDOWN);
        supportedMediaTypes.add(MediaType.TEXT_PLAIN);
        supportedMediaTypes.add(MediaType.TEXT_XML);
        converter.setSupportedMediaTypes(supportedMediaTypes);
        converters.add(converter);
    }

    /**
     * 统一异常处理
     * @param exceptionResolvers 统一异常处理器列表
     */
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add((request, response, handler, e) -> {
            ApiResult result = new ApiResult();
            if (e instanceof BindException || e instanceof MethodArgumentNotValidException) {
                // 处理hibernate validation校验异常
                FieldError error = e instanceof BindException ?
                        ((BindException) e).getBindingResult().getFieldError()
                        : ((MethodArgumentNotValidException)e).getBindingResult().getFieldError();
                String message = error.getDefaultMessage();
                int index = message.indexOf("AppException: ");
                if(index > -1) {
                    message = message.substring(index + 14);
                }
                result.setCode(HttpResultEnum.FAIL.getCode()).setMessage(message);
                logger.info(e.getMessage());
            }
            //业务失败的异常，如“账号或密码错误”
            else if (e instanceof AppException) {
                result.setCode(((AppException) e).getCode()).setMessage(e.getMessage());
                logger.info(e.getMessage(), e);
            } else if (e instanceof NoHandlerFoundException) {
                result.setCode(HttpResultEnum.NOT_FOUND.getCode()).setMessage("接口 [" + request.getRequestURI() + "] 不存在");
            } else if (e instanceof ServletException) {
                result.setCode(HttpResultEnum.FAIL.getCode()).setMessage(e.getMessage());
            } else if (e instanceof HttpMessageNotReadableException) {
                result.setCode(HttpResultEnum.FAIL.getCode()).setMessage("接口 [" + request.getRequestURI() + "] 参数错误");
                logger.error("接口 [" + request.getRequestURI() + "] 参数错误", e);
            } else {
                result.setCode(HttpResultEnum.FAIL.getCode()).setMessage("接口 [" + request.getRequestURI() + "] 内部错误，请联系管理员");
                String message;
                if (handler instanceof HandlerMethod) {
                    HandlerMethod handlerMethod = (HandlerMethod) handler;
                    message = String.format("接口 [%s] 内部错误，请联系管理员，方法：%s.%s，异常摘要：%s",
                            request.getRequestURI(),
                            handlerMethod.getBean().getClass().getName(),
                            handlerMethod.getMethod().getName(),
                            e.getMessage());
                } else {
                    message = e.getMessage();
                }
                logger.error(message, e);
            }
            responseResult(response, result);
            return new ModelAndView();
        });
    }

    /**
     * 回写报文
     * @param response
     * @param result 返回报文
     */
    private void responseResult(HttpServletResponse response, ApiResult result) {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-messageType", "application/json;charset=UTF-8");
        response.setStatus(200);
        try {
            response.getWriter().write(JSON.toJSONString(result));
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 单点登录拦截器
//        try {
//            SpringSSOInterceptor ssoInterceptor = new SpringSSOInterceptor();
//            registry.addInterceptor(ssoInterceptor)
//                    .addPathPatterns("/*");
//        } catch (Exception e) {
//            logger.error("启动单点登录拦截器失败", e);
//        }
    }

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
